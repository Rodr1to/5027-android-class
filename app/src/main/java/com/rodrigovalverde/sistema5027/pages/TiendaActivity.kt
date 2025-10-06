package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.models.Categoria
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.ui.theme.color1
import com.rodrigovalverde.sistema5027.ui.theme.color3
import com.rodrigovalverde.sistema5027.ui.theme.color4

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
                        Text(text = "Categorias",
                            modifier = Modifier.padding(dimensionResource(R.dimen.espacio_3)),
                            style = MaterialTheme.typography.headlineLarge)
                        if(isLoading){
                            LinearProgressIndicator()
                        } else {
                            LazyColumn {
                                items(items = listaCategorias!!){ itemCategoria ->
                                    Card(elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.espacio_1)),
                                        colors = CardDefaults.cardColors(color1),
                                        modifier = Modifier.fillMaxWidth()
                                        .padding(
                                            dimensionResource(R.dimen.espacio_3))
                                        .clickable(onClick = {
                                            seleccionarCategoria(itemCategoria)
                                        })) {
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
    fun seleccionarCategoria(itemCategoria: Categoria) {
        Log.d("Categoria", itemCategoria.nombre)
        Toast.makeText(this, itemCategoria.nombre, Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putInt("idcategoria", itemCategoria.idcategroia)
            putString("nombre", itemCategoria.nombre)
            putString("descripcion", itemCategoria.descripcion)
        }
        val intent = Intent(this, ProductosActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}//class

@Composable

fun DibujarCategoria(itemCategoria: Categoria) {
    AsyncImage(modifier = Modifier.fillMaxWidth().height(120.dp),
        contentScale = ContentScale.Crop,
        model = API_URL + itemCategoria.foto,
        contentDescription = null)
    Row (modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.espacio_2))){
        Text(text = itemCategoria.idcategroia.toString(),
            style = MaterialTheme.typography.displaySmall,
            color = color3,
            modifier = Modifier.width(50.dp))
        Column {
            Text(text = itemCategoria.nombre, style = MaterialTheme.typography.titleLarge)
            Text(text = itemCategoria.descripcion)
            Text(text = "Total: " + itemCategoria.total.toString(), style = MaterialTheme.typography.labelLarge)
        }
    }
}