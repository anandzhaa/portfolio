// =============================================
// Anand Portfolio Backend — Contact Form API
// Deploy FREE on: Render.com or Railway.app
// =============================================

const express = require('express');
const cors = require('cors');
const { MongoClient } = require('mongodb');

const app = express();
const PORT = process.env.PORT || 3001;

// ===== MIDDLEWARE =====
app.use(cors({
  origin: [
    'https://anandjha.com.np',
    'https://anandmohanjha.com.np',
    'http://localhost:5500',
    'http://127.0.0.1:5500',
    '*' // remove '*' once you have your domain
  ]
}));
app.use(express.json());

// ===== MONGODB CONNECTION =====
// Set MONGODB_URI in your Render/Railway environment variables
// Get it from: https://cloud.mongodb.com → Connect → Drivers
const MONGODB_URI = process.env.MONGODB_URI || 'mongodb+srv://YOUR_USER:YOUR_PASS@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority';
const DB_NAME = 'anand_portfolio';

let db;
MongoClient.connect(MONGODB_URI)
  .then(client => {
    db = client.db(DB_NAME);
    console.log('✅ Connected to MongoDB Atlas');
  })
  .catch(err => {
    console.error('❌ MongoDB connection error:', err.message);
  });

// ===== ROUTES =====
app.get('/', (req, res) => {
  res.json({ status: 'ok', message: 'Anand Portfolio API running ✓' });
});

// POST /api/contact — Save message to MongoDB Atlas
app.post('/api/contact', async (req, res) => {
  try {
    const { name, email, subject, message } = req.body;

    if (!name || !email || !message) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return res.status(400).json({ success: false, error: 'Invalid email address' });
    }

    if (!db) {
      return res.status(503).json({ success: false, error: 'Database not connected' });
    }

    const doc = {
      name: String(name).slice(0, 100),
      email: String(email).slice(0, 200),
      subject: String(subject || 'No subject').slice(0, 200),
      message: String(message).slice(0, 2000),
      receivedAt: new Date(),
      ip: req.headers['x-forwarded-for'] || req.socket.remoteAddress || 'unknown',
      read: false
    };

    const result = await db.collection('messages').insertOne(doc);

    console.log(`📨 New message from ${name} (${email}) — ID: ${result.insertedId}`);

    res.json({
      success: true,
      message: 'Message saved successfully',
      id: result.insertedId
    });

  } catch (err) {
    console.error('Contact form error:', err);
    res.status(500).json({ success: false, error: 'Internal server error' });
  }
});

// GET /api/messages — View all messages (add auth in production!)
app.get('/api/messages', async (req, res) => {
  const secret = req.query.secret;
  if (secret !== process.env.ADMIN_SECRET) {
    return res.status(401).json({ error: 'Unauthorized' });
  }
  try {
    const messages = await db.collection('messages')
      .find({})
      .sort({ receivedAt: -1 })
      .limit(100)
      .toArray();
    res.json({ success: true, count: messages.length, messages });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.listen(PORT, () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
