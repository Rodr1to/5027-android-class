package com.rodrigovalverde.sistema5027.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.models.Producto
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.ui.theme.color4
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductosService{
    @GET("productos.php")
    suspend fun getProductos(@Query("idcategoria") idcategoria: Int): List<Producto>
}

object RetrofitClientProductos{
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiservice: ProductosService = retrofit.create(ProductosService::class.java)
}

class ProductosActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras //para poder leer datos que se envían a este activity
        val idcategoria = bundle!!.getInt("idcategoria")
        val nombre = bundle.getString("nombre")
        val descripcion = bundle.getString("descripcion")
        Log.d("datos", nombre.toString())

        val api = RetrofitClientProductos.apiservice

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
                Scaffold (
                    topBar = {
                        TopAppBar(title = {Text(text = nombre.toString())},
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                titleContentColor = Color.White
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                    }
                ) {
                    Column (Modifier.padding(it)){
                        if (isLoading) {
                            LinearProgressIndicator()
                        } else {
                            Text(descripcion.toString())
                            LazyVerticalGrid(
                                modifier = Modifier.padding(dimensionResource(R.dimen.espacio_3)),
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                                columns = GridCells.Fixed(2)
                            ) {
                                items(items = listaProductos!!) { itemProducto ->
                                    Card(
                                        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.espacio_1)),
                                        colors = CardDefaults.cardColors(Color.White),
                                        modifier = Modifier.clickable(
                                            onClick = {
                                                seleccionarProducto(itemProducto.idproducto)
                                            }
                                        )
                                    ) {
                                        DibujarProducto(itemProducto)
                                    }
                                }
                            }
                        }
                    } // Column
                }

            }
        }
    }//onCreate
    fun seleccionarProducto(idproducto: Int) {
        val intent = Intent(this, ProductoDetalleActivity::class.java)
        val bundle = Bundle().apply {
            putInt("idproducto", idproducto)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }


}//class

@SuppressLint("DefaultLocale")
@Composable
fun DibujarProducto(itemProducto: Producto){
    var precioFinal:Float = itemProducto.precio
    if(itemProducto.preciorebajado != 0f){
        precioFinal = itemProducto.preciorebajado
    }

    Column{
        Box (contentAlignment = Alignment.TopEnd) {
            var imagenProducto = API_URL + "imagenes/nofoto.jpg"
            if (itemProducto.imagenchica != null){
                imagenProducto = API_URL + itemProducto.imagenchica
            }

            AsyncImage(
                model = imagenProducto, null,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            if (itemProducto.preciorebajado != 0f) {
                val porcentajeDescuento =
                    (1 - itemProducto.preciorebajado / itemProducto.precio) * 100
                Text(text = "-" + String.format("%.0f", porcentajeDescuento) + "%",
                    modifier = Modifier
                        .background(color = color4)
                        .padding(horizontal = dimensionResource(R.dimen.espacio_2)),
                    color = Color.White)

            }
        }
        Column (modifier = Modifier.padding(dimensionResource(R.dimen.espacio_2)),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = itemProducto.nombre, modifier = Modifier.fillMaxWidth().height(40.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                lineHeight = 14.sp)
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = "S/ " + String.format("%.2f", precioFinal))
                if(itemProducto.preciorebajado != 0f){
                    Text(text = "S/ " + String.format("%.2f", itemProducto.precio),
                        color = Color.Red,
                        textDecoration = TextDecoration.LineThrough,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.espacio_1))
                    )
                }
            }
        }
    }
}