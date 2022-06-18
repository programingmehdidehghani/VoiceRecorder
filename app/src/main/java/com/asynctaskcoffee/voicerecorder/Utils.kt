package com.asynctaskcoffee.voicerecorder

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun ContentResolver.getFileName(uri:Uri): String {
    var name = ""
    var cursor = query(uri,null,null,null,null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name

}