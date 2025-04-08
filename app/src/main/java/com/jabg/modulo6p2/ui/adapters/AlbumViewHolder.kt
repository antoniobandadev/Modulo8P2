package com.jabg.modulo6p2.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jabg.modulo6p2.data.remote.model.AlbumDto
import com.jabg.modulo6p2.databinding.AlbumElementBinding

class AlbumViewHolder (
    private val binding: AlbumElementBinding
): RecyclerView.ViewHolder(binding.root){

    fun bind(album:AlbumDto){
        binding.apply {
            tvTitle.text = album.title
            tvBand.text = album.band
            tvYear.text = album.year.toString()
            tvTracks.text = album.trackCount.toString()

            Glide.with(binding.root.context)
                .load(album.albumFont)
                .into(binding.ivAlbumPort)

        }

    }



}