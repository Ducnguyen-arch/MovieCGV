package com.ducnn17.movieCGV.api

import android.content.Context
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.utils.ui.Alert.info
import com.ducnn17.movieCGV.utils.core.NetworkUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ApiListener {
    companion object {
        fun <T> Call<T>.listen(
            onSuccess: (Boolean, Response<T>) -> Unit,
            onError: (t: Throwable?) -> Unit
        ) {
            try {
                this.enqueue(object : Callback<T> {
                    override fun onFailure(call: Call<T>?, t: Throwable?) {
                        onError(t)
                    }

                    override fun onResponse(call: Call<T>?, response: Response<T>) {
                        onSuccess(response.isSuccessful, response)
                    }
                })
            } catch (e: Throwable) {
                onError(e)
            }
        }
        fun Call<Void>.listen(
            onSuccess: (successful: Boolean) -> Unit,
            onError: (t: Throwable?) -> Unit
        ) {
            try {
                this.enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        onError(t)
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>) {
                        Timber.e(" API RESPONSE: ${response.code()}  -  ${response.message()}")
                        onSuccess(response.isSuccessful)
                    }
                })
            } catch (e: Throwable) {
                onError(e)
            }
        }
        fun <T> Call<T>.listen(
            activity: Context,
            onSuccess: (successful: Boolean, Response<T>) -> Unit
        ) {
            listen(
                onSuccess = onSuccess,
                onError = {
                    Timber.e("! ${it?.message.toString()}")

                    if (!NetworkUtils.isConnected(activity)) {
                        info(activity, R.string.info, activity.getString(R.string.msg_no_network))
                    }else{
                        info(activity, R.string.info, "!: " + it?.message)
                    }
                }
            )
        }

        fun <T> Call<T>.onResponse(activity: Context, action: (T) -> Unit) {
            this.listen(activity) { _, response ->

                if (!response.isSuccessful) {
                    response.handleError(activity)
                    return@listen
                }

                response.body()?.let { action(it) }
            }
        }

        fun <T> Call<T>.onResponseHttp(activity: Context, action: (T?) -> Unit) {
            this.listen(activity) { _, response ->
                if(response.code() == 404){
                    action(null)
                    return@listen
                }

                if (!response.isSuccessful) {
                    response.handleError(activity)
                    return@listen
                }

                response.body()?.let { action(it) }
            }
        }

        fun Call<Void>.onResponse(activity: Context, action: () -> Unit) {
            this.listen(activity) { _, response ->
                if (!response.isSuccessful) {
                    response.handleError(activity)
                    return@listen
                }

                action()
            }
        }

        fun <T> Response<T>.handleError(activity: Context) {
            Timber.e(" _${this}")

            if (!NetworkUtils.isConnected(activity)) {
                throw NoConnectionException(activity.getString(R.string.msg_no_network))
                return
            }

            if (this.code() == 401 || this.code() == 403) {
                info(activity, R.string.info, "!")
                return
            }

            try {
                val json = this.errorBody()?.string().toString()
                val err = Gson().fromJson(json, ApiError::class.java)

                if (err?.message != null) {
                    info(activity, R.string.info, err.message.toString())
                } else {
                    info(activity, R.string.api_error, json)
                }
            } catch (e: Throwable) {
                info(activity, R.string.api_error, this.message())
            }
        }

        fun Call<Void>.onHttpStatus(activity: Context,action: () -> Unit){
            this.listen(activity){ _,_ ->
                action()
            }
        }
    }
}
