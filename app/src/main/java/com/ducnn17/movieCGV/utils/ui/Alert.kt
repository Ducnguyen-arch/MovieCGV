package com.ducnn17.movieCGV.utils.ui

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.ducnn17.movieCGV.R


object Alert {
    fun info(context: Context, title: Int, message: String) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(R.string.ok, null)
            .create()
            .show()
    }

    fun action(context: Context, title: Int, message: String, action: () -> Unit) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(R.string.ok) { _, _ -> action() }
            .create()
            .show()
    }

    fun confirm(
        context: Context,
        title: String,
        message: String,
        action: (choice: Boolean) -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ -> action(true) }
            .setNegativeButton(R.string.no) { _, _ -> action(false) }
            .create()
            .show()
    }

    fun info(context: Context, title: Int, message: Int) {
        info(
            context,
            title,
            context.getString(message)
        )
    }

    fun action(context: Context, title: Int, message: Int, action: () -> Unit) {
        action(
            context,
            title,
            context.getString(message),
            action
        )
    }

    fun Fragment.toast(info: String) {
        Toast.makeText(
            requireActivity(),
            info,
            Toast.LENGTH_LONG
        ).show()
    }

    fun Fragment.toast(@StringRes info: Int) {
        Toast.makeText(
            requireActivity(),
            info,
            Toast.LENGTH_LONG
        ).show()
    }

    fun Fragment.toast(info: String, short: Boolean) {
        if (short) {
            Toast.makeText(
                requireActivity(),
                info,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            toast(info)
        }
    }
}