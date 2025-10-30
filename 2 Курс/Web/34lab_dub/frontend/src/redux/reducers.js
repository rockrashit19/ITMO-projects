import {combineReducers} from 'redux';
function auth(state={user:null, error:null}, action) {
    switch(action.type){
        case 'SET_USER': return {...state, user:action.user};
        case 'SET_ERROR': return {...state, error:action.err};
        default: return state;
    }
}
function history(state=[], action) {
    switch(action.type){
        case 'ADD_RESULT': return [action.res, ...state];
        case 'SET_HISTORY': return action.hist;
        default: return state;
    }
}
export default combineReducers({auth, history});