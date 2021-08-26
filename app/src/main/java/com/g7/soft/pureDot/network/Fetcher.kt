package com.g7.soft.pureDot.network

import android.util.Log
import com.g7.soft.pureDot.constant.ApiConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


class Fetcher {

    private var arService: ApiService? = null
    private var enService: ApiService? = null

    //Singleton Pattern
    fun getInstance(lang: String): ApiService? {
        return if (lang.equals("en", ignoreCase = true)) {
            if (enService == null) enService =
                createService(lang)
            enService
        } else {
            if (arService == null) arService =
                createService(lang)
            arService
        }
    }

    private fun createService(lang: String?): ApiService {
        val httpClint = OkHttpClient.Builder()

        // TODO change timeout values
        httpClint.connectTimeout(120, TimeUnit.SECONDS)
        httpClint.readTimeout(120, TimeUnit.SECONDS)
        httpClint.writeTimeout(120, TimeUnit.SECONDS)

        httpClint.addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("apikey", "puredot2021")
                .addHeader("lang", lang ?: "en")
                .addHeader("Content-Type", " application/json")
                .addHeader("DevicePlatform", "0")
                /*.addHeader( // todo timestamp as api?
                    "CurrentDate",
                    SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale("en"))
                        .format(Date())
                )*/
                .build()

            val response = chain.proceed(request)

            Log.e("Z_", "request: $request")
            Log.e("Z_", "response: $response - ${response.peekBody(2048).string()}")

            if (!response.isSuccessful)
                Timber.e("Error loading request: %s", request.toString())
            response
        }

        // todo delete mock interceptor
        //httpClint.addInterceptor(MockInterceptor())

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
            .client(client)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(ApiConstant.BASE_URL)
            .client(httpClint.build())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}