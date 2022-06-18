package com.asynctaskcoffee.voicerecorder

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GetResponse {


    @Headers(
        "pid-APIKey: 6B5EBBDAFB9D92DF35F3FF2453976012DCEE629DE8ED465F1CA6F66294CB169A",
        "site: Speaker_Demo_App"
    )


    @GET("/VoiceVerification/")
    fun uploadFile(

    ): Call<UploadResponse>

    companion object{
        operator fun invoke() : GetResponse{
            return Retrofit.Builder()
                .baseUrl("https://api.presentid.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GetResponse::class.java)
        }
    }
}