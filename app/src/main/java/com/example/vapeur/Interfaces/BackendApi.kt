package com.example.vapeur.Interfaces
import com.example.vapeur.Class.User
import com.example.vapeur.networkClass.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {
    @POST("users")
    fun login(@Body userLogin: UserLogin): Call<User>
}