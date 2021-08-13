package com.enigmacamp.mysimpleupload.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

object MediaPath {
    fun getRealPathFromURI(contentResolver: ContentResolver, contentUri: Uri): String? {
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(idx)
            cursor.close()
            path
        }
    }
}