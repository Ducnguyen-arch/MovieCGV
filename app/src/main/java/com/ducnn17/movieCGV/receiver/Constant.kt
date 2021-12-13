package com.ducnn17.movieCGV.receiver

import android.Manifest

object Constant {
    val PERMISSION = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    const val REQUEST_CODE_PERMISSION = 0
    const val REQUEST_CODE_CAMERA = 1
    const val REQUEST_CODE_PICK = 2
}