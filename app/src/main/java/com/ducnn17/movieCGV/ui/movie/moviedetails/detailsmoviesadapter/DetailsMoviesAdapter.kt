package com.ducnn17.movieCGV.ui.movie.moviedetails.detailsmoviesadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.data.detailsmovies.DetailsMovies
import com.ducnn17.movieCGV.utils.core.AppInfo

class DetailsMoviesAdapter (var list : List<DetailsMovies.Cast> = mutableListOf()) : RecyclerView.Adapter<DetailsMoviesAdapter.ViewHolder>() {

    fun submitList(list: List<DetailsMovies.Cast>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list_cast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    class ViewHolder(itemview: View) :RecyclerView.ViewHolder(itemview) {
        val title = itemView.findViewById<TextView>(R.id.name)
        val imageView = itemView.findViewById<ImageView>(R.id.imgMovies)
        fun onBind(item : DetailsMovies.Cast){
            title.text = item.name

            val imageUrl = GlideUrl(
                AppInfo.apiImage + item?.profile_path, LazyHeaders.Builder()
                    .addHeader("api_key", "e7631ffcb8e766993e5ec0c1f4245f93")
                    .build()
            )
            imageView.let {
                Glide.with(imageView.context).load(imageUrl).into(it)
            }
        }
    }
}