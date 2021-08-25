package com.example.dtls_android

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.collections.ArrayList
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.databinding.ActivityDashboardBinding
import com.example.dtls_android.resources.MyResources
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import java.time.LocalDateTime
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var id = 0
    private lateinit var binding: ActivityDashboardBinding

    // Data Structure
    private lateinit var newLogList: ArrayList<Log>
    private lateinit var tempLogList: ArrayList<Log>
    //private lateinit var recordsList: List<RecordsResponseItem>

    // RecyclerView
    private lateinit var adapter: LogAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var addButton: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var resultContract: ActivityResultLauncher<Intent>
    private lateinit var itemDecor: DividerItemDecoration

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
        // initialize data structures
        //newLogList = arrayListOf()
        //tempLogList = arrayListOf()
        //getSampleData()

        // handle activity result
        //establishResultContract()

        //addButton.setOnClickListener { redirectToAddLog() }

    }

    private fun loadContent() {
        // Initiate Main Content
        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        textNoTask.visibility = View.GONE
        loadNavigationView()
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

        loadRecyclerView()
    }

    private fun loadRecyclerView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        // setup recyclerview adapter
        itemDecor = DividerItemDecoration(this, layoutManager.orientation)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(itemDecor)
        initializeApi()
    }

    private fun initializeApi() {
        val recordsAPI = RetrofitClient.webservice
        val response = recordsAPI.getRecordsList()
        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            mRecyclerView.adapter = DataAdapter(this, it)
            //mEmptyRecyclerView.adapter = DataAdapter(this, it)
        }
        mRecyclerView.adapter?.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                checkIsEmpty(mRecyclerView.adapter)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                checkIsEmpty(mRecyclerView.adapter)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                checkIsEmpty(mRecyclerView.adapter)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                checkIsEmpty(mRecyclerView.adapter)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                checkIsEmpty(mRecyclerView.adapter)
            }

            override fun onChanged() {
                checkIsEmpty(mRecyclerView.adapter)
            }
        })
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

    private fun checkIsEmpty(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        textNoTask.visibility = if (adapter?.itemCount!! > 0) View.GONE else View.VISIBLE
    }

    private fun redirectToAddLog() {
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