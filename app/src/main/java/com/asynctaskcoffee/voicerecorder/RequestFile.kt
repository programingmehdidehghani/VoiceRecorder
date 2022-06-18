package com.asynctaskcoffee.voicerecorder

import java.io.File

data class RequestFile(
    val sound1: File,
    val sound2: File
)
