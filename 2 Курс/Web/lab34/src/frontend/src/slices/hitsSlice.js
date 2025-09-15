import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { api } from '../api';

export const fetchHits = createAsyncThunk('hits/fetch', async () => api('/hits'));
export const addHit = createAsyncThunk('hits/add', async ({ x, y, r }) =>
    api('/hits', { method: 'POST', body: JSON.stringify({ x, y, r }) })
);

const slice = createSlice({
    name: 'hits',
    initialState: { items: [], status: 'idle', error: null },
    reducers: { clearLocal: (st) => { st.items = []; } },
    extraReducers: (b) => {
        b.addCase(fetchHits.fulfilled, (st, { payload }) => { st.items = payload; st.error = null; });
        b.addCase(addHit.fulfilled, (st, { payload }) => { st.items.unshift(payload); });
    }
});

export const { clearLocal } = slice.actions;
export default slice.reducer;
