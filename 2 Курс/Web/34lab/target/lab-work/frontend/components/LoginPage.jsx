const { Button, TextInput } = belle;
const { useState } = React;
const { useDispatch } = ReactRedux;

const LoginPage = () => {
    const dispatch = useDispatch();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async () => {
        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            if (response.ok) {
                const user = await response.json();
                dispatch({ type: 'LOGIN', payload: user });
            } else {
                setError('Неверный логин или пароль');
            }
        } catch (err) {
            setError('Ошибка сервера');
        }
    };

    return (
        <div className="login-form">
            <Header />
            <TextInput placeholder="Логин" value={username} onChange={e => setUsername(e.value)} />
            <TextInput placeholder="Пароль" value={password} onChange={e => setPassword(e.value)} type="password" />
            <Button primary onClick={handleLogin}>Войти</Button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};