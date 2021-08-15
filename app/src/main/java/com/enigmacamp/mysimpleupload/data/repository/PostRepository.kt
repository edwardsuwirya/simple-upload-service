package com.enigmacamp.mysimpleupload.data.repository

import com.enigmacamp.mysimpleupload.data.api.PostApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostRepository(private val postApi: PostApi) {
    suspend fun uploadFile(imageFiles: List<File?>, postId: Int): Boolean {
        val post = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            postId.toString()
        )
        var i = 0
        val multiPartImages = ArrayList<MultipartBody.Part>()
        for (file in imageFiles) {
            i++
            file?.let {
                multiPartImages.add(prepareMultiParts(file, "Image_$i"))
            }
        }
        val result = postApi.upload(post, multiPartImages)
        return result.isSuccessful
    }

    private fun prepareMultiParts(imageFile: File, partName: String): MultipartBody.Part {
        val requestFile =
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                imageFile
            )
        return MultipartBody.Part.createFormData(
            partName,
            imageFile.name,
            requestFile
        )
    }
}