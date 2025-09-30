package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.models.Empleado
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface EmpleadosService {
    @GET("empleados")
    suspend fun getEmpleados(): List<Empleado>
}
class EmpleadosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmpleadosService::class.java)

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaEmpleados by remember {
                    mutableStateOf<List<Empleado>?> (null)}

                LaunchedEffect(Unit) {
                    listaEmpleados = api.getEmpleados()
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        if(isLoading){
                            LinearProgressIndicator()
                        } else {
                            LazyRow {
                                items(items = listaEmpleados!!){ itemEmpleado ->

                                    Box {
                                        AsyncImage(
                                            API_URL + itemEmpleado.foto, null,
                                            modifier = Modifier.fillParentMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        DibujarEmpleado(itemEmpleado)
                                    }
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

fun DibujarEmpleado(itemEmpleado: Empleado) {
        Column {
            Text(text = itemEmpleado.nombres)
            Text(text = itemEmpleado.apellidos)
            Text(text = itemEmpleado.cargo)
        }
}