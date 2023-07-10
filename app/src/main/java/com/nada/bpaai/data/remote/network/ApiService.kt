package com.nada.bpaai.data.remote.network

import com.nada.bpaai.data.remote.response.AddNewStoryResponse
import com.nada.bpaai.data.remote.response.ListStoryItem
import com.nada.bpaai.data.remote.response.LoginResponse
import com.nada.bpaai.data.remote.response.RegisterResponse
import com.nada.bpaai.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ):Call<AddNewStoryResponse>

    @GET("stories")
    fun getLocationStory(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ): Call<StoriesResponse>

}