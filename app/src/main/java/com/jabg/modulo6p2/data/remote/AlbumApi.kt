package com.jabg.modulo6p2.data.remote

import com.jabg.modulo6p2.data.remote.model.AlbumDetDto
import com.jabg.modulo6p2.data.remote.model.AlbumDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumApi {

    @GET("foalsLp")
    suspend fun getAllAlbums() : List<AlbumDto>

    @GET("foals/lp_detail/{id}")
    suspend fun getAlbumDetail(
        @Path("id")
        id: Long
    ): AlbumDetDto

}