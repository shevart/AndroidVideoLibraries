package com.wix.core

import com.wix.core.model.MediaFile

interface VideoCompressor {
    fun compressVideo(request: VideoCompressionRequest)

    interface CompressionCallback {
        fun updateProgress(text: String)

        fun done(file: MediaFile)

        fun failed(file: MediaFile, e: Throwable? = null)
    }

    data class VideoCompressionRequest(
        val inputFilePath: String,
        val outputFilePath: String,
        val file: MediaFile
    )
}