package com.jabg.modulo6p2.ui.fragments

import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.databinding.FragmentAlbumDetailBinding
import com.jabg.modulo6p2.ui.MainActivity
import com.jabg.modulo6p2.ui.MainViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch

class AlbumDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding : FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private val args: AlbumDetailFragmentArgs by navArgs()

    private var player: YouTubePlayer? = null

    private lateinit var googleMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lifecycle.addObserver(binding.vvYoutube)

        val  snackBar : Snackbar = Snackbar.make(view,getString(R.string.disconnected), Snackbar.LENGTH_INDEFINITE)
            .setTextColor(requireContext().getColor(R.color.white))

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner){ isConnected ->
            if(isConnected){
                snackBar.setText(getString(R.string.connected))
                snackBar.setBackgroundTint(requireContext().getColor(R.color.green))

                viewModel.getAlbumDetail(args.id)

                lifecycleScope.launch {
                    try {
                        binding.apply {

                            tvTitle.text = args.title

                            viewModel.albumDet.observe(viewLifecycleOwner) { albumDetail ->

                                Glide.with(requireActivity())
                                    .load(albumDetail.albumBack)
                                    .into(object : CustomTarget<Drawable>() {
                                        override fun onResourceReady(
                                            resource: Drawable,
                                            transition: Transition<in Drawable>?
                                        ) {

                                            ivImage.setImageDrawable(resource)
                                            tvLongDesc.text = albumDetail.description
                                            tvGenre.text = albumDetail.genre
                                            tvDuration.text = albumDetail.totalDuration
                                            tvProducers.text = albumDetail.producers
                                            tvSingles.text = albumDetail.highlightedSingles
                                            tvCritical.text = albumDetail.criticalReception
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                                tvLongDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                                            vvYoutube.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                                    super.onReady(youTubePlayer)
                                                    player = youTubePlayer
                                                    youTubePlayer.loadVideo(albumDetail.videoId.toString(), 0f)
                                                    showData()
                                                }
                                                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {

                                                    when(state) {
                                                        PlayerConstants.PlayerState.UNSTARTED -> {
                                                            Log.d("YouTubePlayer", "Video no initialized")
                                                        }
                                                        PlayerConstants.PlayerState.VIDEO_CUED -> {
                                                            Log.d("YouTubePlayer", "Video ready")
                                                        }
                                                        PlayerConstants.PlayerState.BUFFERING -> {
                                                            Log.d("YouTubePlayer", "Video loading (buffering)")
                                                        }
                                                        PlayerConstants.PlayerState.PLAYING -> {
                                                            Log.d("YouTubePlayer", "Video playing")
                                                            (activity as? MainActivity)?.pauseAudio()
                                                        }
                                                        PlayerConstants.PlayerState.PAUSED -> {
                                                            Log.d("YouTubePlayer", "Video paused")
                                                        }
                                                        PlayerConstants.PlayerState.ENDED -> {
                                                            Log.d("YouTubePlayer", "Video ended")
                                                        }else -> {

                                                        }
                                                    }
                                                }

                                            })

                                            snackBar.dismiss()

                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {

                                        }

                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            ivImage.setImageDrawable(errorDrawable)
                                        }
                                    })
                            }
                        }
                        viewModel.getAlbumDetail(args.id)

                    }catch (e : Exception){
                        e.printStackTrace()
                    }finally {

                    }
                }

            } else {
                snackBar.setText(getString(R.string.disconnected))
                snackBar.setBackgroundTint(requireContext().getColor(R.color.red))
                snackBar.show()

            }

        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        viewModel.albumDet.observe(viewLifecycleOwner) { albumDetail ->

            createMaker(albumDetail.latitude?.toDouble() ?: 0.0,
                        albumDetail.longitude?.toDouble() ?: 0.0,
                            albumDetail.studio.toString(),
                            albumDetail.location.toString())

        }
    }

    private fun createMaker(lat: Double, lon: Double, title: String, snippet: String){
        val coordinates = LatLng(lat, lon)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(title)
            .snippet(snippet)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.guitar))
        googleMap.addMarker(marker)
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4_000,
            null
        )
    }



    override fun onDestroy() {
        super.onDestroy()
        binding.vvYoutube.release()
        _binding = null
        (activity as? MainActivity)?.resumeAudio()

    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }


    private fun showData(){
        binding.viewAlbumDetLoad.visibility = View.GONE
        binding.viewAlbumDetComplete.visibility = View.VISIBLE
    }



}