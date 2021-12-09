package com.ducnn17.movieCGV.utils.core

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtils {
    companion object {
        @SuppressLint("MissingPermission")
        fun isConnected(context: Context): Boolean {
            val cm: ConnectivityManager? =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm?.activeNetworkInfo
            return null != activeNetwork && activeNetwork.isConnected
        }
    }

}
