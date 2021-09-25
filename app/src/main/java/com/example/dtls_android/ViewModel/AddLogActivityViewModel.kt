package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLogActivityViewModel : ViewModel() {

    var addLogLiveData: MutableLiveData<Record?> = MutableLiveData()

    fun getAddLogObservable(): MutableLiveData<Record?> {
        return addLogLiveData
    }

    fun addNewRecord(record: Record) {
        val api = RetrofitClient.webservice
        val call = api.createRecord(record)

        call.enqueue(object: Callback<Record?> {
            override fun onFailure(call: Call<Record?>, t: Throwable) {
                Log.d(null, "Error: Callback for addNewRecord() has failed.")
                addLogLiveData.postValue(null)
            }

            override fun onResponse(call: Call<Record?>, response: Response<Record?>) {
                if (response.isSuccessful) {
                    addLogLiveData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response for addNewRecord() has failed.")
                    addLogLiveData.postValue(null)
                }
            }
        })
    }

}