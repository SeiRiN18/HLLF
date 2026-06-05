import { useEffect, useState } from 'react'
import { getProducts, getCategories } from '../api'
import ProductCard from '../components/ProductCard'

export default function Home() {
  const [products, setProducts]     = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading]       = useState(true)
  const [search, setSearch]         = useState('')
  const [category, setCategory]     = useState('')
  const [sort, setSort]             = useState('')

  useEffect(() => {
    getCategories().then(r => setCategories(r.data)).catch(() => {})
  }, [])

  useEffect(() => {
    setLoading(true)
    const params = {}
    if (search)   params.search   = search
    if (category) params.category = category
    if (sort)     params.sort     = sort
    getProducts(params)
      .then(r => setProducts(r.data))
      .catch(() => setProducts([]))
      .finally(() => setLoading(false))
  }, [search, category, sort])

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1>Каталог товарів</h1>
        </div>
        <div className="filters">
          <input
            className="search-input"
            placeholder="Пошук товарів..."
            value={search}
            onChange={e => setSearch(e.target.value)}
          />
          <select className="filter-select" value={category} onChange={e => setCategory(e.target.value)}>
            <option value="">Всі категорії</option>
            {categories.map(c => <option key={c} value={c}>{c}</option>)}
          </select>
          <select className="filter-select" value={sort} onChange={e => setSort(e.target.value)}>
            <option value="">Сортування</option>
            <option value="price_asc">Ціна: зростання</option>
            <option value="price_desc">Ціна: спадання</option>
            <option value="rating">Рейтинг</option>
          </select>
        </div>
        {loading
          ? <p className="loading">Завантаження...</p>
          : products.length === 0
            ? <p className="empty">Товарів не знайдено</p>
            : <div className="products-grid">{products.map(p => <ProductCard key={p.id} product={p} />)}</div>
        }
      </div>
    </div>
  )
}
