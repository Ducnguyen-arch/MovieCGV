package com.ducnn17.movieCGV.ui.movie.movieAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.data.movies.entity.Result
import com.ducnn17.movieCGV.data.movies.dao.AppDatabase
import com.ducnn17.movieCGV.utils.core.AppInfo
import com.ducnn17.movieCGV.utils.ui.ChangeBadgeNumber
import com.ducnn17.movieCGV.utils.ui.ItemClickListener
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class MovieAdapter(
    var list: MutableList<Result?>? = null,
    val context: Context,
    var type: Int? = null,
    val lifecycle: LifecycleCoroutineScope,
    val ItemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val type: Int) {
        TYPE_ONE(0),
        TYPE_TWO(1)
    }

    fun setType(type: Int) {
        this.type = type
        notifyDataSetChanged()
    }

    fun submitList(list: MutableList<Result?>) {
        this.list = list
        notifyDataSetChanged()

    }

    fun addList(list: MutableList<Result?>) {
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (this.type != null) {
            return type!!
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.TYPE_ONE.type -> {
                val view = inflater.inflate(R.layout.item_list_movies, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_grid_list_movies, parent, false)
                ViewHolder2(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.apply {
            when (this) {
                is ViewHolder -> this.onBind(list?.get(position)!!)
                is ViewHolder2 -> this.onBind(list?.get(position)!!)
            }
        }
    }

    override fun getItemCount(): Int {
        if (list.isNullOrEmpty()) {
            return 0
        }
        return list!!.size

    }

    inner class ViewHolder(
        itemview: View
    ) : RecyclerView.ViewHolder(itemview) {
        val releaseday = itemView.findViewById<TextView>(R.id.releaseday)
        val rating = itemView.findViewById<TextView>(R.id.rating)
        val overView = itemView.findViewById<TextView>(R.id.overView)
        val title = itemView.findViewById<TextView>(R.id.title)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val imageadult = itemView.findViewById<ImageView>(R.id.adult)
        val star = itemview.findViewById<ImageView>(R.id.favourite)

        init {
            itemview.setOnClickListener {
                ItemClickListener.onClick(list!![layoutPosition]!!.id)
            }

            star.setOnClickListener {
                lifecycle.launch {
                    val data =  AppDatabase.getDataBase(context).resultDao().checkid(list!![layoutPosition]!!.id)
                    if(data != null){
                        star.setImageResource(R.drawable.ic_baseline_star_border_24)
                            AppDatabase.getDataBase(context).resultDao().delete(list!![layoutPosition]!!)
                        EventBus.getDefault().post( ChangeBadgeNumber(-1)) //gui event di
                    }else{
                        star.setImageResource(R.drawable.ic_baseline_star_24)
                        AppDatabase.getDataBase(context).resultDao().insertAll(list!![layoutPosition]!!)
                        EventBus.getDefault().post( ChangeBadgeNumber(1)) //gui event di
                    }
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind(item: Result) {
            title.text = item.title
            releaseday.text = item.release_date
            rating.text = item.vote_average.toString() + "/10"
            overView.text = item.overview
            Log.d("linkimage", item.backdrop_path)
            Log.d("linkposter", item.poster_path)

            lifecycle.launch {
                val data =  AppDatabase.getDataBase(context).resultDao().checkid(item.id)
                star.setImageResource(R.drawable.ic_baseline_star_24)
            }


            val imageUrl = GlideUrl(
                AppInfo.apiImage + item.backdrop_path, LazyHeaders.Builder()
                    .addHeader("api_key", "e7631ffcb8e766993e5ec0c1f4245f93")
                    .build()
            )
            imageView.let {
                Glide.with(imageView.context).load(imageUrl).into(it)
            }
            if (item.adult) {
                imageadult.visibility = View.VISIBLE
            } else {
                imageadult.visibility = View.GONE
            }

        }
    }

    inner class ViewHolder2(
        itemview: View
    ) : RecyclerView.ViewHolder(itemview) {
        init {
            itemview.setOnClickListener {
                ItemClickListener.onClick(list!![layoutPosition]!!.id)
            }
        }
        val title = itemView.findViewById<TextView>(R.id.name)
        val imageView = itemView.findViewById<ImageView>(R.id.imgMovies)

        @SuppressLint("SetTextI18n")
        fun onBind(item: Result) {
            title.text = item.title


            val imageUrl = GlideUrl(
                AppInfo.apiImage + item?.backdrop_path, LazyHeaders.Builder()
                    .addHeader("api_key", "e7631ffcb8e766993e5ec0c1f4245f93")
                    .build()
            )
            imageView.let {
                Glide.with(imageView.context).load(imageUrl).into(it)
            }

        }
    }
}