package com.jabg.modulo6p2.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.data.remote.NetworkConnection
import com.jabg.modulo6p2.databinding.FragmentAlbumsBinding
import com.jabg.modulo6p2.ui.LoginActivity
import com.jabg.modulo6p2.ui.MainActivity
import com.jabg.modulo6p2.ui.MainViewModel
import com.jabg.modulo6p2.ui.adapters.AlbumAdapter
import kotlinx.coroutines.launch

class AlbumsFragment : Fragment() {

    private var _binding : FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by viewModels()
    private lateinit var albumAdapter : AlbumAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        binding.btnCloseSession.setOnClickListener {
            closeSession()
        }


        binding.swSound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.imgSound.setImageResource(R.drawable.ic_music)
                (activity as? MainActivity)?.playAudio()
            } else {
                binding.imgSound.setImageResource(R.drawable.ic_music_off)
                (activity as? MainActivity)?.stopAudio()
            }
        }

            val  snackbar : Snackbar = Snackbar.make(view,getString(R.string.disconnected),Snackbar.LENGTH_INDEFINITE)
            .setTextColor(requireContext().getColor(R.color.white))


                val networkConnection = NetworkConnection(requireContext())
                networkConnection.observe(viewLifecycleOwner){ isConnected ->
                    if(isConnected){
                        snackbar.setText(getString(R.string.connected))
                        snackbar.setBackgroundTint(requireContext().getColor(R.color.green))
                        lifecycleScope.launch {
                            try {
                                binding.rvAlbums.apply {
                                    layoutManager = LinearLayoutManager(requireContext())

                                    viewModel.album.observe(viewLifecycleOwner, Observer { album ->
                                        albumAdapter = AlbumAdapter(album){ selectedAlbum ->
                                            findNavController().navigate(AlbumsFragmentDirections.actionAlbumsFragmentToAlbumDetailFragment(id = selectedAlbum.id, title = selectedAlbum.title))
                                        }
                                        adapter = albumAdapter

                                        showData()
                                        snackbar.dismiss()
                                    })
                                }
                                viewModel.getAlbum()

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

    private fun closeSession() {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(getString(R.string.close_title))
            .setMessage(getString(R.string.close_message))
            .setPositiveButton(getString(R.string.close)) { _, _ ->
                firebaseAuth.signOut()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                Toast.makeText(requireContext(), getString(R.string.close_success),Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun showData(){
        binding.viewAlbumLoad.visibility = View.GONE
        binding.rvAlbums.visibility = View.VISIBLE
        binding.lyMain.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as? MainActivity)?.pauseAudio()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.resumeAudio()
    }

}