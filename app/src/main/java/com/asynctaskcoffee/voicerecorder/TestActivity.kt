package com.asynctaskcoffee.voicerecorder

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.asynctaskcoffee.audiorecorder.worker.AudioRecordListener
import com.asynctaskcoffee.audiorecorder.worker.MediaPlayListener
import com.asynctaskcoffee.audiorecorder.worker.Player
import com.asynctaskcoffee.audiorecorder.worker.Recorder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TestActivity : AppCompatActivity(), AudioRecordListener, MediaPlayListener ,UploadRequestBody.UploadCallBack{

    lateinit var recorder: Recorder
    lateinit var player: Player
    private var selectFile : Uri? = null
    private var fileName: String? = null
    private lateinit var names: Array<String>

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                , Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        var list = arrayListOf<String>()
        list.add("Paris was founded about 1825, was incorporated in 1853, and was re-incorporated in 1873.")
        list.add("The first printed edition appeared in London in 1553.")
        list.add("There is a public park, besides bowling-greens and cricket and football fields.53.")
        list.add("Through the whole of this tract the letters are used which are common to Persian, Arabic and Turkish, written from right to left.")
        list.add("His particular research interests were in Artificial Intelligence applied to advanced automation.")
        list.add("The car warmed quickly and she fell asleep again.")
        list.add("They would have some time to enjoy a late Christmas at home when they returned.")
        list.shuffle()
        tv_record_one.text = list[0]
        tv_record_one2.text = list[1]
    }

    fun startRecord(view: View) {
        recorder = Recorder(this,this)
        iv_start_record.isEnabled = false
        recorder.startRecord()
        iv_stop_record.isEnabled = true
        //playRecordButton.visibility = GONE
    }

    fun stopRecord(view: View) {
        iv_stop_record.isEnabled = false
        recorder.stopRecording()
        iv_start_record.isEnabled = true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun sendFileFirst(view: View){
        var listFile = arrayListOf<String>()
        if (Recorder.listFiles.size == 0){
            Toast.makeText(this,"Please Record Voice",Toast.LENGTH_SHORT).show()
            return
        }else if (Recorder.listFiles.size == 1){
            Toast.makeText(this,"Please Record Second Voice",Toast.LENGTH_SHORT).show()
            return
        }
        listFile.add(Recorder.listFiles[0])
        listFile.add(Recorder.listFiles[1])


        val file1 = File(listFile[0])
        val file2 = File(listFile[1])

        val requestFile1: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),file1)
        val requestFile2: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),file2)
        val multipartBody1 = MultipartBody.Part.createFormData("sound1", file1.name, requestFile1)
        val multipartBody2 = MultipartBody.Part.createFormData("sound2", file2.name, requestFile2)


        val responseBodyCall: Call<ResponseBody> =
            MyApi().uploadFile(multipartBody1,multipartBody2)

        responseBodyCall.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                val responseBody = response.body()!!
                Log.d("Success", "success " + response.code())
                Log.d("Success", "success " + response.message())
                Log.d("Success", "success " + response.body())
                Log.i("tag", "response is success  $responseBody")

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.d("Success", "fail  = " + t.message)
            }
        })
        listFile.clear()
        val file = File("/storage/emulated/0/Android/data/com.asynctaskcoffee.voicerecorder/files")
        if(file.exists())
            file.delete();

        val responseBodyCall1: Call<UploadResponse> =
            GetResponse().uploadFile()


      /*  responseBodyCall1.enqueue(object : Callback<UploadResponse?> {
            override fun onResponse(call: Call<UploadResponse?>, response: Response<UploadResponse?>) {
                Log.d("Success1", "success " + response.code())
                Log.d("Success1", "success " + response.message())
                Log.d("Success1", "success " + response.body())
                val post = JsonObject()[response.body().toString()].asJsonObject
            }

            override fun onFailure(call: Call<UploadResponse?>, t: Throwable) {
                Log.d("Success1", "fail  = " + t.message)
            }
        })*/
    }


    override fun onRecordFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onReadyForRecord() {

    }

    override fun onAudioReady(audioUri: String?) {
        Toast.makeText(this, audioUri, Toast.LENGTH_SHORT).show()
        iv_start_record.isEnabled = true
        iv_stop_record.isEnabled = true
        player = Player(this)
        player.injectMedia(audioUri)
    }

    fun playRecord(view: View) {
        if (player.player!!.isPlaying)
            player.stopPlaying()
        else player.startPlaying()
    }

    @SuppressLint("SetTextI18n")
    override fun onStopMedia() {
    }

    override fun onStartMedia() {
    }

    override fun onProgressUpdate(progress: Int) {
        TODO("Not yet implemented")
    }
}




