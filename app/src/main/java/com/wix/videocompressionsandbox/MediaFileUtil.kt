package com.wix.videocompressionsandbox

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

const val MP4 = "mp4"
const val JPEG = "jpg"

const val VIDEO_PREFIX = "video"
const val IMG_PREFIX = "img"

const val WIX_FOLDER = "wix_"

const val ALBUM_WIX_VIDEOS = "WixCompression"
const val ALBUM_WIX_IMAGES = "Wix Images"

fun createTempMediaFile(context: Context, isVideo: Boolean, originFileName: String? = null): File {
    val fileName = createMediaFileName(isVideo, originFileName)
    val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        createMediaFileInAppSpecificFolder(context, fileName, isVideo)
    } else {
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName)
    }
    if (file.parentFile?.exists() == false) {
        file.parentFile?.mkdirs()
    }
    return file
}

private fun createMediaFileInAppSpecificFolder(
    context: Context,
    fileName: String,
    videoFile: Boolean
): File {
    val file = if (videoFile) {
        File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), fileName)
    } else {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
    }
    file.parentFile?.mkdirs()
    return file
}

private fun createMediaFileName(isVideo: Boolean, originFileName: String?): String {
    val directory = if (isVideo) ALBUM_WIX_VIDEOS else ALBUM_WIX_IMAGES
    val fileType = if (isVideo) MP4 else JPEG
    val prefix = if (isVideo) VIDEO_PREFIX else IMG_PREFIX

    return if (originFileName != null) {
        "_$originFileName"
    } else {
        "$directory/$WIX_FOLDER${prefix}_${System.currentTimeMillis()}.$fileType"
    }
}