import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { removeToast } from '../slices/uiSlice'

export default function Toasts() {
    const toasts = useSelector(s => s.ui.toasts)
    const dispatch = useDispatch()

    useEffect(() => {
        const timers = toasts.map(t =>
            setTimeout(() => dispatch(removeToast(t.id)), 3000)
        )
        return () => timers.forEach(clearTimeout)
    }, [toasts.length])

    return (
        <div className="toasts">
            {toasts.map(t => (
                <div key={t.id} className={`toast ${t.kind}`}>{t.text}</div>
            ))}
        </div>
    )
}
