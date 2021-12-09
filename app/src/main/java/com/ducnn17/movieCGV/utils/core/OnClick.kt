package com.ducnn17.movieCGV.utils.core

import android.os.SystemClock
import android.view.View
import com.ducnn17.movieCGV.utils.views.OnSingleClickListener
import java.util.concurrent.atomic.AtomicBoolean

fun View.setSingleClickListener(action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 1000
        private var canClick = AtomicBoolean(true)
        override fun onClick(v: View) {
            lastClickTime = SystemClock.elapsedRealtime()
            if (canClick.getAndSet(false)) {
                v?.run {
                    postDelayed({
                        canClick.set(true)
                    }, lastClickTime)
                    action()
                }
            }
        }
    })
}

fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}