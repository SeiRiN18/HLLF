import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getOrders, getRecommendations } from '../api'
import { useAuth } from '../context/AuthContext'
import ProductCard from '../components/ProductCard'

const STATUS_LABELS = {
  pending:   'Очікує',
  confirmed: 'Підтверджено',
  shipped:   'Відправлено',
  delivered: 'Доставлено',
  cancelled: 'Скасовано'
}

export default function Orders() {
  const { user } = useAuth()
  const [orders, setOrders]         = useState([])
  const [recs, setRecs]             = useState([])
  const [loading, setLoading]       = useState(true)

  useEffect(() => {
    if (!user) return
    Promise.all([getOrders(), getRecommendations()])
      .then(([oRes, rRes]) => { setOrders(oRes.data); setRecs(rRes.data) })
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [user])

  if (!user) return (
    <div className="page">
      <div className="container">
        <p className="empty">Увійдіть, щоб переглянути замовлення. <Link to="/login">Увійти</Link></p>
      </div>
    </div>
  )

  if (loading) return <p className="loading">Завантаження...</p>

  return (
    <div className="page">
      <div className="container">
        <div className="page-header"><h1>Мої замовлення</h1></div>

        {orders.length === 0
          ? <p className="empty">Замовлень поки немає. <Link to="/">До каталогу</Link></p>
          : (
            <div className="orders-list" style={{ marginBottom:'2.5rem' }}>
              {orders.map(order => (
                <div key={order.id} className="order-card">
                  <div className="order-header">
                    <div>
                      <strong>#{order.id.slice(0, 8)}</strong>
                      <span className="text-muted" style={{ marginLeft:'0.75rem' }}>
                        {new Date(order.createdAt).toLocaleDateString('uk-UA')}
                      </span>
                    </div>
                    <span className={`status-badge status-${order.status}`}>
                      {STATUS_LABELS[order.status] || order.status}
                    </span>
                  </div>
                  <div style={{ fontSize:'0.9rem', marginBottom:'0.5rem' }}>
                    {order.items.map(i => `${i.name} × ${i.qty}`).join(', ')}
                  </div>
                  <div style={{ display:'flex', justifyContent:'space-between', fontSize:'0.9rem' }}>
                    <span className="text-muted">{order.address}</span>
                    <strong>{order.total.toLocaleString('uk-UA')} грн</strong>
                  </div>
                </div>
              ))}
            </div>
          )
        }

        {recs.length > 0 && (
          <>
            <div className="section-title">Рекомендації для вас</div>
            <div className="products-grid">
              {recs.map(p => <ProductCard key={p.id} product={p} />)}
            </div>
          </>
        )}
      </div>
    </div>
  )
}
