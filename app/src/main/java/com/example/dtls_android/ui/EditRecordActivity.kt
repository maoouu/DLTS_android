package com.example.dtls_android.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dtls_android.R
import com.example.dtls_android.ViewModel.EditRecordActivityViewModel
import com.example.dtls_android.service.response.Record
import com.example.dtls_android.session.AccountPref
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface EditLogWatcher: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
}

class EditRecordActivity : AppCompatActivity(), EditLogWatcher {
    private lateinit var autoCompleteEditTextView: MaterialAutoCompleteTextView
    private lateinit var authorEditField: TextInputEditText
    private lateinit var descEditField: TextInputEditText
    private lateinit var dropdownAdapter: ArrayAdapter<String>
    private lateinit var status: Array<String>
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var viewModel: EditRecordActivityViewModel

    private lateinit var authorString: String
    private lateinit var descString: String
    private lateinit var statusString: String
    private lateinit var dateCreatedString: String
    private lateinit var accountSession: AccountPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_log)
        val recordId = intent.getStringExtra("record_id")!!
        accountSession = AccountPref(this)

        initActivity()
        initViewModel()
        loadRecordObservable(recordId)
        updateRecordObservable()

        btnSave.setOnClickListener { save(recordId) }
        btnCancel.setOnClickListener { finish() }
    }

    private fun initActivity() {
        authorEditField = findViewById(R.id.authorEditInput)
        descEditField = findViewById(R.id.descriptionEditInput)
        status = resources.getStringArray(R.array.status)
        dropdownAdapter = ArrayAdapter(this, R.layout.dropdown_item, status)
        autoCompleteEditTextView = findViewById(R.id.statusEditFieldAuto)
        btnSave = findViewById(R.id.btnSaveEdit)
        btnCancel = findViewById(R.id.btnCancelEdit)

        btnSave.isEnabled = false
        authorEditField.addTextChangedListener(this)
        descEditField.addTextChangedListener(this)
        autoCompleteEditTextView.addTextChangedListener(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(EditRecordActivityViewModel::class.java)
    }

    private fun loadRecordObservable(id: String) {
        viewModel.getLoadRecordDataObservable().observe(this, {
            if (it != null) {
                dateCreatedString = it.dateCreated
                authorString = it.author
                descString = it.description
                statusString = it.status

                authorEditField.setText(authorString)
                descEditField.setText(descString)
                autoCompleteEditTextView.setText(statusString)
                autoCompleteEditTextView.setAdapter(dropdownAdapter)
            }
        })
        viewModel.getRecordById(id, accountSession.getToken())
    }

    private fun updateRecordObservable() {
        viewModel.getUpdateRecordDataObservable().observe(this, {
            if (it == null) {
                Toast.makeText(this@EditRecordActivity, "Failed to update record.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@EditRecordActivity, DashboardActivity::class.java)
                Toast.makeText(this@EditRecordActivity, "A record has been successfully updated.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        })
    }

    private fun save(id: String) {
        val author = authorEditField.text.toString().trim()
        val dateCreated = dateCreatedString
        val dateModified = LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))
        val description = descEditField.text.toString().trim()
        val status = autoCompleteEditTextView.editableText.toString()

        val record = Record(id, author, dateCreated, dateModified, description, status)
        viewModel.updateRecord(id, record, accountSession.getToken())
    }

    override fun afterTextChanged(p0: Editable?) {
        val isModified = !(
                authorEditField.text.toString() == authorString &&
                descEditField.text.toString() == descString &&
                autoCompleteEditTextView.text.toString() == statusString
                )
        btnSave.isEnabled = isModified
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}