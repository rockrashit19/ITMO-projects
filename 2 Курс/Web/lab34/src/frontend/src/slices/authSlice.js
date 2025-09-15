import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { api } from '../api';

export const login = createAsyncThunk('auth/login', async ({ username, password }) =>
    api('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) })
);
export const logout = createAsyncThunk('auth/logout', async () =>
    api('/auth/logout', { method: 'POST' })
);
export const checkMe = createAsyncThunk('auth/me', async () => api('/auth/me'));

const slice = createSlice({
    name: 'auth',
    initialState: { user: null, status: 'idle', error: null },
    reducers: {},
    extraReducers: (b) => {
        b.addCase(login.fulfilled, (st, { payload }) => { st.user = payload; st.error = null; });
        b.addCase(login.rejected, (st) => { st.error = 'Неверный логин или пароль'; });
        b.addCase(checkMe.fulfilled, (st, { payload }) => { st.user = payload; });
        b.addCase(logout.fulfilled, (st) => { st.user = null; });
    }
});

export default slice.reducer;
