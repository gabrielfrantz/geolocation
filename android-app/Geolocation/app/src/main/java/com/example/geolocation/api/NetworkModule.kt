package com.example.geolocation.api

import android.content.Context
import android.content.pm.PackageManager

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@InstallIn(ActivityRetainedComponent::class)
@Module
object RetrofitNetworkModule {
    @ActivityRetainedScoped
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {

        //Security.insertProviderAt(Conscrypt.newProvider(), 1)

        /*val certFactory = CertificateFactory.getInstance("X.509")
        val certs = context.assets.list("certs").orEmpty()
            .map { certFactory.generateCertificate(context.assets.open("certs/$it")) as X509Certificate }

        val handshakeCerts = HandshakeCertificates.Builder()
            .addPlatformTrustedCertificates()
            .apply { certs.forEach(::addTrustedCertificate) }
            .build()*/

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .build()

                chain.proceed(request)
            }
            .callTimeout(0, TimeUnit.MILLISECONDS)
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
            //.sslSocketFactory(handshakeCerts.sslSocketFactory(), handshakeCerts.trustManager())
            .build()

        client.dispatcher().maxRequestsPerHost = client.dispatcher().maxRequests

        val moshi = Moshi.Builder().build()

        val converterFactory = MoshiConverterFactory
            .create(moshi)
            .withNullSerialization()

        val info = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA,
        )
        //val apiUrl = "http://10.1.1.65:3000/api/"
        val apiUrl = "http://localhost:3000/api/"

        return Retrofit
            .Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()

    @ActivityRetainedScoped
    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create()
}