const { Button, TextInput, Table } = belle;
const { useSelector, useDispatch } = ReactRedux;

const MainPage = () => {
    const dispatch = useDispatch();
    const { user, x, y, r, results } = useSelector(state => state);
    const xValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
    const rValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];

    const handleYChange = (value) => {
        if (/^-?\d*\.?\d*$/.test(value) && (value === '' || (parseFloat(value) >= -5 && parseFloat(value) <= 5))) {
            dispatch({ type: 'SET_Y', payload: value });
        }
    };

    const handlePointCheck = async (xCoord, yCoord) => {
        if (x !== null && y !== '' && r !== null && r > 0) {
            try {
                const response = await fetch('/api/points', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        userId: user.id,
                        x: xCoord || x,
                        y: yCoord || parseFloat(y),
                        r
                    })
                });
                if (response.ok) {
                    const result = await response.json();
                    dispatch({ type: 'ADD_RESULT', payload: result });
                }
            } catch (err) {
                console.error('Error checking point:', err);
            }
        }
    };

    const handleLogout = () => {
        dispatch({ type: 'LOGOUT' });
    };

    React.useEffect(() => {
        const fetchResults = async () => {
            try {
                const response = await fetch(`/api/points/${user.id}`);
                if (response.ok) {
                    const data = await response.json();
                    dispatch({ type: 'SET_RESULTS', payload: data });
                }
            } catch (err) {
                console.error('Error fetching results:', err);
            }
        };
        fetchResults();
    }, [user.id]);

    return (
        <div className="main-page">
            <Header />
            <div className="input-section">
                <div>
                    <label>X:</label>
                    <div className="button-group">
                        {xValues.map(val => (
                            <Button
                                key={val}
                                primary={x === val}
                                onClick={() => dispatch({ type: 'SET_X', payload: val })}
                            >
                                {val}
                            </Button>
                        ))}
                    </div>
                </div>
                <div>
                    <label>Y:</label>
                    <TextInput
                        value={y}
                        onChange={e => handleYChange(e.value)}
                        placeholder="-5 ... 5"
                    />
                </div>
                <div>
                    <label>R:</label>
                    <div className="button-group">
                        {rValues.map(val => (
                            <Button
                                key={val}
                                primary={r === val}
                                onClick={() => dispatch({ type: 'SET_R', payload: val })}
                                disabled={val <= 0}
                            >
                                {val}
                            </Button>
                        ))}
                    </div>
                </div>
                <Button primary onClick={() => handlePointCheck()}>Проверить</Button>
            </div>
            <Graph r={r} results={results} onPointClick={handlePointCheck} />
            <Table className="results-table">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Попадание</th>
                    <th>Время</th>
                </tr>
                </thead>
                <tbody>
                {results.map((result, idx) => (
                    <tr key={idx}>
                        <td>{result.x}</td>
                        <td>{result.y}</td>
                        <td>{result.r}</td>
                        <td>{result.isInside ? 'Да' : 'Нет'}</td>
                        <td>{result.timestamp}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <Button primary onClick={handleLogout}>Выйти</Button>
        </div>
    );
};