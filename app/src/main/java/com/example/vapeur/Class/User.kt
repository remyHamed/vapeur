package com.example.vapeur.Class

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("mail")
    public var mail: String = "";
    @SerializedName("password")
    public var password: String = "";
    @SerializedName("_id")
    public val _id: String = "";

    constructor(mail: String, password: String) {
        this.mail = mail
        this.password = password
    }
}