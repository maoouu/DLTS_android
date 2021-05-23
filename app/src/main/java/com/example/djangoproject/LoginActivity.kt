package com.example.djangoproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        val usernameField: EditText = findViewById(R.id.usernameLoginField)
        val passwordField: EditText = findViewById(R.id.passwordLoginField)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignup: Button = findViewById(R.id.btnSignup)

        btnLogin.setOnClickListener{
            val username = usernameField.text.toString().replace("\\s".toRegex(), "")
            val password = passwordField.text.toString().replace("\\s".toRegex(), "")
            if (username == "admin" && password == "password") {
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
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