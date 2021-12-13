package com.ducnn17.movieCGV.receiver

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

object PermissionUtils {
    fun requestPermission(activity: Activity?, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity!!, permissions, requestCode)
    }

    fun checkPermission(context: Context?, permissions: Array<String>): Boolean {
        var isValid = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                isValid = false
                break
            }
        }
        return isValid
    }
}