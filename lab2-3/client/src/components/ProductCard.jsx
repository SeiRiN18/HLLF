import { Link } from 'react-router-dom'
import { useCart } from '../context/CartContext'

export default function ProductCard({ product }) {
  const { addItem } = useCart()

  return (
    <div className="card product-card">
      <img
        className="product-img"
        src={product.image || 'https://placehold.co/400x300?text=No+Image'}
        alt={product.name}
      />
      <div className="card-body" style={{ display:'flex', flexDirection:'column', flex:1, gap:'0.25rem' }}>
        <div className="product-category">{product.category}</div>
        <Link to={`/product/${product.id}`} className="product-name">{product.name}</Link>
        <div className="product-rating">{'★'.repeat(Math.round(product.rating))}{'☆'.repeat(5 - Math.round(product.rating))} {product.rating.toFixed(1)}</div>
        <div className="product-price">{product.price.toLocaleString('uk-UA')} грн</div>
        <button
          className="btn btn-primary btn-sm"
          style={{ marginTop:'auto' }}
          onClick={() => addItem(product)}
        >
          До кошика
        </button>
      </div>
    </div>
  )
}
