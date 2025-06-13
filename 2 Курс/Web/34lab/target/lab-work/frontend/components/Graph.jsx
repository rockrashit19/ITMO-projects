const { useRef, useEffect } = React;

const Graph = ({ r, results, onPointClick }) => {
    const svgRef = useRef(null);

    const drawArea = (ctx, width, height) => {
        if (!r) return;
        ctx.clearRect(0, 0, width, height);
        ctx.beginPath();

        ctx.arc(width / 2, height / 2, r * width / 2, 0, 2 * Math.PI);
        ctx.fillStyle = 'rgba(0, 0, 255, 0.2)';
        ctx.fill();


        results.forEach(point => {
            ctx.beginPath();
            const px = width / 2 + (point.x * width / 2);
            const py = height / 2 - (point.y * height / 2);
            ctx.arc(px, py, 3, 0, 2 * Math.PI);
            ctx.fillStyle = point.isInside ? 'green' : 'red';
            ctx.fill();
        });
    };

    useEffect(() => {
        const canvas = svgRef.current;
        const ctx = canvas.getContext('2d');
        drawArea(ctx, canvas.width, canvas.height);
    }, [r, results]);

    const handleClick = (e) => {
        if (!r) return;
        const rect = svgRef.current.getBoundingClientRect();
        const scale = rect.width / 10;
        const x = ((e.clientX - rect.getX()) - rect.width / 2) * 2 / scale;
        const y = (rect.height / 2 - (e.clientY - rect.top)) * 2 / scale;
        onPointClick(x, y);
    };

    return (
        <canvas
            ref={svgRef}
            width={500}
            height={400}
            className="graph"
            onClick={handleClick}
        />
    );
};

export default Graph;