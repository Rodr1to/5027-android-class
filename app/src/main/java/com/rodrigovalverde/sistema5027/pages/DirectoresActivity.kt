package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.rodrigovalverde.sistema5027.R
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigovalverde.sistema5027.models.Director
import com.rodrigovalverde.sistema5027.models.Proveedor
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.pages.ui.theme.color1
import com.rodrigovalverde.sistema5027.pages.ui.theme.color2
import com.rodrigovalverde.sistema5027.pages.ui.theme.color4
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface DirectoresService {
    @GET("directores.php")
    suspend fun getDirectores(): List<Director>
}

object RetrofitClientDirectores{
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiservice: DirectoresService = retrofit.create(DirectoresService::class.java)
}


class DirectoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = RetrofitClientDirectores.apiservice

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaDirectores by remember {
                    mutableStateOf<List<Director>?> (null)}

                LaunchedEffect(Unit) {
                    listaDirectores = api.getDirectores()
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            startActivity(Intent(this, DirectoresInsertarActivity::class.java))
                        }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                    ) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){
                        Text(text = stringResource(R.string.title_activity_directores),
                            modifier = Modifier.padding(dimensionResource(R.dimen.espacio_1)),
                            style = MaterialTheme.typography.headlineLarge)
                        if(isLoading){
                            LinearProgressIndicator()
                        } else {
                            LazyColumn {

                                    items(items = listaDirectores!!){ itemDirector ->
                                        Column (Modifier.clickable(onClick = {
                                            seleccionarDirector(itemDirector)
                                        })) {
                                            DibujarDirector(itemDirector)
                                        }
                                    }
                                }
                            }
                    }
                }

            }
        }
    }
    fun seleccionarDirector (itemDirector: Director) {
        val bundle = Bundle().apply {
            putInt("iddirector", itemDirector.iddirector)
            putString("nombres", itemDirector.nombres)
            putString("peliculas", itemDirector.peliculas)
        }
        val intent = Intent(this, DirectoresActualizarActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

@Composable

fun DibujarDirector(itemDirector: Director) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(dimensionResource(R.dimen.espacio_2))
            .border(BorderStroke(3.dp, color2))
            .padding(dimensionResource(R.dimen.espacio_3))
    ) {

        Text(
            text = itemDirector.iddirector.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = color1
        )
        Text(text = itemDirector.nombres)
        Text(text = itemDirector.peliculas)
    }
}
