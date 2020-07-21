package com.wix.transcoder

import com.otaliastudios.transcoder.Transcoder
import com.otaliastudios.transcoder.TranscoderListener
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy
import com.wix.core.AbsVideoCompressor
import com.wix.core.VideoCompressor
import java.lang.Exception

class TranscoderVideoCompressor(
    callback: VideoCompressor.CompressionCallback
) : AbsVideoCompressor(callback) {
    override fun provideLibraryName() = "Transcoder"

    override fun doVideoCompression(request: VideoCompressor.VideoCompressionRequest) {
        val listener = object : TranscoderListener {
            override fun onTranscodeCompleted(successCode: Int) {
                videoCompressionComplete(request)
            }

            override fun onTranscodeProgress(progress: Double) {
            }

            override fun onTranscodeCanceled() {
            }

            override fun onTranscodeFailed(exception: Throwable) {
                videoCompressionFailed(request, exception)
            }
        }
        try {
            Transcoder.into(request.outputFilePath)
                .addDataSource(request.inputFilePath)
                .setVideoTrackStrategy(DefaultVideoStrategy.atMost(720, 1280).build())
                .setListener(listener)
                .transcode()
        } catch (e: Exception) {
            videoCompressionFailed(request, e)
        }
    }
}