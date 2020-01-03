package com.appstreet.topgithubapp.model

import com.appstreet.topgithubapp.networkcall.NetworkCallInterface
import io.reactivex.Observable


class TrendingDevRepository(val networkCallInterface: NetworkCallInterface) {

    /**
     * method to call github trending developers list api
     * @param language - language of developers list
     * @param since - monthly or weekly or daily etc
     */
    fun executeTrendingDevRepositoryApi(language: String, since: String): Observable<List<TrendingDeveloperModel>> {
        return networkCallInterface.getTrendingRepositories(language, since)
    }

}