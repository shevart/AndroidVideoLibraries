package com.wix.core.model

//640x480
//960x540
//1280x720
//1920x1080
//3840x2160

sealed class VideoScaleFactor

object OriginVideoScale: VideoScaleFactor()

object P3840VideoScale: VideoScaleFactor()
object P1920VideoScale: VideoScaleFactor()
object P1280VideoScale: VideoScaleFactor()
object P960VideoScale: VideoScaleFactor()
object P640VideoScale: VideoScaleFactor()