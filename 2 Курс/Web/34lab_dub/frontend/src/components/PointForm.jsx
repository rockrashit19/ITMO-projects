import React, {useEffect, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { addResult, setHistory } from '../redux/actions';
import { checkPoint, getHistory } from '../api/api';
import PlotArea from './PlotArea';
import ResultsTable from './ResultsTable';
export default function PointForm(){
    const user = useSelector(s=>s.auth.user);
    const dispatch = useDispatch();
    const [x, setX] = useState(0), [y, setY] = useState(0), [r, setR] = useState(1);
    useEffect(()=>{ getHistory(user.id).then(hist=>dispatch(setHistory(hist))); },[]);
    const submit = async ()=>{
        const res = await checkPoint(x,y,r,user.id);
        dispatch(addResult(res));
    };
    return (
        <div>
            <h2>Привет, {user.login}</h2>
            <div>
                <span>X:</span>{[-4,-3,-2,-1,0,1,2,3,4].map(v=><button key={v} onClick={()=>setX(v)}>{v}</button>)}
            </div>
            <div>
                <span>Y:</span>
                <input type="number" step="0.1" min="-5" max="5" value={y} onChange={e=>setY(+e.target.value)}/>
            </div>
            <div>
                <span>R:</span>{[-4,-3,-2,-1,0,1,2,3,4].map(v=><button key={v} onClick={()=>setR(v)}>{v}</button>)}
            </div>
            <button onClick={submit}>Проверить</button>
            <PlotArea x={x} y={y} r={r} onClick={submit}/>
            <ResultsTable/>
        </div>
    );
}