package com.wix.videocompressionsandbox

import android.content.Context
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.wix.videocompressionsandbox.models.MediaThumbCreator
import com.wix.core.model.MediaFile

object VideoLoader {
    @WorkerThread
    fun loadVideos(cxt: Context): List<MediaFile> {
        val videos = mutableListOf<MediaFile>()
        cxt.contentResolver.query(EXTERNAL_URI, PROJECTION, MEDIA_SELECTION, null, ORDER)
            .use { imagesCursor ->
                if (imagesCursor?.moveToFirst() == true) {
                    val mediaThumbCreator = MediaThumbCreator(imagesCursor)
                    do {
                        try {
                            val mediaThumb = mediaThumbCreator.create()
                            videos += mediaThumb
                        } catch (e: Exception) {
                        }
                    } while (imagesCursor.moveToNext())
                }
            }

        return videos.filter { it.isVideo }
    }

    private val EXTERNAL_URI = MediaStore.Files.getContentUri("external")
    val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.WIDTH,
        MediaStore.Files.FileColumns.HEIGHT,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Video.VideoColumns.DURATION,
        MediaStore.Files.FileColumns.MIME_TYPE
    )

    private const val MEDIA_SELECTION = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    private const val ORDER =
        "${MediaStore.Images.Media.DATE_ADDED} DESC, ${MediaStore.Files.FileColumns._ID} DESC"
}

