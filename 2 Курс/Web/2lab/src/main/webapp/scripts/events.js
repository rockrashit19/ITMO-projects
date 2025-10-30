document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('areaCanvas');
    const ctx = canvas.getContext('2d');
    const rSelect = document.getElementById('rSelect');
    const form = document.getElementById('pointForm');
    const yInput = document.getElementById('yInput');
    let points = Array.isArray(window.initialPoints) ? window.initialPoints.slice() : [];
    if (window.latestData) {
        points.push({
            x: window.latestData.x,
            y: window.latestData.y,
            r: window.latestData.r,
            inside: window.latestData.inside
        });
    }
    drawAxes(canvas, ctx);
    form.addEventListener('submit', e => {
        const y = parseFloat(yInput.value);
        if (isNaN(y) || y < -3 || y > 3) {
            alert('Y должен быть числом от -3 до 3');
            e.preventDefault();
        }
    });
    rSelect.addEventListener('change', () => {
        drawAxes(canvas, ctx);
        const rValue = parseFloat(rSelect.value);
        if (!isNaN(rValue) && rValue > 0) {
            drawArea(canvas, ctx, rValue);
            drawPoints(canvas, ctx, rValue, points);
        }
    });
    canvas.addEventListener('click', event => {
        const rValueStr = rSelect.value;
        if (!rValueStr) {
            alert('Радиус не установлен!');
            return;
        }
        const rValue = parseFloat(rValueStr);
        if (isNaN(rValue) || rValue <= 0) {
            alert('Радиус должен быть положительным числом!');
            return;
        }
        const rect = canvas.getBoundingClientRect();
        const px = event.clientX - rect.left;
        const py = event.clientY - rect.top;
        const x = (px - canvas.width / 2) / (canvas.width / 12);
        const y = (canvas.height / 2 - py) / (canvas.width / 12);
        if (isNaN(x) || isNaN(y)) {
            alert('Некорректные координаты!');
            return;
        }
        const query = `controller?x=${x.toFixed(2)}&y=${y.toFixed(2)}&r=${rValueStr}`;
        console.log('Запрос:', query);
        window.location.href = query;
    });
});
