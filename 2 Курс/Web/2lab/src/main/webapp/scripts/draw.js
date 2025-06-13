function drawAxes(canvas, ctx) {
    const WIDTH = canvas.width;
    const HEIGHT = canvas.height;
    const CENTER_X = WIDTH / 2;
    const CENTER_Y = HEIGHT / 2;
    const UNIT_PIXELS = WIDTH / 12;
    ctx.clearRect(0, 0, WIDTH, HEIGHT);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(CENTER_X, 0);
    ctx.lineTo(CENTER_X, HEIGHT);
    ctx.moveTo(0, CENTER_Y);
    ctx.lineTo(WIDTH, CENTER_Y);
    ctx.stroke();
    ctx.font = '12px Arial';
    ctx.fillStyle = 'black';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    for (let i = -6; i <= 6; i += 1) {
        const xPos = CENTER_X + i * UNIT_PIXELS;
        const yPos = CENTER_Y - i * UNIT_PIXELS;
        ctx.beginPath();
        ctx.moveTo(xPos, CENTER_Y - 5);
        ctx.lineTo(xPos, CENTER_Y + 5);
        ctx.stroke();
        if (i !== 0) {
            ctx.fillText(i.toString(), xPos, CENTER_Y + 8);
        }
        ctx.beginPath();
        ctx.moveTo(CENTER_X - 5, yPos);
        ctx.lineTo(CENTER_X + 5, yPos);
        ctx.stroke();
        if (i !== 0) {
            ctx.textAlign = 'right';
            ctx.textBaseline = 'middle';
            ctx.fillText(i.toString(), CENTER_X - 8, yPos);
            ctx.textAlign = 'center';
            ctx.textBaseline = 'top';
        }
    }
    ctx.beginPath();

    // стрелки
    ctx.moveTo(CENTER_X, 0);
    ctx.lineTo(CENTER_X - 6, 14);
    ctx.moveTo(CENTER_X, 0);
    ctx.lineTo(CENTER_X + 6, 14);
    ctx.moveTo(WIDTH, CENTER_Y);
    ctx.lineTo(WIDTH - 14, CENTER_Y - 6);
    ctx.moveTo(WIDTH, CENTER_Y);
    ctx.lineTo(WIDTH - 14, CENTER_Y + 6);
    ctx.stroke();
}
function drawArea(canvas, ctx, r) {
    if (isNaN(r) || r <= 0) return;
    const UNIT_PIXELS = canvas.width / 12;
    const CENTER_X = canvas.width / 2;
    const CENTER_Y = canvas.height / 2;
    const pixelR = r * UNIT_PIXELS;
    const pixelRhalf = (r / 2) * UNIT_PIXELS;
    ctx.fillStyle = 'rgba(0, 0, 255, 0.5)';
    ctx.beginPath();
    ctx.arc(CENTER_X, CENTER_Y, pixelRhalf, -Math.PI, -Math.PI * 1.5, true);
    ctx.lineTo(CENTER_X, CENTER_Y);
    ctx.fill();
    ctx.fillRect(CENTER_X - pixelR, CENTER_Y - pixelR, pixelR, pixelR);
    ctx.beginPath();
    ctx.moveTo(CENTER_X, CENTER_Y);
    ctx.lineTo(CENTER_X + pixelRhalf, CENTER_Y);
    ctx.lineTo(CENTER_X, CENTER_Y + pixelR);
    ctx.closePath();
    ctx.fill();
}
function drawPoints(canvas, ctx, r, points) {
    if (isNaN(r) || r <= 0) return;
    const UNIT_PIXELS = canvas.width / 12;
    const CENTER_X = canvas.width / 2;
    const CENTER_Y = canvas.height / 2;
    points.forEach(p => {
        if (p.r === r) {
            const px = CENTER_X + p.x * UNIT_PIXELS;
            const py = CENTER_Y - p.y * UNIT_PIXELS;
            ctx.beginPath();
            ctx.arc(px, py, 4, 0, 2 * Math.PI);
            ctx.fillStyle = p.inside ? 'green' : 'red';
            ctx.fill();
        }
    });
}
