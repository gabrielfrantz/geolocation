package com.example.geolocation.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class UserRequest(
    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "username")
    val username: String,

    @Json(name = "password")
    val password: String
)

interface UsersApi {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users")
    suspend fun postUser(@Body userRequest: UserRequest): Response<Unit>
}