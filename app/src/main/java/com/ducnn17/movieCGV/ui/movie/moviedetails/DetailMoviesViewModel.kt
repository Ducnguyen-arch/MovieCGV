package com.ducnn17.movieCGV.ui.movie.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailMoviesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Fragment control"
    }
    val text: LiveData<String> = _text
}