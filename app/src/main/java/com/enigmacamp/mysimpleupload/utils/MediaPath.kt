package com.enigmacamp.mysimpleupload.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log

object MediaPath {
    fun getRealPathFromURI(contentResolver: ContentResolver, contentUri: Uri): String? {
        Log.d("MainActivity", "getUriAuthority: ${contentUri.authority}")
        var cursor: Cursor? = null
        if ("com.android.providers.media.documents".equals("${contentUri.authority}")) {
            val docId = DocumentsContract.getDocumentId(contentUri)
            val sel = MediaStore.Images.Media._ID + "=?"
            val split = docId.split(":")
            cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                sel,
                arrayOf(split[1]),
                null
            )
        } else if ("com.android.providers.downloads.documents".equals("${contentUri.authority}")) {
            val id = DocumentsContract.getDocumentId(contentUri)
            if (id.startsWith("raw:")) {
                return id.replaceFirst("raw:", "");
            }
            val downloadUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), id.toLong()
            )
            cursor = contentResolver.query(
                downloadUri,
                null,
                null,
                null,
                null
            )

        } else {
            cursor = contentResolver.query(contentUri, null, null, null, null)
        }

        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(idx)
            Log.d("MainActivity", "getRealPathFromURI: $path")
            cursor.close()
            path
        }
    }
}
//content://com.android.providers.media.documents/document/image%3A81 => error
//content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F80/ORIGINAL/NONE/1194803451