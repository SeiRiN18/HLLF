import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getProduct } from '../api'
import { useCart } from '../context/CartContext'

export default function ProductDetail() {
  const { id } = useParams()
  const { addItem } = useCart()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const [added, setAdded]     = useState(false)

  useEffect(() => {
    getProduct(id)
      .then(r => setProduct(r.data))
      .catch(() => setProduct(null))
      .finally(() => setLoading(false))
  }, [id])

  const handleAdd = () => {
    addItem(product)
    setAdded(true)
    setTimeout(() => setAdded(false), 1500)
  }

  if (loading) return <p className="loading">Завантаження...</p>
  if (!product) return <p className="empty">Товар не знайдено. <Link to="/">Назад</Link></p>

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: 820 }}>
        <Link to="/" className="btn btn-outline btn-sm" style={{ marginBottom:'1.2rem' }}>← Назад</Link>
        <div className="card">
          <div className="card-body" style={{ display:'grid', gridTemplateColumns:'1fr 1fr', gap:'2rem' }}>
            <img
              src={product.image || 'https://placehold.co/500x400?text=No+Image'}
              alt={product.name}
              style={{ width:'100%', borderRadius:6, objectFit:'cover' }}
            />
            <div style={{ display:'flex', flexDirection:'column', gap:'0.6rem' }}>
              <div className="product-category">{product.category}</div>
              <h1 style={{ fontSize:'1.4rem' }}>{product.name}</h1>
              <div className="product-rating" style={{ fontSize:'1rem' }}>
                {'★'.repeat(Math.round(product.rating))}{'☆'.repeat(5 - Math.round(product.rating))} {product.rating.toFixed(1)}
              </div>
              <p style={{ color:'#475569', fontSize:'0.92rem', lineHeight:1.6 }}>{product.description}</p>
              <div className="product-price" style={{ fontSize:'1.6rem', margin:'0.5rem 0' }}>
                {product.price.toLocaleString('uk-UA')} грн
              </div>
              <div className="text-muted">В наявності: {product.stock} шт.</div>
              <button className="btn btn-primary" onClick={handleAdd} style={{ marginTop:'auto' }}>
                {added ? 'Додано!' : 'До кошика'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
