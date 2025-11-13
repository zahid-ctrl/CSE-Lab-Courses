<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "mrt_db";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$card = $_POST['card'];
$name = $_POST['name'];
$email = $_POST['email'];
$phone = $_POST['phone'];

$sql = "INSERT INTO users (card_number, name, email, phone)
        VALUES ('$card', '$name', '$email', '$phone')";

if ($conn->query($sql) === TRUE) {
  header("Location: dashboard.html");
  exit();
} else {
  echo "Error: " . $conn->error;
}
$conn->close();
?>
