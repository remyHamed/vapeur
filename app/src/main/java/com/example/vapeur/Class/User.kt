package com.example.vapeur.Class

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("mail")
    val mail: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("_id")
    val _id: String
)
