const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');

const FILE = path.join(__dirname, '../data/products.json');
const read = () => JSON.parse(fs.readFileSync(FILE, 'utf8'));

router.get('/categories', (req, res) => {
  const cats = [...new Set(read().map(p => p.category))];
  res.json(cats);
});

router.get('/', (req, res) => {
  let products = read();
  const { category, search, sort } = req.query;
  if (category) products = products.filter(p => p.category === category);
  if (search)   products = products.filter(p => p.name.toLowerCase().includes(search.toLowerCase()));
  if (sort === 'price_asc')  products.sort((a, b) => a.price - b.price);
  if (sort === 'price_desc') products.sort((a, b) => b.price - a.price);
  if (sort === 'rating')     products.sort((a, b) => b.rating - a.rating);
  res.json(products);
});

router.get('/:id', (req, res) => {
  const product = read().find(p => p.id === req.params.id);
  if (!product) return res.status(404).json({ error: 'Товар не знайдено' });
  res.json(product);
});

module.exports = router;
