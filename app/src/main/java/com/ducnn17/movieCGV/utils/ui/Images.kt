package com.ducnn17.movieCGV.utils.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ducnn17.movieCGV.utils.core.AppInfo

object Images {
    fun ImageView.setImage(url: String){
        Glide.with(this).load(AppInfo.apiImage+url)
            .into(this);
    }
}
