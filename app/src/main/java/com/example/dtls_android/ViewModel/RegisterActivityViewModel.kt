package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.NewAccount
import com.example.dtls_android.service.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivityViewModel : ViewModel() {
    var registerAccountData: MutableLiveData<RegisterResponse> = MutableLiveData()

    fun getRegisterAccountDataObservable(): MutableLiveData<RegisterResponse> {
        return registerAccountData
    }

    fun register(newAccount: NewAccount) {
        val api = RetrofitClient.webservice
        val call = api.register(newAccount)

        call.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    registerAccountData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response to register was unsuccessful.")
                    registerAccountData.postValue(null)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d(null, "Error: Callback to register has failed.")
                registerAccountData.postValue(null)
            }
        })
    }
}