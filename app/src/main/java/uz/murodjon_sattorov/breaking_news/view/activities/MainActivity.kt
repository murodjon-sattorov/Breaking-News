package uz.murodjon_sattorov.breaking_news.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.isVibration
import uz.murodjon_sattorov.breaking_news.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    lateinit var navController: NavController

    private var back = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        navController = findNavController(R.id.main_fragment)

        mainBinding.bottomBar.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    navController.navigate(R.id.homeFragment)
                }
                1 -> {
                    navController.navigate(R.id.categoryFragment)
                }
                2 -> {
                    navController.navigate(R.id.followFragment)
                }
                else -> {
                    navController.navigate(R.id.savedFragment)
                }
            }
        }

        mainBinding.bottomBar.setOnItemReselectedListener {

        }
    }

    fun closeBottom() {
        mainBinding.bottomBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.categoryFragment
            || navController.currentDestination?.id == R.id.followFragment
            || navController.currentDestination?.id == R.id.savedFragment
        ) {
            navController.navigate(R.id.homeFragment)
            mainBinding.bottomBar.itemActiveIndex = 0
        } else if (navController.currentDestination?.id == R.id.searchFragment
            || navController.currentDestination?.id == R.id.settingsFragment
            || navController.currentDestination?.id == R.id.fullNewsInfoFragment
            || navController.currentDestination?.id == R.id.aboutFragment
        ) {
            navController.popBackStack()
        } else if (navController.currentDestination?.id == R.id.homeFragment) {
            if (back) {
                finish()
            } else {
                isVibration(this)
                Toast.makeText(this, "Press to AGAIN", Toast.LENGTH_SHORT).show()
                back = true
            }
            this.lifecycleScope.launch {
                delay(2000L)
                back = false
            }
        }
    }

}