package com.rodrigovalverde.sistema5027.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update // <-- AÑADIR IMPORT
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY apellidos ASC") // Ordenado por apellido
    fun getAll(): Flow<List<Usuario>>

    @Insert
    suspend fun insert(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    // --- AÑADIR ESTAS DOS FUNCIONES ---
    @Update
    suspend fun update(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    fun getUsuarioById(usuarioId: Int): Flow<Usuario>
}