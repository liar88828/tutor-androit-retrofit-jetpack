package com.example.compose_app.data

import com.example.compose_app.models.CatFacts
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/fact")
//    hati hati harus di beri suspense artinya asynchronous
    suspend fun getCatFacts(): Response<CatFacts>
}
