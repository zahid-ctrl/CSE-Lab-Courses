document.addEventListener("DOMContentLoaded", () => {
  const startSelect = document.getElementById("start");
  const endSelect = document.getElementById("end");
  const resultDiv = document.getElementById("result");
  const button = document.getElementById("calculateBtn");

  // Load stations from PHP
  fetch("get_fare.php?action=stations")
    .then(res => res.json())
    .then(stations => {
      stations.forEach(station => {
        startSelect.add(new Option(station, station));
        endSelect.add(new Option(station, station));
      });
    });

  // Calculate fare
  button.addEventListener("click", () => {
    const start = startSelect.value;
    const end = endSelect.value;

    if (!start || !end) {
      resultDiv.innerHTML = "⚠️ Please select both stations.";
      return;
    }

    if (start === end) {
      resultDiv.innerHTML = "Same station selected. Fare: ৳20";
      return;
    }

    fetch(`get_fare.php?action=fare&start=${start}&end=${end}`)
      .then(res => res.text())
      .then(data => (resultDiv.innerHTML = data))
      .catch(err => {
        console.error(err);
        resultDiv.innerHTML = "Error fetching fare.";
      });
  });
});
