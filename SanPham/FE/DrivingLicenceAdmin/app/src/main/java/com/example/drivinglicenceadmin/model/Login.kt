package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

public class Login(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String
): Serializable {

}