import React from 'react';
import { useSelector } from 'react-redux';
export default function ResultsTable(){
    const hist = useSelector(s=>s.history);
    return (
        <table>
            <thead>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Hit</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody>
            {hist.map(r=>(<tr key={r.id}>
                <td>{r.x}</td>
                <td>{r.y}</td>
                <td>{r.r}</td>
                <td>{r.hit?'Да':'Нет'}</td>
                <td>{r.timestamp}</td>
            </tr>))}
            </tbody>
        </table>
    );
}