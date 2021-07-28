package com.example.dtls_android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class AddLogActivity : AppCompatActivity() {
    private lateinit var authorField: TextInputEditText
    private lateinit var descField: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)

        authorField = findViewById(R.id.authorInput)
        descField = findViewById(R.id.descriptionInput)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        btnSave.isEnabled = false

        authorField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val author: String = authorField.text.toString().trim {it <= ' '}
                val description: String = descField.text.toString().trim {it <= ' '}
                btnSave.isEnabled = author.isNotEmpty() && description.isNotEmpty()
            }
        })

        descField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val author: String = authorField.text.toString().trim {it <= ' '}
                val description: String = descField.text.toString().trim {it <= ' '}
                btnSave.isEnabled = author.isNotEmpty() && description.isNotEmpty()
            }
        })

        btnSave.setOnClickListener {
            val author = authorField.text.toString().trim()
            val description = descField.text.toString().trim()

            val data = Intent()
            data.putExtra("AUTHOR", author)
            data.putExtra("DESC", description)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}