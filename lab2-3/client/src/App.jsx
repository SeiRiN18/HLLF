import React, { Suspense, lazy } from 'react'
import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'

const Home          = lazy(() => import('./pages/Home'))
const ProductDetail = lazy(() => import('./pages/ProductDetail'))
const Cart          = lazy(() => import('./pages/Cart'))
const Checkout      = lazy(() => import('./pages/Checkout'))
const Orders        = lazy(() => import('./pages/Orders'))
const Login         = lazy(() => import('./pages/Login'))
const Register      = lazy(() => import('./pages/Register'))

export default function App() {
  return (
    <>
      <Navbar />
      <Suspense fallback={<p className="loading">Завантаження...</p>}>
        <Routes>
          <Route path="/"            element={<Home />} />
          <Route path="/product/:id" element={<ProductDetail />} />
          <Route path="/cart"        element={<Cart />} />
          <Route path="/checkout"    element={<Checkout />} />
          <Route path="/orders"      element={<Orders />} />
          <Route path="/login"       element={<Login />} />
          <Route path="/register"    element={<Register />} />
        </Routes>
      </Suspense>
    </>
  )
}
