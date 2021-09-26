package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditRecordActivityViewModel : ViewModel() {

    var loadRecordData: MutableLiveData<Record?> = MutableLiveData()
    var updateRecordData: MutableLiveData<Record?> = MutableLiveData()

    fun getLoadRecordDataObservable(): MutableLiveData<Record?> {
        return loadRecordData
    }

    fun getUpdateRecordDataObservable(): MutableLiveData<Record?> {
        return updateRecordData
    }

    fun getRecordById(id: String) {
        val api = RetrofitClient.webservice
        val call = api.getRecordById(id)

        call.enqueue(object: Callback<Record?> {
            override fun onResponse(call: Call<Record?>, response: Response<Record?>) {
                if (response.isSuccessful) {
                    loadRecordData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response to retrieve data was not successful.")
                    loadRecordData.postValue(null)
                }
            }

            override fun onFailure(call: Call<Record?>, t: Throwable) {
                Log.d(null, "Error: Callback to retrieve data has failed.")
                loadRecordData.postValue(null)
            }

        })
    }

    fun updateRecord(id: String, record: Record) {
        val api = RetrofitClient.webservice
        val call = api.updateRecord(id, record)

        call.enqueue(object: Callback<Record?> {
            override fun onResponse(call: Call<Record?>, response: Response<Record?>) {
                if (response.isSuccessful) {
                    updateRecordData.postValue(response.body())
                } else {
                    Log.d(null,"Error: Callback for updateRecord() has failed.")
                    updateRecordData.postValue(null)
                }
            }

            override fun onFailure(call: Call<Record?>, t: Throwable) {
                Log.d(null, "Error: Unable to update record.")
                updateRecordData.postValue(null)
            }

        })
    }


}