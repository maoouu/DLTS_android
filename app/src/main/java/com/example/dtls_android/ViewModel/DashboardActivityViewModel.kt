package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DashboardActivityViewModel: ViewModel() {

    var recyclerListData: MutableLiveData<List<Record>> = MutableLiveData()
    var deleteRecordData: MutableLiveData<Record> = MutableLiveData()

    fun getRecordListObservable(): MutableLiveData<List<Record>> {
        return recyclerListData
    }

    fun getDeleteRecordDataObservable(): MutableLiveData<Record> {
        return deleteRecordData
    }

    //fun getRecordList() {
    fun getRecordList(token: String?) {
        val api = RetrofitClient.webservice
        //val call = api.getRecordsList()
        val call = api.getRecordsList("token $token")

        call.enqueue(object: Callback<List<Record>> {
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d(null, "Error: Callback for getRecordsList() has failed.")
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    recyclerListData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response for getRecordsList() has failed.")
                    recyclerListData.postValue(null)
                }
            }
        })
    }

    fun searchRecord(query: String, token: String?) {
        val api = RetrofitClient.webservice
        val call = api.getRecordsList("token $token")

        call.enqueue(object: Callback<List<Record>> {
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d(null, "Error: Callback for getRecordsList() has failed.")
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    val responseList = response.body()
                    val tempList: MutableList<Record> = mutableListOf()
                    responseList?.forEach {
                        val author = it.author.toLowerCase(Locale.getDefault())
                        val desc = it.description.toLowerCase(Locale.getDefault())
                        val status = it.status.toLowerCase(Locale.getDefault())
                        // Add to temporary list if one of the fields contain the query
                        val queryFound = author.contains(query) || desc.contains(query) || status.contains(query)
                        if (queryFound) { tempList.add(it) }
                    }
                    recyclerListData.postValue(tempList as List<Record>)

                } else {
                    Log.d(null, "Error: Response for getRecordsList() has failed.")
                    recyclerListData.postValue(null)
                }
            }
        })
    }

    fun deleteRecord(id: String) {
        val api = RetrofitClient.webservice
        val call = api.deleteRecord(id)

        call.enqueue(object: Callback<Record> {
            override fun onResponse(call: Call<Record>, response: Response<Record>) {
                if (response.isSuccessful) {
                    deleteRecordData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response to delete record was not successful.")
                    deleteRecordData.postValue(null)
                }
            }

            override fun onFailure(call: Call<Record>, t: Throwable) {
                Log.d(null, "Error: Callback to delete record has failed.")
                deleteRecordData.postValue(null)
            }

        })
    }
}