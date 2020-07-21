package com.wix.core.model

open class MFile(
    initUri: String,
    var editedUri: String? = null
) {
    var uri: String = initUri
}