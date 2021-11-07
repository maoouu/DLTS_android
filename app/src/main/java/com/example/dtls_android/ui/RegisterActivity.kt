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
import androidx.lifecycle.ViewModelProvider
import com.example.dtls_android.R
import com.example.dtls_android.ViewModel.RegisterActivityViewModel
import com.example.dtls_android.account.AccountManager
import com.example.dtls_android.service.response.NewAccount
import com.google.android.material.textfield.TextInputEditText

interface RegisterActivityWatcher: TextWatcher {
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
}

class RegisterActivity : AppCompatActivity(), RegisterActivityWatcher {

        private lateinit var emailField: TextInputEditText
        private lateinit var usernameField: TextInputEditText
        private lateinit var passwordField: TextInputEditText
        private lateinit var confirmPassField: TextInputEditText

        private lateinit var btnRegister: Button
        private lateinit var btnBack: Button
        private lateinit var viewModel: RegisterActivityViewModel

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

        viewModel = ViewModelProvider(this).get(RegisterActivityViewModel::class.java)
        emailField = findViewById(R.id.emailRegisterInput)
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
        registerObservable()
    }

    private fun registerObservable() {
        viewModel.getRegisterAccountDataObservable().observe(this, {
            if (it != null) {
                Toast.makeText(this@RegisterActivity, it.successMsg, Toast.LENGTH_LONG).show()
                finish()
            } else {
                val error = "There's a problem in registering your account, please try again."
                emailField.setText("")
                usernameField.setText("")
                passwordField.setText("")
                confirmPassField.setText("")
                Toast.makeText(this@RegisterActivity, error, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun afterTextChanged(p0: Editable?) {
        val emailIsNotEmpty = emailField.text.toString().isNotEmpty()
        btnRegister.isEnabled = passwordField.text.toString() == confirmPassField.text.toString() && emailIsNotEmpty
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun register() {
        val email = emailField.text.toString().trim()
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString()

        if (username.isBlank() || password.isBlank()) {
            val error = "Please enter your username and password."
            usernameField.setText("")
            passwordField.setText("")
            confirmPassField.setText("")
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.register(NewAccount(username, password, email))
        }
    }
}