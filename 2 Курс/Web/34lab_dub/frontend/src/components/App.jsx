import React from 'react';
import { useSelector } from 'react-redux';
import LoginForm from './LoginForm';
import PointForm from './PointForm';
export default function App() {
    const user = useSelector(s=>s.auth.user);
    return user ? <PointForm/> : <LoginForm/>;
}