package com.ducnn17.movieCGV.utils.core

import com.ducnn17.movieCGV.BuildConfig

object AppInfo {
    const val apiUrl = "https://api.themoviedb.org/3/"
    const val apiImage = "https://image.tmdb.org/t/p/original"
    fun getVersion() = BuildConfig.VERSION_NAME
    fun getAppId() = BuildConfig.APPLICATION_ID
}