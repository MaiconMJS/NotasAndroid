package com.newoverride.notas.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM roomnote")
    fun getAll(): List<RoomNote>

    @Insert
    fun insert(vararg notas: RoomNote)

    @Update
    fun update(notas: RoomNote)

    @Delete
    fun delete(notas: List<RoomNote>)
}