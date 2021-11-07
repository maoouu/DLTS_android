package com.example.dtls_android.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtls_android.service.RetrofitClient
import com.example.dtls_android.service.response.Account
import com.example.dtls_android.service.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivityViewModel : ViewModel() {
    var loginAccountData: MutableLiveData<LoginResponse> = MutableLiveData()

    fun getLoginAccountDataObservable(): MutableLiveData<LoginResponse> {
        return loginAccountData
    }

    fun login(account: Account) {
        val api = RetrofitClient.webservice
        val call = api.login(account)

        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    loginAccountData.postValue(response.body())
                } else {
                    Log.d(null, "Error: Response to login was not successful.")
                    loginAccountData.postValue(null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(null, "Error: Callback to login has failed.")
                loginAccountData.postValue(null)
            }

        })
    }
}