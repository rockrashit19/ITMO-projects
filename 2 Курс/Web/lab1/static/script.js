const state = {
  x: -3,
  y: -3,
  r: 1,
};

const table = document.getElementById("resultTable");
const tbody = table.querySelector('tbody'); // Получаем tbody
const error = document.getElementById("error");
const possibleYs = new Set([-3, -2, -1, 0, 1, 2, 3, 4, 5]);
const possibleRs = new Set([1, 2, 3, 4, 5]);

const validateState = (state) => {
  if (isNaN(state.x) || state.x < -3 || state.x > 3) {
    error.hidden = false;
    error.innerText = "x must be in range [-3, 3]";
    throw new Error("Invalid state");
  }

  if (isNaN(state.y) || !possibleYs.has(state.y)) {
    error.hidden = false;
    error.innerText = `y must be in [${[...possibleYs].join(", ")}]`;
    throw new Error("Invalid state");
  }

  if (isNaN(state.r) || !possibleRs.has(state.r)) {
    error.hidden = false;
    error.innerText = `r must be in [${[...possibleRs].join(", ")}]`;
    throw new Error("Invalid state");
  }

  error.hidden = true;
}

let selectedRBtn = null;

document.addEventListener('DOMContentLoaded', () => {
  const xInput = document.getElementById('x');
  xInput.addEventListener('input', () => {
    state.x = parseInt(xInput.value);
  });

  const yRadios = document.getElementsByName('y');
  yRadios.forEach(radio => {
    radio.addEventListener('change', (ev) => {
      state.y = parseFloat(ev.target.value);
    });
  });

  document.querySelectorAll('.r-button').forEach(button => {
    button.addEventListener('click', (ev) => {
      if (selectedRBtn) selectedRBtn.style.border = "";
      selectedRBtn = button;
      state.r = parseFloat(ev.target.getAttribute('data-r'));
      selectedRBtn.style.border = "#FF6961 1px solid";
    });
  });

  document.getElementById('submit').addEventListener('click', async (ev) => {
    ev.preventDefault();

    try {
      validateState(state);

      const newRow = tbody.insertRow(0);

      const rowX = newRow.insertCell(0);
      const rowY = newRow.insertCell(1);
      const rowR = newRow.insertCell(2);
      const rowTime = newRow.insertCell(3);
      const rowExecTime = newRow.insertCell(4);
      const rowResult = newRow.insertCell(5);

      // const response = await fetch(`http://localhost:8080/calculate?x=${state.x}&y=${state.y}&r=${state.r}`, {method: "GET"});
      const response = await fetch(`http://localhost:8080/fcgi-bin/labwork1.jar?x=${state.x}&y=${state.y}&r=${state.r}`, {method: "GET"});


      const results = {
        x: state.x,
        y: state.y,
        r: state.r,
        execTime: "",
        time: "",
        result: "error",
      };

      if (response.ok) {
        const result = await response.json();
        results.time = new Date(result.time).toLocaleString();
        results.execTime = `${result.workTime} ns`;
        results.result = result.result === "in" ? "Yes" : "No";
      } else if (response.status === 400) {
        const result = await response.json();
        results.time = "N/A";
        results.execTime = "N/A";
        results.result = `error: ${result.error}`;
      } else {
        results.time = "N/A";
        results.execTime = "N/A";
        results.result = "error";
      }

      const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
      localStorage.setItem("results", JSON.stringify([...prevResults, results]));

      rowX.innerText = results.x.toString();
      rowY.innerText = results.y.toString();
      rowR.innerText = results.r.toString();
      rowTime.innerText = results.time;
      rowExecTime.innerText = results.execTime;
      rowResult.innerText = results.result;
    } catch (e) {
      console.error(e);
    }
  });

  // Загружаем предыдущие результаты, добавляя их сверху
  const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
  prevResults.forEach(result => {
    const newRow = tbody.insertRow(0); // Добавляем в начало

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    rowX.innerText = result.x.toString();
    rowY.innerText = result.y.toString();
    rowR.innerText = result.r.toString();
    rowTime.innerText = result.time;
    rowExecTime.innerText = result.execTime;
    rowResult.innerText = result.result;
  });
});