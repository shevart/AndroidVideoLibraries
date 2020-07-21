package com.wix.videocompressionsandbox

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.wix.androidtranscoder.AndroidTranscoderVideoCompressor
import com.wix.core.VideoCompressor
import com.wix.core.model.*
import com.wix.core.model.MediaFile.Companion.getVideoUri
import com.wix.ffmpeg.FFMpegVideoCompressor
import com.wix.litr.LitrVideoCompressor
import com.wix.transcoder.TranscoderVideoCompressor
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val callback = object : VideoCompressor.CompressionCallback {
        override fun updateProgress(text: String) {
            updateText(text)
        }

        override fun done(file: MediaFile) {
        }

        override fun failed(file: MediaFile, e: Throwable?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    startVideoCompression()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    finish()
                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun startVideoCompression() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            updateText("LOLLIPOP")
            return
        }

        Thread {
            val videos = VideoLoader.loadVideos(applicationContext)
            Log.e("", "videos count is: ${videos.size}")
            val video = videos
                .filter { !it.obtainFileName(it.uri).startsWith("_") }
                .maxBy { it.videoDuration }
            if (video == null) {
                updateText("video is null")
                return@Thread
            }

            compressVideo(video)
        }.start()
    }

    private fun updateText(msg: String) {
        handler.post {
            tvTime.text = msg
            Log.e("video", msg)
        }
    }

    private fun compressVideo(video: MediaFile) {
        val inputVideoPath = getFullPathFromContentUri(this, video.getVideoUri())!!
        val outputVideoPath = getVideoOutputFilePath(video)
        val request = VideoCompressor.VideoCompressionRequest(
            inputVideoPath, outputVideoPath, video
        )
//        val compressor: VideoCompressor = FFMpegVideoCompressor(callback)
        val compressor: VideoCompressor = TranscoderVideoCompressor(callback)
        compressor.compressVideo(request)
    }

    private fun getFullPathFromContentUri(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri, projection, null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    private fun getVideoOutputFilePath(video: MediaFile): String {
        val desFile = createTempMediaFile(
            context = this,
            isVideo = true,
            originFileName = video.obtainFileName(video.uri)
        )
        if (desFile.exists()) {
            desFile.delete()
            try {
                desFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return desFile.path
    }
}
