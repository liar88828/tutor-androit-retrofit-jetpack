package com.example.compose_app.utils

import com.example.compose_app.data.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val api: ApiInterface by lazy {
        Retrofit
            .Builder()
            .baseUrl(Urls.Base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}