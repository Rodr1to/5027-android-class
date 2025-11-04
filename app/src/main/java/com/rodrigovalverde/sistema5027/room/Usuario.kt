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

    // AÑADIR ESTE CAMPO
    @ColumnInfo(name = "edad")
    val edad: Int, // <-- Tu profesor pide Edad

    @ColumnInfo(name = "foto")
    val foto: String? // Opcional, como mencionaste
)