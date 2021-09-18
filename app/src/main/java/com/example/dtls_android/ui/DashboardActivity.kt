package com.example.dtls_android.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.collections.ArrayList
import android.content.Intent
import android.graphics.DiscretePathEffect
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.DataAdapter
import com.example.dtls_android.Log
import com.example.dtls_android.LogAdapter
import com.example.dtls_android.R
import com.example.dtls_android.ViewModel.DashboardActivityViewModel
import com.example.dtls_android.databinding.ActivityDashboardBinding
import com.example.dtls_android.databinding.ContentMainBinding
import com.example.dtls_android.resources.MyResources
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.RecordsList
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.awaitResponse
import java.lang.Exception
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.internal.schedulers.IoScheduler
import java.time.LocalDateTime
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var id = 0
    private lateinit var binding: ActivityDashboardBinding

    // Data Structure
    private lateinit var newLogList: ArrayList<Log>
    private lateinit var tempLogList: ArrayList<Log>

    // RecyclerView
    private lateinit var recyclerViewAdapter: DataAdapter
    private lateinit var viewModel: DashboardActivityViewModel
    private lateinit var rvProgressBar: ProgressBar
    private lateinit var adapter: LogAdapter
    //private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var addButton: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var resultContract: ActivityResultLauncher<Intent>
    //private lateinit var itemDecor: DividerItemDecoration

    // Navigation Header
    private lateinit var mheaderView: View
    private lateinit var mheaderUsername: TextView
    private lateinit var mheaderDesc: TextView

    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
        session = LoginPref(this)
        session.checkLogin()

        loadContent()
        loadNavigationView()
        loadRecyclerView()
        initViewModel()

        // initialize data structures
        //newLogList = arrayListOf()
        //tempLogList = arrayListOf()
        //getSampleData()

        // handle activity result
        //establishResultContract()

        addButton.setOnClickListener { redirectToAdd() }

    }

    private fun loadContent() {
        // Initiate Main Content
        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        rvProgressBar = findViewById(R.id.rvProgressBar)
        textNoTask.visibility = View.GONE
    }

    private fun loadNavigationView() {
        // Initiate Navigation View
        mNavigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mheaderView = mNavigationView.getHeaderView(0)
        mheaderUsername = mheaderView.findViewById(R.id.nav_header_username)
        mheaderDesc = mheaderView.findViewById(R.id.nav_header_desc)
        mheaderUsername.text = session.getUserDetails()
        mheaderDesc.text = "Hello World!~"

        val toggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            binding.toolbar,
            0,
            0
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    private fun loadRecyclerView() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        linear.stackFromEnd = true
        val rvBinding: RecyclerView = findViewById(R.id.recyclerView)
        rvBinding.apply {
            layoutManager = linear
            val decoration = DividerItemDecoration(this@DashboardActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = DataAdapter()
            adapter = recyclerViewAdapter
        }
        // setup recyclerview adapter
        //itemDecor = DividerItemDecoration(this, layoutManager.orientation)
        //mRecyclerView.layoutManager = layoutManager
        //mRecyclerView.addItemDecoration(itemDecor)
        //initializeApi()
    }

    private fun initViewModel() {
        rvProgressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this).get(DashboardActivityViewModel::class.java)
        viewModel.getRecordListObservable().observe(this, {
            if (it == null) {
                Toast.makeText(this@DashboardActivity, "No results found", Toast.LENGTH_LONG).show()
            } else {
                recyclerViewAdapter.recordsList = it.toMutableList()
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getRecordList()
        rvProgressBar.visibility = View.GONE
    }

    private fun establishResultContract() {
        resultContract = registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("STATUS").toString(),
                    LocalDateTime.now(),
                    result.data?.getStringExtra("DESC").toString(),
                )
                newLogList.add(newLog)
                tempLogList.add(newLog)
                mRecyclerView.adapter?.notifyDataSetChanged()
                Toast.makeText(this, "A new entry has been added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setIcon(R.drawable.ic_signout)
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") {
                dialog,_->
                session.logoutUser()
                finish()
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /**
    private fun getSampleData() {
        // Remove empty text upon generating sample data
        textNoTask.visibility = View.GONE
        val sample = MyResources

        for (item in sample.author.indices) {
            val sampleData = Log(
                id++,
                sample.author[item],
                sample.status[item],
                sample.date[item],
                sample.description[item]
            )
            newLogList.add(sampleData)
        }

        tempLogList.addAll(newLogList)
        adapter = LogAdapter(tempLogList, newLogList)
    }
    **/

    private fun checkIsEmpty(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        textNoTask.visibility = if (adapter?.itemCount!! > 0) View.GONE else View.VISIBLE
    }

    private fun redirectToAdd() {
        val intent = Intent(this, AddLogActivity::class.java)
        resultContract.launch(intent)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView

        searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
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
                        val itemAuthor = it.author.toLowerCase(Locale.getDefault())
                        val itemDesc = it.description.toLowerCase(Locale.getDefault())
                        val itemStatus = it.status.toLowerCase(Locale.getDefault())

                        val queryFound: Boolean = itemAuthor.contains(searchText) ||
                                itemDesc.contains(searchText) || itemStatus.contains(searchText)

                        if (queryFound) { tempLogList.add(it) }
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_menu_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_menu_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_menu_logout -> {
                confirmLogout()
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}