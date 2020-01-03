package com.appstreet.topgithubapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.appstreet.topgithubapp.R
import com.appstreet.topgithubapp.model.TrendingDevRepository
import com.appstreet.topgithubapp.model.TrendingDeveloperModel
import com.appstreet.topgithubapp.networkcall.Resource
import com.appstreet.topgithubapp.utilities.InternetConnectionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TrendingDevViewModel @Inject constructor(val repository: TrendingDevRepository) :
    ViewModel() {
    @Inject
    lateinit var internetConnectionManager: InternetConnectionManager

    private val disposables = CompositeDisposable()
    private val developersLiveData = MutableLiveData<Resource<List<TrendingDeveloperModel>>>()

    /**
     * method to get trending developers list as live data
     * @return trending developers live data
     */
    fun getTrendingDevelopersList(): LiveData<Resource<List<TrendingDeveloperModel>>> {
        return developersLiveData
    }

    /**
     * method to call trending developers list api
     * @param language - language of developers list
     * @param since - monthly or weekly or daily etc
     */
    fun callTrendingDevelopersRepositoryApi(language: String, since: String) {
        if (internetConnectionManager.isNetworkAvailable()) {
            developersLiveData.value = Resource.Loading(null)
            disposables.add(repository.executeTrendingDevRepositoryApi(language, since)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { d -> developersLiveData.setValue(Resource.Loading(null)) }
                .subscribe(
                    { result -> developersLiveData.setValue(Resource.Success(result)) },
                    { throwable ->
                        developersLiveData.setValue(
                            Resource.Error(throwable.message ?: "Unknown Error")
                        )
                    }
                ))
        } else {
            developersLiveData.setValue(
                Resource.InternetError(R.string.internet_connection_error_message)
            )
        }
    }

    /**
     * Clear all observers that added in composite disposable
     */
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}