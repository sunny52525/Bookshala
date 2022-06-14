package com.sunny.handbook.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("User")
    val User: User,
)


@Parcelize
data class HomeResponse(
    @SerializedName("book")
    val book: Book?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("_id")
    val _id:String? = null
) : Parcelable

/**
 * {
"name":"Sunny",
"student_roll":"2019404022",
"phone":"7004468293",
"email":"kumarsunny3232@gmail.com",
"room_number":"708",
"building":"HR1",
"password":"help"
}
 */

data class RegisterRequest(
    val name: String,
    val student_roll: String,
    val phone: String,
    val email: String,
    val room_number: String,
    val building: String,
    val password: String,
)

data class BaseResponse(
    val message:String
)