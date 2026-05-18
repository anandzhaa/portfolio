package com.anand.portfolio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anand.portfolio.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Hide system UI for full-screen splash
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )

        val nameText = findViewById<TextView>(R.id.splashName)
        val roleText = findViewById<TextView>(R.id.splashRole)
        val tagText  = findViewById<TextView>(R.id.splashTag)

        // Animate elements in sequence
        lifecycleScope.launch {
            delay(300)
            nameText.visibility = View.VISIBLE
            nameText.startAnimation(AnimationUtils.loadAnimation(this@SplashActivity, android.R.anim.fade_in))

            delay(400)
            roleText.visibility = View.VISIBLE
            roleText.startAnimation(AnimationUtils.loadAnimation(this@SplashActivity, android.R.anim.slide_in_left))

            delay(400)
            tagText.visibility = View.VISIBLE
            tagText.startAnimation(AnimationUtils.loadAnimation(this@SplashActivity, android.R.anim.fade_in))

            delay(1800)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
