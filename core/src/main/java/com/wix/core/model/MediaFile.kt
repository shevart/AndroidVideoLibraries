package com.wix.core.model

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import java.io.File

open class MediaFile(
    val id: Long,
    var isVideo: Boolean,
    uri: String,
    var videoDuration: Long = 0L,
    var videoUrl: String? = null,
    val width: Int = 0,
    val height: Int = 0
) : MFile(uri) {
    private var localFile: File? = null


    // open for test only
    open fun asFile(): File {
        return if (localFile != null) {
            if (this.editedUri != null && localFile?.absolutePath != editedUri) {
                generateFile()
            }
            localFile!!
        } else {
            generateFile()
            localFile!!
        }
    }

    private fun generateFile() {
        localFile = if (this.editedUri == null) {
            File(uri)
        } else {
            File(Uri.parse(editedUri).path)
        }
    }

    fun obtainFileName(localUri: String): String {
        return if (localUri.contains('/') && localUri.contains('.')) {
            localUri.substring(localUri.lastIndexOf('/') + 1)
        } else {
            localUri
        }
    }

    companion object {
        fun MediaFile.getVideoUri(): Uri {
            return ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                this.id
            )
        }
    }
}