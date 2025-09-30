package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rodrigovalverde.sistema5027.models.Categoria
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CategoriasService {
    @GET("categorias.php")
    suspend fun getCategorias(): List<Categoria>
}
class TiendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoriasService::class.java)

        setContent {
            Sistema5027Theme {

                var isLoading by remember { mutableStateOf(true) }
                var listaCategorias by remember {
                    mutableStateOf<List<Categoria>?> (null)}

                LaunchedEffect(Unit) {
                    listaCategorias = api.getCategorias()
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        if(isLoading){
                            LinearProgressIndicator()
                        } else {
                            LazyColumn {
                                items(items = listaCategorias!!){ itemCategoria ->
                                    DibujarCategoria(itemCategoria)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable

fun DibujarCategoria(itemCategoria: Categoria) {
        Text(text = itemCategoria.nombre)
        Text(text = itemCategoria.descripcion)
        Text(text = itemCategoria.total.toString())

}