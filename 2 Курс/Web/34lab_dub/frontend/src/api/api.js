const BASE = 'http://localhost:8080/lab34-backend/api';
export async function login(creds) {
    const resp = await fetch(`${BASE}/auth/login`, {
        method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(creds)
    });
    if(!resp.ok) throw new Error('Unauthorized');
    return resp.json();
}
export async function checkPoint(x,y,r,userId) {
    const resp = await fetch(`${BASE}/points/check`,{
        method:'POST', headers:{'Content-Type':'application/json'},
        body:JSON.stringify({x,y,r,userId})
    }); return resp.json();
}
export async function getHistory(userId) {
    const resp = await fetch(`${BASE}/points/history/${userId}`);
    return resp.json();
}