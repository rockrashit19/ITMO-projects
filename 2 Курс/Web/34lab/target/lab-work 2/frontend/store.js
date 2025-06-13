const initialState = {
    user: null,
    results: [],
    x: null,
    y: '',
    r: null
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case 'LOGIN':
            return { ...state, user: action.payload };
        case 'LOGOUT':
            return { ...state, user: null, results: [], x: null, y: '', r: null };
        case 'SET_X':
            return { ...state, x: action.payload };
        case 'SET_Y':
            return { ...state, y: action.payload };
        case 'SET_R':
            return { ...state, r: action.payload };
        case 'ADD_RESULT':
            return { ...state, results: [...state.results, action.payload] };
        case 'SET_RESULTS':
            return { ...state, results: action.payload };
        default:
            return state;
    }
};

const store = Redux.createStore(reducer);