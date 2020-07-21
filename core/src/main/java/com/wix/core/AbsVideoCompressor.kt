package com.wix.core

abstract class AbsVideoCompressor(
    val callback: VideoCompressor.CompressionCallback
) : VideoCompressor {
    private var startTime = 0L

    final override fun compressVideo(request: VideoCompressor.VideoCompressionRequest) {
        startTime = System.currentTimeMillis() / 1000
        callback.updateProgress(
            text = provideLibraryName() + "\n\n" +
                    "Input: ${request.inputFilePath}\n\n" +
                    "Output: ${request.outputFilePath}\n\n"
        )
        doVideoCompression(request)
    }

    protected abstract fun provideLibraryName(): String

    protected abstract fun doVideoCompression(request: VideoCompressor.VideoCompressionRequest)

    protected fun videoCompressionComplete(request: VideoCompressor.VideoCompressionRequest) {
        val endTime = System.currentTimeMillis() / 1000
        callback.updateProgress(
            text = provideLibraryName() + "\n\n" +
                    "Compression time is: ${endTime - startTime} seconds\n" +
                    "Video duration is: ${request.file.videoDuration / 1000} seconds\n\n" +
                    "Input: ${request.inputFilePath}\n\n" +
                    "Output: ${request.outputFilePath}"
        )
    }

    protected fun videoCompressionFailed(
        request: VideoCompressor.VideoCompressionRequest,
        e: Throwable? = null
    ) {
        callback.updateProgress("onFailure :(")
        callback.failed(request.file, e)
    }
}