package com.example.dtls_android.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.dtls_android.R
import com.example.dtls_android.account.AccountManager
import com.google.android.material.textfield.TextInputEditText

interface RegisterActivityWatcher: TextWatcher {
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
}

class RegisterActivity : AppCompatActivity(), RegisterActivityWatcher {

        private lateinit var usernameField: TextInputEditText
        private lateinit var passwordField: TextInputEditText
        private lateinit var confirmPassField: TextInputEditText

        private lateinit var btnRegister: Button
        private lateinit var btnBack: Button

        lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        accountManager = intent.getParcelableExtra("ACCOUNTMANAGER")!!

        usernameField = findViewById(R.id.usernameRegisterInput)
        passwordField = findViewById(R.id.passwordRegisterInput)
        confirmPassField = findViewById(R.id.confirmPassRegisterInput)

        btnRegister = findViewById(R.id.btnRegister)
        btnBack = findViewById(R.id.btnBack)
        btnRegister.isEnabled = false

        passwordField.addTextChangedListener(this)

        confirmPassField.addTextChangedListener(this)

        btnRegister.setOnClickListener { register() }

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        btnRegister.isEnabled = passwordField.text.toString() == confirmPassField.text.toString()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun register() {
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (accountManager.exists(username)) {
            usernameField.setText("")
            passwordField.setText("")
            Toast.makeText(this, "Account already exists.", Toast.LENGTH_SHORT).show()
        } else {
            val data = Intent()
            data.putExtra("USERNAME", username)
            data.putExtra("PASSWORD", password)
            //Toast.makeText(this, "A new user has been created.", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}