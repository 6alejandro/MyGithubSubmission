package com.example.mygithubsubmission.data.remote

import com.example.mygithubsubmission.BuildConfig
import com.example.mygithubsubmission.data.model.Item
import com.example.mygithubsubmission.data.model.ResponseDetailUser
import com.example.mygithubsubmission.data.model.ResponseUserGithub
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUser(
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): ResponseDetailUser

    @JvmSuppressWildcards
    @GET("/users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<Item>

    @JvmSuppressWildcards
    @GET("/users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun getSearchUser(
        @QueryMap params: Map<String, Any>,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): ResponseUserGithub
}