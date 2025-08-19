import React, {useState} from 'react';
import { useDispatch } from 'react-redux';
import { setUser, setError } from '../redux/actions';
import { login } from '../api/api';
export default function LoginForm() {
    const [loginVal, setLogin] = useState('');
    const [pass, setPass] = useState('');
    const dispatch = useDispatch();
    const submit = async e => {
        e.preventDefault();
        try {
            const user = await login({login:loginVal, passwordHash:pass});
            dispatch(setUser(user));
        } catch(e) {
            dispatch(setError('Неверные данные'));
        }
    };
    return (
        <form onSubmit={submit}>
            <h2>Авторизация</h2>
            <input value={loginVal} onChange={e=>setLogin(e.target.value)} placeholder="Login"/>
            <input type="password" value={pass} onChange={e=>setPass(e.target.value)} placeholder="Password"/>
            <button type="submit">Войти</button>
        </form>
    );
}