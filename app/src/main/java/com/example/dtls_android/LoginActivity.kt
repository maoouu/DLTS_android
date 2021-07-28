package com.example.dtls_android

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        usernameField = findViewById(R.id.usernameLoginField)
        passwordField = findViewById(R.id.passwordLoginField)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnLogin.setOnClickListener{
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            val username = usernameField.text.toString().replace("\\s".toRegex(), "")
            val password = passwordField.text.toString().replace("\\s".toRegex(), "")
            if (username == "admin" && password == "password") {
                startActivity(intent)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                passwordField.setText("")
                Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
            }
        }

        btnSignup.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}