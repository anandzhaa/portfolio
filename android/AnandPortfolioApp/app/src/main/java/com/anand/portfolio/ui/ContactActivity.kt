package com.anand.portfolio.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anand.portfolio.R
import com.anand.portfolio.network.ApiClient
import com.anand.portfolio.model.ContactRequest
import kotlinx.coroutines.launch

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Back button
        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Send button
        findViewById<Button>(R.id.btnSend).setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        val name    = findViewById<EditText>(R.id.etName).text.toString().trim()
        val email   = findViewById<EditText>(R.id.etEmail).text.toString().trim()
        val subject = findViewById<EditText>(R.id.etSubject).text.toString().trim()
        val message = findViewById<EditText>(R.id.etMessage).text.toString().trim()
        val btn     = findViewById<Button>(R.id.btnSend)
        val status  = findViewById<TextView>(R.id.tvStatus)

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            showStatus("Please fill in name, email and message.", false)
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showStatus("Please enter a valid email address.", false)
            return
        }

        btn.isEnabled = false
        btn.text = "Sending..."
        status.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = ApiClient.service.sendContact(
                    ContactRequest(name, email, subject.ifEmpty { "Message from App" }, message)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    showStatus("✓ Message sent! I'll get back to you soon.", true)
                    clearForm()
                } else {
                    showStatus("Failed to send. Please try again.", false)
                }
            } catch (e: Exception) {
                showStatus("Network error. Please check your connection.", false)
            } finally {
                btn.isEnabled = true
                btn.text = "Send Message"
            }
        }
    }

    private fun showStatus(message: String, success: Boolean) {
        val tv = findViewById<TextView>(R.id.tvStatus)
        tv.text = message
        tv.setTextColor(
            if (success) getColor(R.color.accent_green)
            else getColor(R.color.accent_orange)
        )
        tv.visibility = View.VISIBLE
    }

    private fun clearForm() {
        listOf(R.id.etName, R.id.etEmail, R.id.etSubject, R.id.etMessage).forEach {
            findViewById<EditText>(it).text.clear()
        }
    }
}
