package com.wix.videocompressionsandbox.models

import android.database.Cursor
import android.provider.MediaStore
import com.wix.core.model.MediaFile
import com.wix.videocompressionsandbox.VideoLoader

class MediaThumbCreator(private val imageCursor: Cursor) {
    private val idColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[0])
    private val uriColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[1])
    private val bucketColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[2])
    private val typeColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[3])
    private val widthColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[4])
    private val heightColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[5])
    private val dateAddedColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[6])
    private val durationColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[7])
    private val mimeTypeColumn = imageCursor.getColumnIndex(VideoLoader.PROJECTION[8])


    /**
     * create media thumb item from the current cursor place and return
     * @throws IllegalStateException
     * @throws KotlinNullPointerException
     * @throws NullPointerException
     * the exceptions thrown by the cursor when the requested column have null values or
     * the column does not exist in the device which is odd but mainly for null values
     */
    fun create(): MediaFile {
        val id = imageCursor.getLong(idColumn)
        val uri = imageCursor.getString(uriColumn)
        val type = imageCursor.getInt(typeColumn)
        val duration = imageCursor.getLong(durationColumn)
        val width = imageCursor.getInt(widthColumn)
        val height = imageCursor.getInt(heightColumn)
        return MediaFile(
            id = id,
            isVideo = (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
            uri = uri,
            videoDuration = duration,
            width = width,
            height = height
        )
    }

    companion object {
//        @WorkerThread
//        fun fromPath(uri: String): MediaThumbedItem {
//            val validUri = uri.replace("file://", "")
//            val duration = -1L
//            val isVideo = false
//
//            return MediaThumbedItem(
//                isVideo = isVideo,
//                uri = uri,
//                videoDuration = duration
//            )
//        }

//        private fun getImageDims(uri: String): Pair<Int, Int> {
//            val bounds = BitmapFactory.Options()
//            bounds.inJustDecodeBounds = true
//            val decodeFile = BitmapFactory.decodeFile(uri, bounds)
//            val pair = bounds.outWidth to bounds.outHeight
//            decodeFile?.recycle()
//            return pair
//        }
//
//
//        fun File.getMimeType(): String? {
//            val extensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(this.absolutePath)
//            if (extensionFromUrl != null) {
//                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionFromUrl)
//            }
//            return null
//        }

    }
}