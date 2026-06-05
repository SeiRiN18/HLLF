import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useCart } from '../context/CartContext'

export default function Navbar() {
  const { user, signOut } = useAuth()
  const { count } = useCart()
  const navigate = useNavigate()

  const handleLogout = () => { signOut(); navigate('/') }

  return (
    <nav className="navbar">
      <div className="container">
        <Link to="/" className="navbar-brand">ShopReact</Link>
        <div className="navbar-nav">
          <Link to="/" className="nav-link">Каталог</Link>
          <Link to="/cart" className="nav-link">
            Кошик{count > 0 && <span className="badge">{count}</span>}
          </Link>
          {user ? (
            <>
              <Link to="/orders" className="nav-link">Замовлення</Link>
              <span className="nav-link" style={{ opacity: 0.5 }}>{user.username}</span>
              <button className="btn btn-outline btn-sm" onClick={handleLogout}>Вийти</button>
            </>
          ) : (
            <>
              <Link to="/login"    className="btn btn-outline btn-sm">Увійти</Link>
              <Link to="/register" className="btn btn-primary btn-sm">Реєстрація</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}
