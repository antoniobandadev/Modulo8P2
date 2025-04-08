package com.jabg.modulo6p2.data

import com.jabg.modulo6p2.data.remote.AlbumApi
import com.jabg.modulo6p2.data.remote.model.AlbumDetDto
import com.jabg.modulo6p2.data.remote.model.AlbumDto
import retrofit2.Retrofit

class AlbumRepository (private val retrofit: Retrofit){

    private val albumApi = retrofit.create(AlbumApi::class.java)

    suspend fun getAllAlbums() : List<AlbumDto> = albumApi.getAllAlbums()

    suspend fun getDetAlbum(id : Long) : AlbumDetDto = albumApi.getAlbumDetail(id)

}