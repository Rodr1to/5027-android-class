package com.rodrigovalverde.sistema5027.models

data class Producto(
    val idproducto: Int,
    val nombre: String,
    val precio: Float,
    val preciorebajado: Float,
    val imagenchica: String,
    val imagengrande: String,
    val detalle: String,
    val unidadesenexistencia: Int,
    val categoria: String,
    val proveedor: String,
    val telefono: String,
    val pais: String,
    val descripcion: String

)
