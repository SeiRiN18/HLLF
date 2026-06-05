import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { register } from '../api'
import { useAuth } from '../context/AuthContext'

export default function Register() {
  const { signIn } = useAuth()
  const navigate = useNavigate()

  const [form, setForm]     = useState({ username: '', email: '', password: '', confirm: '' })
  const [error, setError]   = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    if (form.password !== form.confirm) { setError('Паролі не збігаються'); return }
    if (form.password.length < 6) { setError('Пароль мінімум 6 символів'); return }
    setLoading(true)
    try {
      const { data } = await register({ username: form.username, email: form.email, password: form.password })
      signIn(data.user, data.token)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.error || 'Помилка реєстрації')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <div className="container">
        <form className="form-card" onSubmit={handleSubmit}>
          <h2>Реєстрація</h2>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-group">
            <label className="form-label">Логін</label>
            <input className="form-control" name="username" value={form.username} onChange={handleChange} autoComplete="username" required />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-control" type="email" name="email" value={form.email} onChange={handleChange} autoComplete="email" required />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Пароль</label>
              <input className="form-control" type="password" name="password" value={form.password} onChange={handleChange} autoComplete="new-password" required />
            </div>
            <div className="form-group">
              <label className="form-label">Повторити пароль</label>
              <input className="form-control" type="password" name="confirm" value={form.confirm} onChange={handleChange} autoComplete="new-password" required />
            </div>
          </div>
          <button className="btn btn-primary btn-block" disabled={loading}>
            {loading ? 'Реєстрація...' : 'Зареєструватися'}
          </button>
          <p style={{ textAlign:'center', marginTop:'1rem', fontSize:'0.9rem' }}>
            Вже є акаунт? <Link to="/login">Увійти</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
