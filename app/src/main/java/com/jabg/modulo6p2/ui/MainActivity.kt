package com.jabg.modulo6p2.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.AlbumRepository
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.data.remote.RetrofitHelper
import com.jabg.modulo6p2.databinding.ActivityMainBinding
import com.jabg.modulo6p2.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //enableEdgeToEdge()
        setContentView(binding.root)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentNavContainer) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)


        screenSplash.setKeepOnScreenCondition {false}


        onBackPressedDispatcher.addCallback(this) {
            if (!navController.popBackStack()) {
                super.finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}