package com.example.djangoproject

import android.content.Intent
import android.view.WindowManager
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        // Send user to Login screen as this activity loads
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            // remove this activity from the stack
            finish()
        }, 3000)
    }
}