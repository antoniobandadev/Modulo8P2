package com.jabg.modulo6p2.ui.fragments

import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.databinding.FragmentAlbumDetailBinding
import com.jabg.modulo6p2.ui.MainViewModel
import com.jabg.modulo6p2.ui.adapters.AlbumAdapter
import kotlinx.coroutines.launch

class AlbumDetailFragment : Fragment() {

    private var _binding : FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private val args: AlbumDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner){ isConnected ->
            if(isConnected){
               // Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                viewModel.getAlbumDetail(args.id)

                lifecycleScope.launch {
                    try {
                        binding.apply {

                            tvTitle.text = args.title

                            viewModel.albumDet.observe(viewLifecycleOwner, Observer { albumDetail ->



                                Glide.with(binding.root.context)
                                    .load(albumDetail.albumBack)
                                    .placeholder(R.drawable.foals)
                                    .into(binding.ivImage)

                                    tvLongDesc.text = albumDetail.description
                                    tvGenre.text = albumDetail.genre
                                    tvDuration.text = albumDetail.totalDuration
                                    tvProducers.text = albumDetail.producers
                                    tvSingles.text = albumDetail.highlightedSingles
                                    tvCritical.text = albumDetail.criticalReception
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                        tvLongDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                                    binding.pbLoading.visibility = View.GONE

                                /*Glide.with(requireActivity())
                                    .load(albumDetail.albumBack)
                                    .into(object : CustomTarget<Drawable>() {
                                        override fun onResourceReady(
                                            resource: Drawable,
                                            transition: Transition<in Drawable>?
                                        ) {
                                            // Una vez que la imagen se ha cargado, la mostramos
                                            ivImage.setImageDrawable(resource)

                                            // Luego mostramos el resto de la información
                                            tvLongDesc.text = albumDetail.description
                                            tvGenre.text = albumDetail.genre
                                            tvDuration.text = albumDetail.totalDuration
                                            tvProducers.text = albumDetail.producers
                                            tvSingles.text = albumDetail.highlightedSingles
                                            tvCritical.text = albumDetail.criticalReception
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                                tvLongDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                                            binding.pbLoading.visibility = View.GONE

                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {
                                            // Aquí puedes poner un placeholder o manejar lo que pasa si la imagen se elimina
                                        }

                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            // Si la carga falla, puedes mostrar un mensaje o imagen por defecto
                                            ivImage.setImageDrawable(errorDrawable) //Default Glide
                                            //ivImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.loading)) //Personalizada

                                        }
                                    })*/
                            })
                        }
                        viewModel.getAlbumDetail(args.id)

                    }catch (e : Exception){
                        e.printStackTrace()
                    }finally {

                    }
                }

            } else {
                Toast.makeText(context, "Desconnected", Toast.LENGTH_SHORT).show()
            }

        }




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}