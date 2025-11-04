package com.rodrigovalverde.sistema5027.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios")
    fun getAll(): Flow<List<Usuario>>

    @Insert
    suspend fun insert(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}