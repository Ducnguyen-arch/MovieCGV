package com.ducnn17.movieCGV.utils.ui

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewLoadMoreScroll(private val mLinearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    private var currentPage = 1
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading) {
            currentPage++
            Log.d("End", "Sucess")
            onLoadMore(currentPage)
            loading = true
        }
    }

    abstract fun onLoadMore(current_page: Int)
    companion object {
        var TAG = RecyclerViewLoadMoreScroll::class.java.simpleName
    }
}