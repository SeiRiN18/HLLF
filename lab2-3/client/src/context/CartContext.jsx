import { createContext, useContext, useState } from 'react'

const CartContext = createContext(null)

const load = () => { try { return JSON.parse(localStorage.getItem('cart')) || [] } catch { return [] } }
const save = (items) => localStorage.setItem('cart', JSON.stringify(items))

export function CartProvider({ children }) {
  const [items, setItems] = useState(load)

  const update = (next) => { setItems(next); save(next) }

  const addItem = (product) => {
    setItems(prev => {
      const existing = prev.find(i => i.id === product.id)
      const next = existing
        ? prev.map(i => i.id === product.id ? { ...i, qty: i.qty + 1 } : i)
        : [...prev, { ...product, qty: 1 }]
      save(next)
      return next
    })
  }

  const removeItem = (id) => update(items.filter(i => i.id !== id))

  const changeQty = (id, qty) => {
    if (qty < 1) { removeItem(id); return }
    update(items.map(i => i.id === id ? { ...i, qty } : i))
  }

  const clearCart = () => update([])

  const total = items.reduce((s, i) => s + i.price * i.qty, 0)
  const count = items.reduce((s, i) => s + i.qty, 0)

  return (
    <CartContext.Provider value={{ items, addItem, removeItem, changeQty, clearCart, total, count }}>
      {children}
    </CartContext.Provider>
  )
}

export const useCart = () => useContext(CartContext)
