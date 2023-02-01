package com.example.vapeur.NetworkRequest

import com.example.vapeur.Class.User
import com.example.vapeur.Interfaces.BackendApi
import com.example.vapeur.NetworkClass.UserLogin
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkRequest {
    private val retrofit = Retrofit
        .Builder()
        .baseUrl("http://10.0.2.2:3001/") // change this IP for testing by your actual machine IP
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(BackendApi::class.java)

    suspend fun login(
        mail:String,
        password : String): User {
        val credential = UserLogin(mail, password)
        return retrofit.loginAsync(credential).await()
    }
}