package com.enigmacamp.mysimpleupload.data.repository

import com.enigmacamp.mysimpleupload.data.api.PostApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostRepository(private val postApi: PostApi) {
    suspend fun uploadFile(imageFile: File, postId: Int): Boolean {
        val requestFile =
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                imageFile
            )
        val image =
            MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestFile
            )
        val post = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            postId.toString()
        )

        val result = postApi.upload(post, image)
        return result.isSuccessful
    }
}