<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "mrt_db";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$action = $_GET['action'] ?? '';

if ($action === 'stations') {
  $stations = [];
  $result = $conn->query("SELECT DISTINCT start_station FROM fares UNION SELECT DISTINCT end_station FROM fares");
  while ($row = $result->fetch_assoc()) {
    $stations[] = $row['start_station'];
  }
  echo json_encode($stations);
} elseif ($action === 'fare') {
  $start = $_GET['start'];
  $end = $_GET['end'];

  $sql = "SELECT fare FROM fares WHERE start_station='$start' AND end_station='$end'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {
    $fare = $result->fetch_assoc()['fare'];
    echo "Fare from <b>$start</b> to <b>$end</b> is <b>৳$fare</b>";
  } else {
    echo " No fare data found for $start → $end.";
  }
}
$conn->close();
?>
