package com.appstreet.topgithubapp.networkcall

import com.appstreet.topgithubapp.model.TrendingDeveloperModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkCallInterface {

    @GET("developers")
    fun getTrendingRepositories(@Query("language") language: String, @Query("since") since: String): Observable<List<TrendingDeveloperModel>>

}