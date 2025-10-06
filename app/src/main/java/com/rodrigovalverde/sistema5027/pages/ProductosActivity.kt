package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

import com.rodrigovalverde.sistema5027.R

import com.rodrigovalverde.sistema5027.models.Producto
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductosService{
    @GET("productos.php")
    suspend fun getProductos(@Query("idcategoria") idcategoria: Int): List<Producto>
}

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras //para poder leer datos que se envían a este activity
        val idcategoria = bundle!!.getInt("idcategoria")
        val nombre = bundle.getString("nombre")
        val descripcion = bundle.getString("descripcion")
        Log.d("datos", nombre.toString())

        val api = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductosService::class.java)

        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaProductos by remember {
                    mutableStateOf<List<Producto>?> (null)}

                LaunchedEffect(Unit) {
                    listaProductos = api.getProductos(idcategoria)
                    isLoading = false
                }
                Column {
                    if(isLoading){
                        LinearProgressIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.padding(dimensionResource(R.dimen.espacio_3)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                            columns = GridCells.Fixed(2)
                        ) {
                            items(items = listaProductos!!){ itemProducto ->
                                DibujarProducto(itemProducto)
                            }
                        }
                    }
                }


            }
        }
    }//onCreate
}//class

@Composable
fun DibujarProducto(itemProducto: Producto){
    Column {
        AsyncImage(model = API_URL + itemProducto.imagenchica, null,
            modifier = Modifier.fillMaxWidth().height(120.dp))
        Text(text = itemProducto.nombre)
        val precio = itemProducto.precio
        Text(text = "S/ " + String.format("%.2f", precio))
    }
}