import React, { useEffect, useRef } from 'react'

const SIZE = 380
const PAD  = 30

export default function GraphCanvas({ r, points = [], onSelectPoint }) {
  const ref = useRef(null)

  const toPx = (x, y) => {
    const scale = (SIZE - 2*PAD) / (2 * r)
    const cx = SIZE/2, cy = SIZE/2
    return { px: cx + x*scale, py: cy - y*scale }
  }
  const toCoord = (px, py) => {
    const scale = (SIZE - 2*PAD) / (2 * r)
    const cx = SIZE/2, cy = SIZE/2
    return { x: (px - cx)/scale, y: (cy - py)/scale }
  }

  useEffect(() => {
    const c = ref.current
    const ctx = c.getContext('2d')
    ctx.clearRect(0,0,SIZE,SIZE)

    ctx.fillStyle = '#fff'
    ctx.fillRect(0,0,SIZE,SIZE)

    ctx.strokeStyle = '#333'
    ctx.lineWidth = 1
    ctx.beginPath()
    ctx.moveTo(PAD, SIZE/2); ctx.lineTo(SIZE-PAD, SIZE/2)
    ctx.moveTo(SIZE/2, PAD); ctx.lineTo(SIZE/2, SIZE-PAD)
    ctx.stroke()

    ctx.beginPath()
    ctx.moveTo(SIZE-PAD, SIZE/2); ctx.lineTo(SIZE-PAD-8, SIZE/2-5); ctx.lineTo(SIZE-PAD-8, SIZE/2+5); ctx.closePath(); ctx.fill()
    ctx.beginPath()
    ctx.moveTo(SIZE/2, PAD); ctx.lineTo(SIZE/2-5, PAD+8); ctx.lineTo(SIZE/2+5, PAD+8); ctx.closePath(); ctx.fill()

    const ticks = [-r, -r/2, r/2, r]
    ctx.fillStyle = '#333'
    ctx.font = '12px system-ui'
    ticks.forEach(t => {
      const { px, py } = toPx(t, 0)
      ctx.beginPath(); ctx.moveTo(px, SIZE/2-4); ctx.lineTo(px, SIZE/2+4); ctx.stroke()
      ctx.fillText(String(Math.round(t*100)/100), px-8, SIZE/2+18)

      const p2 = toPx(0, t)
      ctx.beginPath(); ctx.moveTo(SIZE/2-4, p2.py); ctx.lineTo(SIZE/2+4, p2.py); ctx.stroke()
      ctx.fillText(String(Math.round(t*100)/100), SIZE/2+8, p2.py+4)
    })

    ctx.fillStyle = 'rgba(30,144,255,0.35)'
    let p1 = toPx(0,0), p2 = toPx(-r/2, 0), p3 = toPx(-r/2, r), p4 = toPx(0, r)
    ctx.beginPath(); ctx.moveTo(p1.px, p1.py); ctx.lineTo(p2.px, p2.py); ctx.lineTo(p3.px, p3.py); ctx.lineTo(p4.px, p4.py); ctx.closePath(); ctx.fill()

    const center = toPx(0,0)
    ctx.beginPath()
    ctx.moveTo(center.px, center.py)
    ctx.arc(center.px, center.py, ((SIZE - 2*PAD) / (2 * r)) * (r/2), Math.PI*1.5, 0, false)
    ctx.closePath(); ctx.fill()

    const a = toPx(0,0), b = toPx(-r,0), d = toPx(0,-r)
    ctx.beginPath(); ctx.moveTo(a.px, a.py); ctx.lineTo(b.px, b.py); ctx.lineTo(d.px, d.py); ctx.closePath(); ctx.fill()

    ctx.strokeStyle = '#eee'
    ctx.lineWidth = 1
    const step = r/2
    for (let gx = -r; gx <= r; gx += step) {
      const p = toPx(gx, 0).px
      ctx.beginPath(); ctx.moveTo(p, PAD); ctx.lineTo(p, SIZE-PAD); ctx.stroke()
    }
    for (let gy = -r; gy <= r; gy += step) {
      const p = toPx(0, gy).py
      ctx.beginPath(); ctx.moveTo(PAD, p); ctx.lineTo(SIZE-PAD, p); ctx.stroke()
    }

    for (const pt of points) {
      const { px, py } = toPx(pt.x, pt.y)
      ctx.beginPath()
      ctx.arc(px, py, 3.5, 0, Math.PI*2)
      ctx.fillStyle = pt.hit ? '#128a12' : '#d11'
      ctx.fill()
    }
  }, [r, points])

  const handleClick = (e) => {
    const rect = ref.current.getBoundingClientRect()
    const x = e.clientX - rect.left
    const y = e.clientY - rect.top
    const coord = toCoord(x, y)
    onSelectPoint && onSelectPoint(coord)
  }

  return (
      <div>
        <canvas
            ref={ref}
            width={SIZE}
            height={SIZE}
            onClick={handleClick}
            className="canvas"
            aria-label="График области"
        />
        <div className="axis-labels">
          <span className="x">x</span>
          <span className="y">y</span>
        </div>
      </div>
  )
}
