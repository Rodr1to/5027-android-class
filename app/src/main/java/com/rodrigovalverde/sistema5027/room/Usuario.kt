package com.rodrigovalverde.sistema5027.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombres")
    val nombres: String,
    @ColumnInfo(name = "apellidos")
    val apellidos: String,
    @ColumnInfo(name = "foto")
    val foto: String? // Almacenará la URI de la imagen como texto
)