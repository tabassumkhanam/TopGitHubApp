package com.appstreet.topgithubapp.di

import android.app.Application
import android.content.Context
import com.appstreet.topgithub.utils.AppConstants.Companion.HTTP_CONNECT_TIMEOUT
import com.appstreet.topgithub.utils.AppConstants.Companion.HTTP_READ_TIMEOUT
import com.appstreet.topgithubapp.imagecachelib.ImageHelper
import com.appstreet.topgithubapp.model.TrendingDevRepository
import com.appstreet.topgithubapp.networkcall.NetworkCallInterface
import com.appstreet.topgithubapp.utilities.InternetConnectionManager
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Singleton
    @Provides
    fun providesApplicationContext(app: Application): Context {
        return app
    }

    @Singleton
    @Provides
    internal fun provideRxJava2CallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return interceptor
    }

    @Singleton
    @Provides
    internal fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }


    @Provides
    @Named("BaseURL")
    fun providesBaseURL(): String {
        return "https://github-trending-api.now.sh/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("BaseURL") baseURL: String, jsonConverter: Converter.Factory,
        callAdapter: CallAdapter.Factory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(jsonConverter)
            .addCallAdapterFactory(callAdapter)
            .baseUrl(baseURL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideNetworkCallInterface(retrofit: Retrofit): NetworkCallInterface {
        return retrofit.create(NetworkCallInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideTrendingDevRepository(networkCallInterface: NetworkCallInterface): TrendingDevRepository {
        return TrendingDevRepository(networkCallInterface)
    }


    @Provides
    @Singleton
    fun providesImageLibX(context: Context): ImageHelper {
        return ImageHelper(context)
    }

    @Provides
    @Singleton
    fun provideInternetConnectionManager(context: Context): InternetConnectionManager {
        return InternetConnectionManager(context)
    }

}