package com.asynctaskcoffee.audiorecorder.worker

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.util.*

class Recorder(audioRecordListener: AudioRecordListener?, private var context: Context?) {

    private var recorder: MediaRecorder? = null
    private var audioRecordListener: AudioRecordListener? = null
    private var fileName: String? = null
    private var localPath = ""


    private var isRecording = false

    companion object{
        public var uriAll : String? = null
        public var listFiles = arrayListOf<String>()

    }

    fun setFileName(fileName: String?) {
        this.fileName = fileName
    }

    fun getFileName(): String? {
        return fileName
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun startRecord() {
        if (context == null) {
            throw IllegalStateException("Context cannot be null")
        }
        val destPath: String = context?.getExternalFilesDir(null)?.absolutePath ?: ""
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        localPath = destPath
        localPath += if (fileName == null) {
            "/Recorder_" + UUID.randomUUID().toString() + ".m4a"
        } else {
            fileName
        }
        recorder?.setOutputFile(localPath)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

        try {
            recorder?.prepare()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            reflectError(e.toString())
            return
        }
        recorder?.start()
        isRecording = true
        audioRecordListener?.onReadyForRecord()
    }

    fun reset() {
        if (recorder != null) {
            recorder?.release()
            recorder = null
            isRecording = false
        }
    }

    fun stopRecording() {
        try {
            Thread.sleep(150)
            recorder?.stop()
            recorder?.release()
            recorder = null
            reflectRecord(localPath)
        } catch (e: Exception) {
            e.printStackTrace()
            reflectError(e.toString())
        }
    }

    fun sendFile(){
        if (context == null) {
            throw IllegalStateException("Context cannot be null")
        }
        val destPath: String = context?.getExternalFilesDir(null)?.absolutePath ?: ""
        localPath = destPath
        localPath += if (fileName == null) {
            "/Recorder_" + UUID.randomUUID().toString() + ".m4a"
        } else {
            fileName
        }
        try {
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            reflectError(e.toString())
            return
        }
        audioRecordListener?.onReadyForRecord()


    }

    private fun reflectError(error: String?) {
        audioRecordListener?.onRecordFailed(error)
        isRecording = false
    }

    private fun reflectRecord(uri: String?) {
        audioRecordListener?.onAudioReady(uri)
        if (listFiles.size != 0){
            listFiles.add(uri.toString())
        }else{
            listFiles.add(uri.toString())
        }
        isRecording = false
    }

    init {
        this.audioRecordListener = audioRecordListener
    }
}