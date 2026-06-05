import { Link, useNavigate } from 'react-router-dom'
import { useCart } from '../context/CartContext'

export default function Cart() {
  const { items, removeItem, changeQty, total, clearCart } = useCart()
  const navigate = useNavigate()

  if (items.length === 0) return (
    <div className="page">
      <div className="container">
        <p className="empty">Кошик порожній. <Link to="/">Перейти до каталогу</Link></p>
      </div>
    </div>
  )

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1>Кошик</h1>
          <button className="btn btn-outline btn-sm" onClick={clearCart}>Очистити</button>
        </div>
        <div className="card" style={{ marginBottom:'1.5rem' }}>
          <table className="cart-table">
            <thead>
              <tr>
                <th>Товар</th>
                <th>Ціна</th>
                <th>Кількість</th>
                <th>Сума</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map(item => (
                <tr key={item.id}>
                  <td>
                    <Link to={`/product/${item.id}`}>{item.name}</Link>
                    <div className="text-muted">{item.category}</div>
                  </td>
                  <td>{item.price.toLocaleString('uk-UA')} грн</td>
                  <td>
                    <div className="qty-control">
                      <button className="qty-btn" onClick={() => changeQty(item.id, item.qty - 1)}>-</button>
                      <span>{item.qty}</span>
                      <button className="qty-btn" onClick={() => changeQty(item.id, item.qty + 1)}>+</button>
                    </div>
                  </td>
                  <td>{(item.price * item.qty).toLocaleString('uk-UA')} грн</td>
                  <td>
                    <button className="btn btn-danger btn-sm" onClick={() => removeItem(item.id)}>✕</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div style={{ display:'flex', justifyContent:'flex-end', alignItems:'center', gap:'1.5rem' }}>
          <span style={{ fontSize:'1.2rem', fontWeight:700 }}>
            Разом: {total.toLocaleString('uk-UA')} грн
          </span>
          <button className="btn btn-primary" onClick={() => navigate('/checkout')}>
            Оформити замовлення
          </button>
        </div>
      </div>
    </div>
  )
}
