package com.appstreet.topgithubapp.utilities

import android.content.Context
import android.net.ConnectivityManager

class InternetConnectionManager(val context: Context) {

    /**
     * get Internet connectivity status
     * @return true or false boolean value
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}