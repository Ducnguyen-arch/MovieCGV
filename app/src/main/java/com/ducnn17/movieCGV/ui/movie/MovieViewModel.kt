package com.ducnn17.movieCGV.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Movie Fragment"
    }
    val text: LiveData<String> = _text
}