const startSelect = document.getElementById("start");
const endSelect = document.getElementById("end");
const resultDiv = document.getElementById("result");

// Populate station options
stations.forEach((station) => {
  const option1 = new Option(station, station);
  const option2 = new Option(station, station);
  startSelect.add(option1);
  endSelect.add(option2);
});

function calculateFare() {
  const from = startSelect.value;
  const to = endSelect.value;

  if (from === to) {
    resultDiv.innerText = "You selected the same station.";
    return;
  }

  const fromIndex = stations.indexOf(from);
  const toIndex = stations.indexOf(to);
  const fare = fareTable[from][toIndex];

  resultDiv.innerText = `Fare from "${from}" to "${to}" is ৳${fare}`;
}
