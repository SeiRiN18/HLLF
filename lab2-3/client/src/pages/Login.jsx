import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login } from '../api'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const { signIn } = useAuth()
  const navigate = useNavigate()

  const [form, setForm]     = useState({ username: '', password: '' })
  const [error, setError]   = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const { data } = await login(form)
      signIn(data.user, data.token)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.error || 'Невірний логін або пароль')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <div className="container">
        <form className="form-card" onSubmit={handleSubmit}>
          <h2>Вхід</h2>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-group">
            <label className="form-label">Логін</label>
            <input className="form-control" name="username" value={form.username} onChange={handleChange} autoComplete="username" required />
          </div>
          <div className="form-group">
            <label className="form-label">Пароль</label>
            <input className="form-control" type="password" name="password" value={form.password} onChange={handleChange} autoComplete="current-password" required />
          </div>
          <button className="btn btn-primary btn-block" disabled={loading}>
            {loading ? 'Вхід...' : 'Увійти'}
          </button>
          <p style={{ textAlign:'center', marginTop:'1rem', fontSize:'0.9rem' }}>
            Немає акаунту? <Link to="/register">Зареєструватися</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
