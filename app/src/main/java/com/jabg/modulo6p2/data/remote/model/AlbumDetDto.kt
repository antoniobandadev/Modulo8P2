package com.jabg.modulo6p2.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlbumDetDto (
    @SerializedName("year")
    var year: Long = 0,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("album_back")
    var albumBack: String? = null,

    @SerializedName("total_duration")
    var totalDuration: String? = null,

    @SerializedName("genre")
    var genre: String? = null,

    @SerializedName("producers")
    var producers: String? = null,

    @SerializedName("highlighted_singles")
    var highlightedSingles: String? = null,

    @SerializedName("critical_reception")
    var criticalReception: String? = null,

    @SerializedName("track_count")
    var trackCount: Long = 0,

    @SerializedName("video")
    var videoId: String? = null,

    @SerializedName("studio")
    var studio: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("latitude")
    var latitude: String? = null,

    @SerializedName("longitude")
    var longitude: String? = null,
)