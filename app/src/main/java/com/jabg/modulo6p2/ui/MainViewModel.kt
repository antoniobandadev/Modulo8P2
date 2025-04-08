package com.jabg.modulo6p2.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabg.modulo6p2.data.AlbumRepository
import com.jabg.modulo6p2.data.remote.RetrofitHelper
import com.jabg.modulo6p2.data.remote.model.AlbumDetDto
import com.jabg.modulo6p2.data.remote.model.AlbumDto
import com.jabg.modulo6p2.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.io.IOException

class MainViewModel: ViewModel() {

    private lateinit var repository: AlbumRepository
    private lateinit var retrofit: Retrofit


    private val _album = MutableLiveData<List<AlbumDto>>()
    val album: LiveData<List<AlbumDto>> = _album

    private val _albumDet = MutableLiveData<AlbumDetDto>()
    val albumDet: LiveData<AlbumDetDto> = _albumDet

    fun getAlbum() {
        retrofit = RetrofitHelper().getRetrofit()
        repository = AlbumRepository(retrofit)

        viewModelScope.launch {

            try {
                val albums = repository.getAllAlbums()
                _album.value = albums
                Log.d(Constants.LOGTAG, "Respuesta: $albums")

            }catch (e : IOException){
                e.printStackTrace()
            }

        }

    }

    fun getAlbumDetail(id : Long){
        retrofit = RetrofitHelper().getRetrofit()
        repository = AlbumRepository(retrofit)

        viewModelScope.launch {
            try {
                val albumDetail = repository.getDetAlbum(id)
                _albumDet.value = albumDetail

                Log.d(Constants.LOGTAG, "Respuesta Detalle: $albumDetail")

            }catch (e : Exception){
                e.printStackTrace()
            }
        }

    }



}