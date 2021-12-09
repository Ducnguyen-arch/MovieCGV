package com.ducnn17.movieCGV.ui.favorite

import android.content.IntentFilter
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
import com.ducnn17.movieCGV.R
import com.ducnn17.movieCGV.data.movies.dao.AppDatabase
import com.ducnn17.movieCGV.databinding.FragmentFavouriteBinding
import com.ducnn17.movieCGV.ui.favorite.favouriteAdapter.FavouriteAdapter
import com.ducnn17.movieCGV.utils.ui.ItemClickListener
import com.ducnn17.movieCGV.utils.ui.MessageEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FavouriteFragment : Fragment() {
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        if (binding.listItem.layoutManager is GridLayoutManager){

            binding.listItem.layoutManager = LinearLayoutManager(requireActivity())
            Toast.makeText(context,"Changed LinearLayout", Toast.LENGTH_LONG).show()
            favouriteAdapter.setType(0)
            favouriteAdapter.notifyDataSetChanged()

        }else if (binding.listItem.layoutManager is LinearLayoutManager){

            binding.listItem.layoutManager = GridLayoutManager(requireContext(),2)
            favouriteAdapter.setType(1)
            Toast.makeText(context,"Changed GridLayout", Toast.LENGTH_LONG).show()
            favouriteAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter : IntentFilter = IntentFilter("Broadcast")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteViewModel =
            ViewModelProvider(this)[FavouriteViewModel::class.java]

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        lifecycleScope.launch {
            val data = AppDatabase.getDataBase(requireActivity()).resultDao().getAll()
            Log.d("data", data.toString())
            favouriteAdapter.submitList(data.toMutableList())
        }
    }

    private fun initRecyclerView(){
        favouriteAdapter = FavouriteAdapter(context = requireContext(),lifecycle = lifecycleScope,ItemClickListener = object :
            ItemClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(R.id.details_movies, Bundle().apply {
                    putInt("idMovies",id )
                })
            }
        })
        binding.listItem.adapter = favouriteAdapter
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