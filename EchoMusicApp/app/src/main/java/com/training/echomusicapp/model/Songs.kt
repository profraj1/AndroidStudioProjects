package com.training.echomusicapp.model

data class Songs(
    val songId: Long,
    val songName: String,
    val songArtist: String,
    val songData: String,
    val dateAdded: Long
)