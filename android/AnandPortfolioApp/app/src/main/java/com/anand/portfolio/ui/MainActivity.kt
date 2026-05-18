package com.anand.portfolio.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.anand.portfolio.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupClickListeners()
        animateHeroSection()
    }

    private fun setupClickListeners() {
        // GitHub button
        findViewById<View>(R.id.btnGitHub).setOnClickListener {
            openUrl("https://github.com/anandzhaa")
        }

        // Email button
        findViewById<View>(R.id.btnEmail).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:anandjha9816@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "Opportunity from Portfolio App")
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        // Phone button
        findViewById<View>(R.id.btnPhone).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+9779816810289")
            }
            startActivity(intent)
        }

        // Contact Me button
        findViewById<View>(R.id.btnContactMe).setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        // Project card 1 — Docker CI/CD
        findViewById<CardView>(R.id.cardProject1).setOnClickListener {
            openUrl("https://github.com/anandzhaa/fullstack-docker-deployment")
        }

        // Project card 2 — Ecommerce
        findViewById<CardView>(R.id.cardProject2).setOnClickListener {
            openUrl("https://github.com/anandzhaa/project1")
        }

        // View all projects
        findViewById<View>(R.id.btnViewAllProjects).setOnClickListener {
            openUrl("https://github.com/anandzhaa")
        }

        // Download CV
        findViewById<View>(R.id.btnDownloadCV).setOnClickListener {
            Toast.makeText(this, "CV download coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateHeroSection() {
        val elements = listOf<View>(
            R.id.tvAvailBadge,
            R.id.tvHeroName,
            R.id.tvHeroRole,
            R.id.tvHeroDesc,
            R.id.heroButtons
        ).mapNotNull { id ->
            try { findViewById(id) } catch (e: Exception) { null }
        }

        elements.forEachIndexed { index, view ->
            view?.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            }, (index * 150 + 200).toLong())
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open URL", Toast.LENGTH_SHORT).show()
        }
    }
}
