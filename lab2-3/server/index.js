const express = require('express');
const cors = require('cors');
const compression = require('compression');

const authRoutes = require('./routes/auth');
const productRoutes = require('./routes/products');
const orderRoutes = require('./routes/orders');
const recommendationRoutes = require('./routes/recommendations');

const app = express();
const PORT = 3001;

app.use(compression());
app.use(cors({ origin: 'http://localhost:5173' }));
app.use(express.json());

app.use('/api/auth', authRoutes);
app.use('/api/products', productRoutes);
app.use('/api/orders', orderRoutes);
app.use('/api/recommendations', recommendationRoutes);

app.listen(PORT, () => console.log(`Server: http://localhost:${PORT}`));
