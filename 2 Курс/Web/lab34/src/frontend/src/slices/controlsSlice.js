import { createSlice } from '@reduxjs/toolkit';

const initial = { x: 0, y: 0, r: 1, errors: {} };

const slice = createSlice({
    name: 'controls',
    initialState: initial,
    reducers: {
        setX: (st, {payload}) => { st.x = payload; },
        setY: (st, {payload}) => { st.y = payload; },
        setR: (st, {payload}) => { st.r = payload; },
        setError: (st, {payload}) => { st.errors = { ...st.errors, ...payload }; },
    }
});

export const { setX, setY, setR, setError } = slice.actions;
export default slice.reducer;
