package com.example.geolocation.api

import android.content.Context
import android.content.pm.PackageManager
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import org.conscrypt.Conscrypt
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit

val <T> Response<T>.asError: ErrorResponse?
    get() = Moshi.Builder().build()
        .adapter(ErrorResponse::class.java)
        .fromJson(errorBody()?.string().orEmpty())

class NetworkService {
    private lateinit var retrofit: Retrofit

    private fun getRetrofit(context: Context): Retrofit {
        if (!::retrofit.isInitialized) {
            Security.insertProviderAt(Conscrypt.newProvider(), 1)

            val certFactory = CertificateFactory.getInstance("X.509")
            val certs = context.assets.list("certs").orEmpty()
                .map { certFactory.generateCertificate(context.assets.open("certs/$it")) as X509Certificate }

            val handshakeCerts = HandshakeCertificates.Builder()
                .addPlatformTrustedCertificates()
                .apply { certs.forEach(::addTrustedCertificate) }
                .build()

            val okHttpClient = OkHttpClient.Builder()
                .callTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .sslSocketFactory(handshakeCerts.sslSocketFactory(), handshakeCerts.trustManager())
                .build()

            val info = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA,
            )
            val apiUrl = "http://10.1.1.65:3000/api/"
            //val apiUrl = "http://localhost:3000/api/"

            retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
                .build()
        }

        return retrofit
    }

    private lateinit var authApi: AuthApi
    private lateinit var usersApi: UsersApi

    fun getAuthApi(context: Context): AuthApi {
        if (!::authApi.isInitialized)
            authApi = getRetrofit(context).create()

        return authApi
    }

    fun getUsersApi(context: Context): UsersApi {
        if (!::usersApi.isInitialized)
            usersApi = getRetrofit(context).create()

        return usersApi
    }
}