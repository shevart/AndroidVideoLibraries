package com.wix.videocompressionsandbox.models

import android.content.Context
import com.wix.core.model.MediaFile

//import com.iceteck.silicompressorr.SiliCompressor
//import com.wix.videocompressionsandbox.models.MediaThumbedItem.Companion.getVideoUri

class SiliVideoCompressor {
    fun compressVideo(context: Context, video: MediaFile, outputPath: String): String {
        return ""
//        return SiliCompressor.with(context)
//            .compressVideo(video.getVideoUri(), outputPath, video.width, video.height, 0)
    }
}