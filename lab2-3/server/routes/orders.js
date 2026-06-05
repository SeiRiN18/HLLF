const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');
const auth = require('../middleware/auth');

const FILE = path.join(__dirname, '../data/orders.json');
const read = () => JSON.parse(fs.readFileSync(FILE, 'utf8'));
const write = (d) => fs.writeFileSync(FILE, JSON.stringify(d, null, 2));

router.get('/', auth, (req, res) => {
  const orders = read();
  res.json(req.user.role === 'admin' ? orders : orders.filter(o => o.userId === req.user.id));
});

router.post('/', auth, (req, res) => {
  const { items, address, totalAmount } = req.body;
  const now = new Date().toISOString();
  const order = {
    id: uuidv4(),
    userId: req.user.id,
    items,
    address,
    totalAmount,
    status: 'pending',
    createdAt: now,
    updatedAt: now,
    statusHistory: [{ status: 'pending', time: now }],
  };
  const orders = read();
  orders.push(order);
  write(orders);
  res.json(order);
});

router.patch('/:id/status', auth, (req, res) => {
  const { status } = req.body;
  const orders = read();
  const order = orders.find(o => o.id === req.params.id);
  if (!order) return res.status(404).json({ error: 'Замовлення не знайдено' });
  if (order.userId !== req.user.id && req.user.role !== 'admin')
    return res.status(403).json({ error: 'Доступ заборонено' });
  const now = new Date().toISOString();
  order.status = status;
  order.updatedAt = now;
  order.statusHistory.push({ status, time: now });
  write(orders);
  res.json(order);
});

module.exports = router;
