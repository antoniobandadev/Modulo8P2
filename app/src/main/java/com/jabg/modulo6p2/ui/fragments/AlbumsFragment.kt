package com.jabg.modulo6p2.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.databinding.FragmentAlbumsBinding
import com.jabg.modulo6p2.ui.MainViewModel
import com.jabg.modulo6p2.ui.adapters.AlbumAdapter
import com.jabg.modulo6p2.utils.Constants
import kotlinx.coroutines.launch

class AlbumsFragment : Fragment() {

    private var _binding : FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private lateinit var albumAdapter : AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner){ isConnected ->
            if(isConnected){
                //Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    try {
                        binding.rvAlbums.apply {
                            layoutManager = LinearLayoutManager(requireContext())

                            viewModel.album.observe(viewLifecycleOwner, Observer { album ->
                                albumAdapter = AlbumAdapter(album){ selectedAlbum ->
                                    //Toast.makeText(context, "Album: ${selectedAlbum.title}", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(AlbumsFragmentDirections.actionAlbumsFragmentToAlbumDetailFragment(id = selectedAlbum.id, title = selectedAlbum.title))
                                }
                                adapter = albumAdapter

                            })
                        }
                        viewModel.getAlbum()

                    }catch (e : Exception){
                        e.printStackTrace()
                    }finally {
                        binding.pbLoading.visibility = View.GONE
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