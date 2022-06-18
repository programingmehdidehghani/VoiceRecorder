package com.asynctaskcoffee.voicerecorder

data class UploadResponse(
    val statusCode : String,
    val statusMessage : String,
    val hasError : String,
    val data : String
) {
}