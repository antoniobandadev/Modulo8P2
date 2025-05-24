package com.jabg.modulo6p2.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //enableEdgeToEdge()
        setContentView(binding.root)
        playAudio()

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

    fun pauseAudio() {
            mediaPlayer.pause()
    }

    fun resumeAudio() {
            mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    override fun onStop() {
        super.onStop()
        pauseAudio()
    }

    override fun onPause() {
        super.onPause()
        pauseAudio()
    }

    fun stopAudio() {
        mediaPlayer.stop()
    }

    fun playAudio() {
        val songs = listOf(R.raw.foals, R.raw.foals2, R.raw.foals3)
        val song = songs.random()
        mediaPlayer = MediaPlayer.create(this, song)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

}