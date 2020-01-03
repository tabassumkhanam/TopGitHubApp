package com.appstreet.topgithubapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appstreet.topgithubapp.view.viewmodel.TrendingDevViewModel
import com.appstreet.topgithubapp.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrendingDevViewModel::class)
    internal abstract fun bindTrendingDeveloperViewModel(listViewModel: TrendingDevViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
