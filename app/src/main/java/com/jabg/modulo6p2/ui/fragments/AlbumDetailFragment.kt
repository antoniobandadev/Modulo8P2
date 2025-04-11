package com.jabg.modulo6p2.ui.fragments

import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.databinding.FragmentAlbumDetailBinding
import com.jabg.modulo6p2.ui.MainViewModel
import kotlinx.coroutines.launch

class AlbumDetailFragment : Fragment() {

    private var _binding : FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private val args: AlbumDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  snackbar : Snackbar = Snackbar.make(view,getString(R.string.disconnected), Snackbar.LENGTH_INDEFINITE)
            .setTextColor(requireContext().getColor(R.color.white))

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner){ isConnected ->
            if(isConnected){
                snackbar.setText(getString(R.string.connected))
                snackbar.setBackgroundTint(requireContext().getColor(R.color.green))

                viewModel.getAlbumDetail(args.id)

                lifecycleScope.launch {
                    try {
                        binding.apply {

                            tvTitle.text = args.title

                            viewModel.albumDet.observe(viewLifecycleOwner, Observer { albumDetail ->

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

                                            showData()
                                            snackbar.dismiss()

                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {

                                        }

                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            ivImage.setImageDrawable(errorDrawable)
                                        }
                                    })
                            })
                        }
                        viewModel.getAlbumDetail(args.id)

                    }catch (e : Exception){
                        e.printStackTrace()
                    }finally {

                    }
                }

            } else {
                snackbar.setText(getString(R.string.disconnected))
                snackbar.setBackgroundTint(requireContext().getColor(R.color.red))
                snackbar.show()

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showData(){
        binding.viewAlbumDetLoad.visibility = View.GONE
        binding.viewAlbumDetComplete.visibility = View.VISIBLE
    }


}