package com.example.vapeur.Interfaces
import com.example.vapeur.Class.User
import com.example.vapeur.networkClass.UserLogin
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {
    @POST("user/login")
    fun login(@Body userLogin: UserLogin): Deferred<User>
}