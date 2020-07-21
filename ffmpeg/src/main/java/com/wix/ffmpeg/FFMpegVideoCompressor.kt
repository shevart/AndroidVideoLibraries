package com.wix.ffmpeg

import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.wix.core.AbsVideoCompressor
import com.wix.core.VideoCompressor
import com.wix.core.model.*

class FFMpegVideoCompressor(callback: VideoCompressor.CompressionCallback) :
    AbsVideoCompressor(callback) {
    override fun provideLibraryName() = "FFMpeg"

    override fun doVideoCompression(request: VideoCompressor.VideoCompressionRequest) {
        val inputVideoPathCommand = "-i \"file://${request.inputFilePath}\""
        val outputVideoPathCommand = "\"file://${request.outputFilePath}\""
        val replaceExistingFileCommand = "-y"
        val scaleVideoCommand = calculateNewWidthHeightCommand(
            scaleFactor = P640VideoScale,
            originWidth = request.file.width,
            originHeight = request.file.height
        )
        val cmdCompressVideo =
            "$inputVideoPathCommand $replaceExistingFileCommand $scaleVideoCommand $outputVideoPathCommand"
        callback.updateProgress(cmdCompressVideo)


        val response = FFmpeg.execute(cmdCompressVideo)
        if (response == Config.RETURN_CODE_SUCCESS) {
            videoCompressionComplete(request)
        } else {
            Config.printLastCommandOutput(Log.INFO);
            videoCompressionFailed(request)
        }
    }

    private fun calculateNewWidthHeightCommand(
        scaleFactor: VideoScaleFactor,
        originWidth: Int,
        originHeight: Int
    ): String {
        if (scaleFactor == OriginVideoScale) {
            return ""
        }

        val maxVideoSide = when (scaleFactor) {
            P3840VideoScale -> 3840
            P1920VideoScale -> 1920
            P1280VideoScale -> 1280
            P960VideoScale -> 960
            P640VideoScale -> 640
            OriginVideoScale -> throw RuntimeException()
        }

        return if (originWidth > originHeight) {
            "-vf scale=-1:$maxVideoSide"
        } else {
            "-vf scale=$maxVideoSide:-1"
        }
    }
}