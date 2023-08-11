package com.example.meme_share.network

import retrofit2.http.GET

interface ApiInterface {

    @GET("/gimme")
    fun getData(): retrofit2.Call<responseDataClass>
}