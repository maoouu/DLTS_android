package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.Record
import com.example.dtls_android.service.response.RecordsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

class DashboardActivityViewModel: ViewModel() {

    //var recyclerListData: MutableLiveData<RecordsList> = MutableLiveData()
    var recyclerListData: MutableLiveData<List<Record>> = MutableLiveData()

    //fun getRecordListObservable(): MutableLiveData<RecordsList> {
    fun getRecordListObservable(): MutableLiveData<List<Record>> {
        return recyclerListData
    }

    fun getRecordList() {
        val api = RetrofitClient.webservice
        val call = api.getRecordsList()

        call.enqueue(object: Callback<List<Record>> {
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Log.d(null, "Response Failed 1")
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    recyclerListData.postValue(response.body())
                } else {
                    Log.d(null, "Response Failed 2")
                    recyclerListData.postValue(null)
                }
            }
        })
        /**
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getRecordsList().awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!

                    withContext(Dispatchers.Main) {
                        recyclerListData.postValue(data)
                    }
                } else {
                    Log.d(null, "Response Failed.")
                    recyclerListData.postValue(null)
                }

            } catch (err: IOException) {
                err.printStackTrace()
            }
        }**/
        /**
        call.enqueue(object: Callback<RecordsList> {
            override fun onFailure(call: Call<RecordsList>, t: Throwable) {
                Log.d(null, "Response Failed 1")
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<RecordsList>, response: Response<RecordsList>) {
                if (response.isSuccessful) {
                    recyclerListData.postValue(response.body())
                } else {
                    Log.d(null, "Response Failed 2")
                    recyclerListData.postValue(null)
                }
            }
        })
        **/
    }
}