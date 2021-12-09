package com.ducnn17.movieCGV.ui.movie.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.ducnn17.movieCGV.api.ApiClient
import com.ducnn17.movieCGV.api.movies.MoviesApi
import com.ducnn17.movieCGV.ui.movie.moviedetails.detailsmoviesadapter.DetailsMoviesAdapter
import com.ducnn17.movieCGV.utils.core.AppInfo
import com.ducnn17.movieCGV.api.ApiListener.Companion.onResponse
import androidx.lifecycle.lifecycleScope
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.data.movies.dao.AppDatabase
import com.ducnn17.movieCGV.databinding.FragmentDetailMoviesBinding
import kotlinx.coroutines.launch


class DetailMoviesFragment : Fragment() {
    private lateinit var moviesApi: MoviesApi
    private lateinit var detailsAdapter: DetailsMoviesAdapter
    private lateinit var dashboardViewModel: DetailMoviesViewModel
    private var _binding: FragmentDetailMoviesBinding? = null
    private  var idMovies: Int? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client = ApiClient().client(requireActivity())
        moviesApi = client.create(MoviesApi::class.java)
        idMovies = requireArguments().getInt("idMovies")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DetailMoviesViewModel::class.java)

        _binding = FragmentDetailMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkFavorite()
        initRecyclerView()
        idMovies?.let {
            moviesApi.getMoviesDetails(it).onResponse(requireActivity()){
                binding.apply {
                    releaseday.text = it.release_date
                    rating.text = it.vote_average.toString() + "/10"
                    val imageUrl = GlideUrl(
                        AppInfo.apiImage + it?.backdrop_path, LazyHeaders.Builder()
                            .addHeader("api_key", "e7631ffcb8e766993e5ec0c1f4245f93")
                            .build()
                    )
                    imageView2.let {
                        Glide.with(it.context).load(imageUrl).into(it)
                    }
                    overview.text = it.overview
                    Log.d("data", it.genre_ids.toString())

                }
            }
        }


        idMovies?.let {
            moviesApi.getCastCrewList(it).onResponse(requireActivity()){
                detailsAdapter.submitList(it.cast)
            }
        }
    }
    private fun checkFavorite(){
        idMovies?.let {
            lifecycleScope.launch {
                val data =  AppDatabase.getDataBase(requireContext()).resultDao().checkid(idMovies!!)
                if(data != null){
                    binding.imgStar.setImageResource(R.drawable.ic_baseline_star_24)
                }else{
                    binding.imgStar.setImageResource(R.drawable.ic_baseline_star_border_24)
                }
            }
        }

    }
    private fun initRecyclerView(){
        val layoutManager  = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        detailsAdapter = DetailsMoviesAdapter()
        binding.listCast.layoutManager = layoutManager
        binding.listCast.adapter = detailsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}