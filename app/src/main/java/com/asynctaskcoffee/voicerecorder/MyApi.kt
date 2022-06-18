package com.asynctaskcoffee.voicerecorder

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {


    @Headers(
        "pid-APIKey: 6B5EBBDAFB9D92DF35F3FF2453976012DCEE629DE8ED465F1CA6F66294CB169A",
        "site: Speaker_Demo_App"
    )

    @Multipart
    @POST("/VoiceVerification/")
    fun uploadFile(
        @Part sound1: MultipartBody.Part,
        @Part sound2: MultipartBody.Part
    ):Call<ResponseBody>

    companion object{
        operator fun invoke() : MyApi{
            return Retrofit.Builder()
                .baseUrl("https://api.presentid.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}