package com.jabg.modulo6p2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jabg.modulo6p2.data.remote.model.AlbumDto
import com.jabg.modulo6p2.databinding.AlbumElementBinding

class AlbumAdapter (
    private val albums : List<AlbumDto>,
    private val onAlbumClick: (AlbumDto) -> Unit
): RecyclerView.Adapter<AlbumViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumElementBinding.inflate(LayoutInflater.from(parent.context))
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)

        holder.itemView.setOnClickListener {
            onAlbumClick(album)
        }
    }
}