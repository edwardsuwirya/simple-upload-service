package com.enigmacamp.mysimpleupload.data.api

import com.enigmacamp.mysimpleupload.data.model.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostApi {
    @Multipart
    @POST("posts/upload")
    suspend fun upload(
        @Part("id") id: RequestBody,
        @Part idCard: MultipartBody.Part
    ): Response<Post>
}