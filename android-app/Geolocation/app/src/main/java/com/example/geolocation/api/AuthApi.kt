package com.example.geolocation.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class AuthRequest(
        @Json(name = "username")
        val username: String,

        @Json(name = "password")
        val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
        @Json(name = "token")
        val token: String
)

@JsonClass(generateAdapter = true)
data class ErrorResponse(
        @Json(name = "error")
        val error: String
)

interface AuthApi {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/login")
    suspend fun postLogin(@Body authRequest: AuthRequest): Response<AuthResponse>
}