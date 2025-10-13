package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.models.Categoria
import com.rodrigovalverde.sistema5027.models.Producto
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.ui.theme.color1
import com.rodrigovalverde.sistema5027.ui.theme.color2
import com.rodrigovalverde.sistema5027.ui.theme.color3
import com.rodrigovalverde.sistema5027.ui.theme.color4
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductoDetalleService {
    @GET("productos.php")
    suspend fun getProductos(@Query("idproducto") idproducto: Int): List<Producto>
}

object RetrofitClientProductoDetalle {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiservice: ProductoDetalleService = retrofit.create(ProductoDetalleService::class.java)
}

class ProductoDetalleActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bundle = intent.extras
        val idproducto = bundle?.getInt("idproducto")
        val api = RetrofitClientProductoDetalle.apiservice

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var productoSeleccionado by remember {
                    mutableStateOf<Producto?> (null)}
                LaunchedEffect(Unit) {
                    productoSeleccionado = api.getProductos(idproducto!!).get(0)
                    isLoading = false
                }
                if (isLoading == true){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                else{
                Scaffold(
                        topBar = {
                            TopAppBar(title = {Text(text = productoSeleccionado!!.nombre)},
                                colors = TopAppBarDefaults.topAppBarColors(
                                    titleContentColor = color1
                                ),
                                navigationIcon = {
                                    IconButton(
                                        onClick = { finish() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                            tint = color4
                                        )
                                    }
                                }
                            )
                        }
                        ) { innerPadding ->
                    Column (Modifier.padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = dimensionResource(R.dimen.espacio_3))) {
                        productoSeleccionado?.let { iProducto ->
                            val imagenProducto = API_URL + (iProducto.imagengrande ?: "imagenes/nofoto.jpg")

                            AsyncImage(
                                model = imagenProducto, null,
                                modifier = Modifier.fillMaxWidth().height(240.dp)
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.espacio_2)))
                            HorizontalDivider(thickness = 1.dp, color = color3)
                            FilaDetalle("Precio", iProducto.precio.toString())
                            FilaDetalle("Detalle", iProducto.detalle)
                            FilaDetalle("Stock", iProducto.unidadesenexistencia.toString())
                            FilaDetalle("Categoria", iProducto.categoria)
                            FilaDetalle("Proveedor", iProducto.proveedor)
                            FilaDetalle("Pais", iProducto.pais)
                            FilaDetalle("Atencion al cliente", iProducto.telefono)
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.espacio_2)))
                            Text("Descripcion", style = MaterialTheme.typography.titleLarge)
                            Text(HtmlCompat.fromHtml(
                                iProducto.descripcion,
                                HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            )
                    }


                    }
                }
            }
            }
        }
    }
}

@Composable
fun FilaDetalle(etiqueta: String, valor: String){
    Row (Modifier.fillMaxWidth().padding(4.dp)) {
        Text(
            etiqueta,
            modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold
        )
        Text(valor, modifier = Modifier.weight(2f))
    }
    HorizontalDivider(thickness = 1.dp, color = color3)
}
