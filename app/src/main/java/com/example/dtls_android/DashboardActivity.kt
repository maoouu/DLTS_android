package com.example.dtls_android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private var id = 0

    private lateinit var newLogList: ArrayList<Log>
    private lateinit var tempLogList: ArrayList<Log>

    private lateinit var adapter: LogAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var fab: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var resultContract: ActivityResultLauncher<Intent>

    // TODO: Create Sample Data
    private lateinit var sampleAuthor: Array<String>
    private lateinit var sampleDesc: Array<String>
    private lateinit var sampleDate: Array<LocalDateTime>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        sampleAuthor = arrayOf(
            "MSEUF",
            "AMA",
            "DLL",
            "FEU"
        )

        sampleDesc = arrayOf(
            "Approved",
            "Pending",
            "Declined",
            "Acknowledged"
        )

        sampleDate = arrayOf(
            LocalDateTime.of(2020, 7, 15, 10, 30),
            LocalDateTime.of(2020, 8, 1, 12, 25),
            LocalDateTime.of(2020, 11, 23, 16, 6),
            LocalDateTime.of(2020, 12, 30, 13, 1)
        )

        newLogList = arrayListOf()
        tempLogList = arrayListOf()
        getSampleData()

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter


        resultContract = registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("DESC").toString(),
                    LocalDateTime.now()
                )
                newLogList.add(newLog)
                tempLogList.add(newLog)
                mRecyclerView.adapter?.notifyDataSetChanged()
                Toast.makeText(this, "A new entry has been added", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty(tempLogList)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this, AddLogActivity::class.java)
            resultContract.launch(intent)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempLogList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault()).trim()

                if (searchText.isNotBlank()) {
                    newLogList.forEach {
                        val queryFound = it.author
                            .toLowerCase(Locale.getDefault())
                            .contains(searchText)

                        if (queryFound) {
                            tempLogList.add(it)
                        }
                    }

                    mRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    tempLogList.addAll(newLogList)
                    mRecyclerView.adapter?.notifyDataSetChanged()
                }

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    private fun getSampleData() {
        // Remove empty text upon generating sample data
        textNoTask.visibility = View.GONE

        for (item in sampleAuthor.indices) {
            val sampleData = Log(id++, sampleAuthor[item], sampleDesc[item], sampleDate[item])
            newLogList.add(sampleData)
        }

        tempLogList.addAll(newLogList)
        adapter = LogAdapter(tempLogList)
    }


    private fun checkIsEmpty(logList: ArrayList<Log>) {
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }
}