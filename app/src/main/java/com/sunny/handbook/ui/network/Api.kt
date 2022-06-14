package com.sunny.handbook.ui.network

import com.sunny.handbook.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  "name":"Sunny",
"student_roll":"2019404022",
"phone":"7004468293",
"email":"kumarsunny3232@gmail.com",
"room_number":"708",
"building":"HR1",
"password":"help"
 */
interface Api {
    @POST("login")
    suspend fun login(
        @Body login: LoginRequest
    ): AuthResponse

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): AuthResponse

    @GET("home")
    suspend fun home(): List<HomeResponse>

    @POST("book-add")
    suspend fun addBook(@Body homeResponse: HomeResponse): BaseResponse

    @POST("book-delete")
    suspend fun deleteBook(@Query("book_id") bookID: String): BaseResponse
}