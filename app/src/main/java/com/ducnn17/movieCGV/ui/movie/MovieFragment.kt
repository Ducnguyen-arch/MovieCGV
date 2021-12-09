package com.ducnn17.movieCGV.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.api.ApiClient
import com.ducnn17.movieCGV.api.movies.MoviesApi
import com.ducnn17.movieCGV.ui.movie.movieAdapter.MovieAdapter
import com.ducnn17.movieCGV.utils.ui.ItemClickListener
import com.ducnn17.movieCGV.api.ApiListener.Companion.onResponse
import com.ducnn17.movieCGV.databinding.FragmentHomeBinding
import com.ducnn17.movieCGV.utils.ui.MessageEvent
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus


class MovieFragment : Fragment() {
    private lateinit var moviesApi: MoviesApi
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private var _binding: FragmentHomeBinding? = null
    private var page: Int? = null
    private var totalPages = 0

    private val binding get() = _binding!!

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        if (binding.listMovies.layoutManager is GridLayoutManager){

            binding.listMovies.layoutManager = LinearLayoutManager(requireActivity())
            Toast.makeText(context,"Changed LinearLayout", Toast.LENGTH_LONG).show()
            movieAdapter.setType(0)
            movieAdapter.notifyDataSetChanged()

        }else if (binding.listMovies.layoutManager is LinearLayoutManager){

            binding.listMovies.layoutManager = GridLayoutManager(requireContext(),2)
            movieAdapter.setType(1)
            Toast.makeText(context,"Changed GridLayout", Toast.LENGTH_LONG).show()
            movieAdapter.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client = ApiClient().client(requireActivity())
        moviesApi = client.create(MoviesApi::class.java)
        page = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieViewModel =
            ViewModelProvider(this)[MovieViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setRVScrollListener()

        moviesApi.getListMovies(page!!).onResponse(requireContext()){
            Log.d("data", it.toString())
            totalPages = it.total_pages
            movieAdapter.submitList(it.results.toMutableList())
        }
    }

    private fun initRecyclerView(){
        movieAdapter = MovieAdapter(context = requireContext(),lifecycle = lifecycleScope,
            ItemClickListener = object : ItemClickListener {
                override fun onClick(id: Int) {
                    findNavController().navigate(R.id.details_movies, Bundle().apply {
                        putInt("idMovies",id )
                    })
                }
            })
        binding.listMovies.adapter = movieAdapter
    }
    private fun setRVScrollListener() {
        binding.listMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1)) {
                    loadMoreData()
                }
            }
        })
    }

    private fun loadMoreData() {
        if (totalPages > page!!){
            page = page!!+1
            moviesApi.getListMovies(page!!).onResponse(requireContext()){
                Log.d("data", it.toString())
                movieAdapter.addList(it.results.toMutableList())
            }
        }
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}