package com.ducnn17.movieCGV.receiver

import android.Manifest

object Constant {
    val PERMISSION = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    const val REQUEST_CODE_PERMISSION = 0
    const val REQUEST_CODE_CAMERA = 1
    const val REQUEST_CODE_PICK = 2

    const val USER_AVATAR_KEY = "user_avatar_key"
    const val USER_NAME_KEY = "user_name_key"
    const val USER_EMAIL_KEY = "user_email_key"
    const val USER_BIRTHDAY_KEY = "user_birthday_key"
    const val USER_GENDER_KEY = "user_gender_key"

    const val PREFERENCE_CATEGORY_KEY = "movie_category"
    const val PREFERENCE_RATE_KEY = "movie_rate_from"
    const val PREFERENCE_RELEASE_YEAR_KEY = "movie_release_year"
    const val PREFERENCE_SORT_KEY = "movie_sort"

}