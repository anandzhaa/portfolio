// =============================================
// Anand Portfolio Backend — Contact Form API
// Deploy FREE on: Render.com or Railway.app
// =============================================


 const express = require("express");
const cors = require("cors");
const { MongoClient } = require("mongodb");

const app = express();
const PORT = process.env.PORT || 3001;

// ===== MIDDLEWARE =====
app.use(cors());
app.use(express.json());

// ===== MONGODB =====
const uri = process.env.MONGODB_URI;

const client = new MongoClient(uri);

let db;

// Connect MongoDB once when server starts
async function connectDB() {
  try {
    await client.connect();
    db = client.db("anand_portfolio");
    console.log("✅ Connected to MongoDB Atlas");
  } catch (err) {
    console.error("❌ MongoDB connection error:", err);
  }
}

connectDB();

// ===== ROUTES =====
app.get("/", (req, res) => {
  res.json({ status: "ok", message: "Backend is running ✓" });
});

// Contact API
app.post("/api/contact", async (req, res) => {
  try {
    const { name, email, subject, message } = req.body;

    if (!name || !email || !message) {
      return res.status(400).json({ success: false, error: "Missing fields" });
    }

    const doc = {
      name,
      email,
      subject: subject || "No subject",
      message,
      createdAt: new Date(),
    };

    const result = await db.collection("messages").insertOne(doc);

    res.json({
      success: true,
      message: "Saved successfully",
      id: result.insertedId,
    });

  } catch (err) {
    console.error(err);
    res.status(500).json({ success: false, error: "Server error" });
  }
});

// Get messages (optional admin)
app.get("/api/messages", async (req, res) => {
  try {
    const messages = await db
      .collection("messages")
      .find({})
      .sort({ createdAt: -1 })
      .toArray();

    res.json({ success: true, messages });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

// ===== START SERVER =====
app.listen(PORT, () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
