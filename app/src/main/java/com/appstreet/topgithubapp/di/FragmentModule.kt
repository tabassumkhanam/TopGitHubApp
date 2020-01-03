package com.appstreet.topgithubapp.di

import com.appstreet.topgithubapp.view.fragment.TrendingDevDetailFragment
import com.appstreet.topgithubapp.view.fragment.TrendingDevelopersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributesTrendingDevelopersFragment(): TrendingDevelopersFragment

    @ContributesAndroidInjector
    abstract fun contributesDeveloperDetailFragment(): TrendingDevDetailFragment

}