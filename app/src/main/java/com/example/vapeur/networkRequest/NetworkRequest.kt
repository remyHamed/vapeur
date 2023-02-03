package com.example.vapeur.networkRequest

import com.example.vapeur.Class.User
import com.example.vapeur.Interfaces.BackendApi
import com.example.vapeur.networkClass.UserLogin
import com.example.vapeur.networkClass.UserRegister
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.await
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
        return retrofit.login(credential).await()
    }

    suspend fun create(
        userName :String,
        mail:String,
        password : String): User {
        val bodyUser = UserRegister(userName,mail, password)
        return retrofit.create(bodyUser).await()
    }

    suspend fun password(
        mail:String,
        password : String): User {
        return retrofit.changePassword(mail,password).await()
    }
}