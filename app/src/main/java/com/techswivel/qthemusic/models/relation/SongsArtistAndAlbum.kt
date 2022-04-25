package com.techswivel.qthemusic.models.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song

data class SongsArtistAndAlbum(
    @Embedded
    val song: Song,
    @Relation(parentColumn = "artistId", entityColumn = "artistId")
    val artist: Artist,
    @Relation(parentColumn = "albumId", entityColumn = "albumId")
    val album: Album

)
