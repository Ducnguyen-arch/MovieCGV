package com.ducnn17.movieCGV

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ducnn17.movieCGV.data.movies.dao.AppDatabase
import com.ducnn17.movieCGV.databinding.ActionBarBinding
import com.ducnn17.movieCGV.databinding.ActivityMainBinding
import com.ducnn17.movieCGV.utils.ui.ChangeBadgeNumber
import com.ducnn17.movieCGV.utils.ui.MessageEvent
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {
    private lateinit var bagde: BadgeDrawable
    private lateinit var binding: ActivityMainBinding
    private var dataBagdeNumber: Int = 0
    private var currentScreen = R.id.navigation_home
    private lateinit var actionBarBinding: ActionBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favourite,
                R.id.navigation_notifications,
                R.id.details_movies
            )
        )
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer)

        bagde = navView.getOrCreateBadge(R.id.navigation_favourite)
        actionBarBinding = ActionBarBinding.inflate(LayoutInflater.from(applicationContext))
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = actionBarBinding.root
        updateBagdeIcon()
        navView.setupWithNavController(navController)
        setEventClick()


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            currentScreen = destination.id
            when(currentScreen){
                R.id.navigation_home -> {
                    actionBarBinding.frameChangeLayout.visibility = View.VISIBLE
                    actionBarBinding.headerTitle.text = "Movies"
                }
                R.id.navigation_favourite -> {
                    actionBarBinding.frameChangeLayout.visibility = View.VISIBLE
                    actionBarBinding.headerTitle.text = "Favourite"
                }
                R.id.navigation_setting -> {
                    actionBarBinding.frameChangeLayout.visibility = View.VISIBLE
                    actionBarBinding.headerTitle.text = "Setting"
                }
                R.id.navigation_about -> {
                    actionBarBinding.frameChangeLayout.visibility = View.VISIBLE
                    actionBarBinding.headerTitle.text = "About"
                }
                R.id.details_movies -> {
                    actionBarBinding.headerTitle.text = "Details Movies"
                }
                else -> actionBarBinding.frameChangeLayout.visibility = View.GONE

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ChangeBadgeNumber?) { /* Do something */
        dataBagdeNumber += event!!.number
        lifecycleScope.launch {
            val data = AppDatabase.getDataBase(this@MainActivity).resultDao().getAll()
            if (dataBagdeNumber > 0) {
                bagde.isVisible = true
                bagde.number = dataBagdeNumber
            } else {
                bagde.isVisible = false
                bagde.number = dataBagdeNumber
            }

        }
    }


    private fun updateBagdeIcon() {
        lifecycleScope.launch {
            val data = AppDatabase.getDataBase(this@MainActivity).resultDao().getAll()
            dataBagdeNumber = data.size
            if (dataBagdeNumber > 0) {
                bagde.isVisible = true
                bagde.number = data.size
            } else {
                bagde.isVisible = false
                bagde.number = dataBagdeNumber
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

    private fun setEventClick() {
        actionBarBinding.apply {
            drawer.setOnClickListener {
                binding.container.openDrawer(GravityCompat.START)
            }
            imgMore.setOnClickListener {
                val pm = PopupMenu(this@MainActivity, it)
                pm.menuInflater.inflate(R.menu.bottom_nav_menu, pm.getMenu())
                pm.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.home -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return true
                            }
                            R.id.favourite -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return true
                            }

                        }
                        return true
                    }
                })
                pm.show()
            }
            changeLayoutToGrid.setOnClickListener {
                changeLayoutToGrid.visibility = View.GONE
                changeLayoutToLinear.visibility = View.VISIBLE
                EventBus.getDefault().post(MessageEvent())
            }
            changeLayoutToLinear.setOnClickListener {
                changeLayoutToGrid.visibility = View.VISIBLE
                changeLayoutToLinear.visibility = View.GONE
                EventBus.getDefault().post(MessageEvent())
            }
        }
    }
}