package com.jabg.modulo6p2.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlbumDto(

    @SerializedName("id")
    var id : Long = 0,

    @SerializedName("band")
    var band : String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("album_front")
    var albumFont : String?,

    @SerializedName("year")
    var year : Long = 0,

    @SerializedName("track_count")
    var trackCount : Long = 0


)
