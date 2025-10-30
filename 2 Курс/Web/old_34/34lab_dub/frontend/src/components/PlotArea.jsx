import React, {useRef, useEffect} from 'react';
export default function PlotArea({x,y,r,onClick}){
    const ref=useRef();
    useEffect(()=>{
        const ctx=ref.current.getContext('2d');
        const size=200; ctx.clearRect(0,0,size,size);

        ctx.beginPath(); ctx.fillStyle='rgba(0,0,255,0.3)';
        ctx.moveTo(size/2,size/2);
        ctx.arc(size/2,size/2, r*(size/4), Math.PI/2, Math.PI, true);
        ctx.lineTo(size/2,size/2); ctx.fill();
        ctx.fillStyle= x*x+y*y<=r*r ? 'green':'red';
        ctx.fillRect(size/2+x*(size/4)-3, size/2-y*(size/4)-3,6,6);
    },[x,y,r]);
    return <canvas ref={ref} width={200} height={200} onClick={onClick} style={{border:'1px solid'}}/>
}