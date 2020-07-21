package com.wix.litr

import android.content.Context
import android.media.MediaFormat
import android.net.Uri
import com.linkedin.android.litr.MediaTransformer
import com.linkedin.android.litr.TransformationListener
import com.linkedin.android.litr.analytics.TrackTransformationInfo
import com.wix.core.AbsVideoCompressor
import com.wix.core.VideoCompressor
import java.io.File

class LitrVideoCompressor(
    private val context: Context,
    callback: VideoCompressor.CompressionCallback
) :
    AbsVideoCompressor(callback) {
    private val mediaTransformer: MediaTransformer by lazy {
        MediaTransformer(context)
    }

    override fun provideLibraryName() = "Litr"

    override fun doVideoCompression(request: VideoCompressor.VideoCompressionRequest) {
        val requestId = System.currentTimeMillis().toString()
        val targetVideoFormat = MediaFormat()
        val targetAudioFormat = MediaFormat()
        val listener = object : TransformationListener {
            override fun onCancelled(
                id: String,
                trackTransformationInfos: MutableList<TrackTransformationInfo>?
            ) {

            }

            override fun onStarted(id: String) {
            }

            override fun onProgress(id: String, progress: Float) {
            }

            override fun onError(
                id: String,
                cause: Throwable?,
                trackTransformationInfos: MutableList<TrackTransformationInfo>?
            ) {
                videoCompressionFailed(request, cause)
            }

            override fun onCompleted(
                id: String,
                trackTransformationInfos: MutableList<TrackTransformationInfo>?
            ) {
                videoCompressionComplete(request)
            }
        }

        mediaTransformer.transform(
            requestId,
            Uri.fromFile(File(request.inputFilePath)),
            request.outputFilePath,
            null,
            null,
            listener,
            100,
            emptyList()
        )
    }
}