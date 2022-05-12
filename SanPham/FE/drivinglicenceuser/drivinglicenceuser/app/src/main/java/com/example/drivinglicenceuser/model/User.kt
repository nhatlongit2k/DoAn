package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.Instant

class User(
    @SerializedName("id")
    var id: Long,
    @SerializedName("login")
    var login: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("activated")
    var activated: Boolean,
    @SerializedName("langKey")
    var langKey: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("authorities")
    var authorities: ArrayList<String>,
    @SerializedName("phoneNumber")
    var phoneNumber: String

): Serializable {
}