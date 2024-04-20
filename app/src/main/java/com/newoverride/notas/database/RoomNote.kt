package com.newoverride.notas.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "desc") val desc: String?,
    @ColumnInfo(name = "data") val data: String?,
    @ColumnInfo(name = "hora") val hora: String?
)
