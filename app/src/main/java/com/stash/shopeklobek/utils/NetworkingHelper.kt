package com.stash.shopeklobek.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*

object NetworkingHelper {
    fun hasInternet(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (t: Throwable) {
            return false
        }
    }
}