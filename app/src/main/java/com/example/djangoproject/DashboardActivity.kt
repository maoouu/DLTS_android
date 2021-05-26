package com.example.djangoproject

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

class DashboardActivity : AppCompatActivity() {

    var id = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        val logList = ArrayList<Log>()
        val log1 = Log(id++, "CSMA", "To be approved by the President.", Date())
        val log2 = Log(id++, "ECE", "Pending.", Date())
        val log3 = Log(id++, "BED", "Approved", Date())
        val log4 = Log(id++, "CAFA", "Approved", Date())

        logList.add(log1)
        logList.add(log2)
        logList.add(log3)
        logList.add(log4)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LogAdapter(logList) {
            toast("${it.title} Clicked")
        }

        fab.setOnClickListener {
            logList.add(Log(id++, "Title [${id}]", "Description [${id}]", Date()))
            recyclerView.adapter?.notifyDataSetChanged()
        }

        // If there are no entries
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
        }

        return true
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}