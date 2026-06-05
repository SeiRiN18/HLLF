import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { createOrder } from '../api'
import { useCart } from '../context/CartContext'
import { useAuth } from '../context/AuthContext'

export default function Checkout() {
  const { items, total, clearCart } = useCart()
  const { user } = useAuth()
  const navigate = useNavigate()

  const [form, setForm] = useState({ address: '', phone: '', payment: 'card' })
  const [error, setError]   = useState('')
  const [loading, setLoading] = useState(false)

  if (!user) return (
    <div className="page">
      <div className="container">
        <p className="empty">
          Для оформлення замовлення потрібно <Link to="/login">увійти</Link>.
        </p>
      </div>
    </div>
  )

  if (items.length === 0) return (
    <div className="page">
      <div className="container">
        <p className="empty">Кошик порожній. <Link to="/">До каталогу</Link></p>
      </div>
    </div>
  )

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const handleSubmit = async e => {
    e.preventDefault()
    if (!form.address.trim() || !form.phone.trim()) { setError('Заповніть всі поля'); return }
    setLoading(true)
    try {
      await createOrder({
        items: items.map(i => ({ productId: i.id, name: i.name, price: i.price, qty: i.qty })),
        total,
        address: form.address,
        phone: form.phone,
        payment: form.payment
      })
      clearCart()
      navigate('/orders')
    } catch (err) {
      setError(err.response?.data?.error || 'Помилка оформлення')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <div className="container">
        <h1 style={{ marginBottom:'1.5rem' }}>Оформлення замовлення</h1>
        <div style={{ display:'grid', gridTemplateColumns:'1fr 360px', gap:'1.5rem', alignItems:'start' }}>
          <form onSubmit={handleSubmit} className="card card-body">
            {error && <div className="alert alert-error">{error}</div>}
            <div className="form-group">
              <label className="form-label">Адреса доставки</label>
              <input className="form-control" name="address" value={form.address} onChange={handleChange} placeholder="м. Харків, вул. Науки 14" required />
            </div>
            <div className="form-group">
              <label className="form-label">Телефон</label>
              <input className="form-control" name="phone" value={form.phone} onChange={handleChange} placeholder="+380501234567" required />
            </div>
            <div className="form-group">
              <label className="form-label">Оплата</label>
              <select className="form-control filter-select" name="payment" value={form.payment} onChange={handleChange}>
                <option value="card">Карткою онлайн</option>
                <option value="cash">Готівкою при отриманні</option>
                <option value="transfer">Банківський переказ</option>
              </select>
            </div>
            <button className="btn btn-primary btn-block" disabled={loading}>
              {loading ? 'Оформлення...' : 'Підтвердити замовлення'}
            </button>
          </form>

          <div className="card card-body">
            <div className="section-title">Ваше замовлення</div>
            {items.map(i => (
              <div key={i.id} style={{ display:'flex', justifyContent:'space-between', marginBottom:'0.5rem', fontSize:'0.9rem' }}>
                <span>{i.name} × {i.qty}</span>
                <span>{(i.price * i.qty).toLocaleString('uk-UA')} грн</span>
              </div>
            ))}
            <hr style={{ margin:'0.75rem 0', border:'none', borderTop:'1px solid var(--border)' }} />
            <div style={{ display:'flex', justifyContent:'space-between', fontWeight:700 }}>
              <span>Разом</span>
              <span>{total.toLocaleString('uk-UA')} грн</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
