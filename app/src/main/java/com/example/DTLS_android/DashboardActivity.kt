package com.example.DTLS_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    var id = 0L
    val logList = ArrayList<Log>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        val log1 = Log("CSMA", "To be approved by the President.", Date())
        val log2 = Log("ECE", "Pending.", Date())
        val log3 = Log("BED", "Approved", Date())
        val log4 = Log("CAFA", "Approved", Date())

        logList.add(log1)
        logList.add(log2)
        logList.add(log3)
        logList.add(log4)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LogAdapter(logList) {
            toast("${it.title} Clicked")
        }

        fab.setOnClickListener {
            //TODO: create an activity that will handle a new entry
            val intent = Intent(this, AddLogActivity::class.java)
            startActivity(intent)
            finish()

            //logList.add(Log(id++, "Title [${id}]", "Description [${id}]", Date()))
            //recyclerView.adapter?.notifyDataSetChanged()
        }

        // If there are no entries
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}