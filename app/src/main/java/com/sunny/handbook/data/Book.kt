package com.sunny.handbook.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    @SerializedName("author")
    val author: String,
    @SerializedName("imageLink")
    val imageLink: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("publication")
    val publication: String
):Parcelable