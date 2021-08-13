package com.enigmacamp.mysimpleupload.data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3008/enigma/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val postApi: PostApi by lazy {
        retrofit.create(PostApi::class.java)
    }
}