package com.example.dtls_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dtls_android.session.LoginPref

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button

    lateinit var session: LoginPref

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

        session = LoginPref(this)
        // check if user is already logged in
        if (session.isLoggedIn()) {
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        usernameField = findViewById(R.id.usernameLoginField)
        passwordField = findViewById(R.id.passwordLoginField)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnLogin.setOnClickListener{
            val usernameInput = usernameField.text.toString().trim()
            val passwordInput = passwordField.text.toString().trim()

            if (usernameInput.isBlank() || passwordInput.isBlank()) {
                usernameField.setText("")
                passwordField.setText("")
                Toast.makeText(this, "Please enter your username and password.", Toast.LENGTH_LONG).show()
            } else if (usernameInput == "admin" && passwordInput == "password") {
                session.createLoginSession(usernameInput)
                // Start Activity
                val intent = Intent(applicationContext, DashboardActivity::class.java)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            } else {
                usernameField.setText("")
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