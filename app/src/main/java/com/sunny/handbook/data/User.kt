package com.sunny.handbook.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("_id")
    val _id: String? = null,
    @SerializedName("building")
    val building: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("room_number")
    val room_number: String,
    @SerializedName("student_roll")
    val student_roll: String
) : Parcelable