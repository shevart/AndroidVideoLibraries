package com.wix.androidtranscoder

import com.wix.core.AbsVideoCompressor
import com.wix.core.VideoCompressor
import net.ypresto.androidtranscoder.MediaTranscoder
import net.ypresto.androidtranscoder.format.MediaFormatStrategyPresets
import java.lang.Exception

class AndroidTranscoderVideoCompressor(
    callback: VideoCompressor.CompressionCallback
): AbsVideoCompressor(callback) {

    override fun provideLibraryName() = "AndroidTranscoder"

    override fun doVideoCompression(request: VideoCompressor.VideoCompressionRequest) {
        MediaTranscoder.getInstance().transcodeVideo(
            request.inputFilePath,
            request.outputFilePath,
            MediaFormatStrategyPresets.createExportPreset960x540Strategy(),
            object : MediaTranscoder.Listener {
                override fun onTranscodeCompleted() {
                    videoCompressionComplete(request)
                }

                override fun onTranscodeProgress(progress: Double) {

                }

                override fun onTranscodeCanceled() {

                }

                override fun onTranscodeFailed(exception: Exception?) {
                    videoCompressionFailed(request, exception)
                }
            }
        )
    }
}