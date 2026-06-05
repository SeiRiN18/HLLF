const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');
const auth = require('../middleware/auth');

router.get('/', auth, (req, res) => {
  const orders   = JSON.parse(fs.readFileSync(path.join(__dirname, '../data/orders.json'), 'utf8'));
  const products = JSON.parse(fs.readFileSync(path.join(__dirname, '../data/products.json'), 'utf8'));

  const userOrders = orders.filter(o => o.userId === req.user.id);

  if (!userOrders.length) return res.json(products.slice(0, 4));

  const boughtCategories = new Set();
  const boughtIds = new Set();
  userOrders.forEach(o => o.items.forEach(i => {
    boughtCategories.add(i.category);
    boughtIds.add(i.productId);
  }));

  const recommended = products.filter(p => boughtCategories.has(p.category) && !boughtIds.has(p.id));
  const fill = products.filter(p => !boughtIds.has(p.id) && !recommended.find(r => r.id === p.id));
  recommended.push(...fill);

  res.json(recommended.slice(0, 4));
});

module.exports = router;
