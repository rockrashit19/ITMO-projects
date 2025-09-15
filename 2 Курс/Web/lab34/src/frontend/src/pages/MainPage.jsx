import React, { useEffect } from 'react';
import { Button, TextInput } from 'belle';
import { useDispatch, useSelector } from 'react-redux';
import { selectControls, selectHits } from '../store';
import { setX, setY, setR } from '../slices/controlsSlice';
import { addHit, fetchHits } from '../slices/hitsSlice';

const X_VALUES = [-4,-3,-2,-1,0,1,2,3,4];
const R_VALUES = [1,2,3,4,5];

export default function MainPage() {
    const dispatch = useDispatch();
    const { x, y, r } = useSelector(selectControls);
    const { items } = useSelector(selectHits);

    useEffect(()=>{ dispatch(fetchHits()); }, []);

    const submit = () => {
        if (isNaN(y) || y < -5 || y > 5) return alert('Y должен быть числом в [-5;5]');
        dispatch(addHit({ x, y: Number(y), r }));
    };

    return (
        <div className="main-grid">
            <section className="panel">
                <h3>Координаты</h3>
                <div> X: {X_VALUES.map(v =>
                    <Button key={v} style={{margin: 4}} primary={v===x} onClick={()=>dispatch(setX(v))}>{v}</Button>
                )}</div>
                <div style={{marginTop:10}}>
                    Y: <TextInput style={{width:120}} value={y} onChange={e=>dispatch(setY(e.target.value))} placeholder="-5 ... 5" />
                </div>
                <div style={{marginTop:10}}>
                    R: {R_VALUES.map(v =>
                    <Button key={v} style={{margin: 4}} primary={v===r} onClick={()=>dispatch(setR(v))}>{v}</Button>
                )}
                </div>
                <Button style={{marginTop:12}} onClick={submit}>Проверить</Button>
            </section>

            <section className="graph">
                {/* Здесь позже будет Canvas с областью и кликом */}
                <div className="graph-placeholder">График появится здесь</div>
            </section>

            <section className="table">
                <h3>История</h3>
                <table className="results">
                    <thead><tr><th>Время</th><th>X</th><th>Y</th><th>R</th><th>Попадание</th></tr></thead>
                    <tbody>
                    {items.map(r => (
                        <tr key={r.id}>
                            <td>{new Date(r.createdAt).toLocaleString()}</td>
                            <td>{r.x}</td><td>{r.y}</td><td>{r.r}</td>
                            <td style={{color: r.hit ? 'green' : 'red'}}>{r.hit ? 'да' : 'нет'}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </section>
        </div>
    );
}
