import React, { useEffect, useState } from 'react';
import { Button, TextInput } from 'belle';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../slices/authSlice';
import { useNavigate, useLocation } from 'react-router-dom';
import { selectAuth } from '../store';

function Clock13() {
    const [now, setNow] = useState(new Date());
    useEffect(()=>{
        const t = setInterval(()=>setNow(new Date()), 13000);
        return ()=>clearInterval(t);
    },[]);
    return <div>{now.toLocaleString()}</div>;
}

export default function LoginPage() {
    const [username, setU] = useState('student');
    const [password, setP] = useState('123456');
    const { user, error } = useSelector(selectAuth);
    const dispatch = useDispatch();
    const nav = useNavigate();
    const loc = useLocation();

    useEffect(()=>{
        if (user) nav('/app', { replace:true, state: loc.state });
    }, [user]);

    return (
        <div className="login">
            <h2>Вход на платформу</h2>
            <Clock13/>
            <div className="form">
                <label>Логин</label>
                <TextInput value={username} onChange={(e)=>setU(e.target.value)} />
                <label>Пароль</label>
                <TextInput type="password" value={password} onChange={(e)=>setP(e.target.value)} />
                {error ? <div className="error">{error}</div> : null}
                <Button onClick={()=>dispatch(login({ username, password }))}>Войти</Button>
            </div>
        </div>
    );
}
