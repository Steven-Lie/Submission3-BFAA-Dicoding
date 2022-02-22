package com.dicoding.githubusers.api

import com.dicoding.githubusers.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getUsers(): Call<List<UsersData>>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getUserSearched(
        @Query("q") q: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UsersData>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UsersData>>
}