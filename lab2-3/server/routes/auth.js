const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

const JWT_SECRET = 'lab2-shop-secret';
const FILE = path.join(__dirname, '../data/users.json');

const readUsers = () => JSON.parse(fs.readFileSync(FILE, 'utf8'));
const writeUsers = (d) => fs.writeFileSync(FILE, JSON.stringify(d, null, 2));

router.post('/register', (req, res) => {
  const { username, email, password } = req.body;
  if (!username || !email || !password)
    return res.status(400).json({ error: 'Заповніть всі поля' });

  const users = readUsers();
  if (users.find(u => u.email === email))
    return res.status(400).json({ error: 'Email вже використовується' });

  const user = {
    id: uuidv4(),
    username,
    email,
    password: bcrypt.hashSync(password, 10),
    role: 'user',
    createdAt: new Date().toISOString(),
  };
  users.push(user);
  writeUsers(users);

  const token = jwt.sign({ id: user.id, email, role: user.role }, JWT_SECRET, { expiresIn: '7d' });
  res.json({ token, user: { id: user.id, username, email, role: user.role } });
});

router.post('/login', (req, res) => {
  const { email, password } = req.body;
  const users = readUsers();
  const user = users.find(u => u.email === email);
  if (!user || !bcrypt.compareSync(password, user.password))
    return res.status(401).json({ error: 'Невірний email або пароль' });

  const token = jwt.sign({ id: user.id, email, role: user.role }, JWT_SECRET, { expiresIn: '7d' });
  res.json({ token, user: { id: user.id, username: user.username, email, role: user.role } });
});

module.exports = router;
