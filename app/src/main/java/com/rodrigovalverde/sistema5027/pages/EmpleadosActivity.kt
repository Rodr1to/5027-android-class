package com.rodrigovalverde.sistema5027.pages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.models.Empleado
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.rodrigovalverde.sistema5027.R

interface EmpleadosService {
    @GET("empleados")
    suspend fun getEmpleados(): List<Empleado>
}

object RetrofitClientEmpleados{
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiservice: EmpleadosService = retrofit.create(EmpleadosService::class.java)
}

class EmpleadosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = RetrofitClientEmpleados.apiservice

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaEmpleados by remember {
                    mutableStateOf<List<Empleado>?> (null)}

                LaunchedEffect(Unit) {
                    listaEmpleados = api.getEmpleados()
                    isLoading = false
                }

                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        if(isLoading){
                            // LinearProgressIndicator()
                            CircularProgressIndicator()
                        } else {
                            LazyRow {
                                items(items = listaEmpleados!!){ itemEmpleado ->


                                        DibujarEmpleado(itemEmpleado, modifier = Modifier.fillParentMaxSize())

                                }
                            }

                        }
                    }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable

fun DibujarEmpleado(itemEmpleado: Empleado, modifier: Modifier) {
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current){
        configuration.screenHeightDp.dp.toPx()
    }

    Box (modifier = modifier) {
        AsyncImage(
            API_URL + itemEmpleado.foto, null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = screenHeightPx * 0.75f,
                endY = screenHeightPx * 1f,

            ))
        ){ }

        Column (modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.espacio_2)),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom){
            Text(text = itemEmpleado.nombres + " " + itemEmpleado.apellidos,
                modifier = Modifier.background(Color.White)
                    .padding(horizontal = dimensionResource(R.dimen.espacio_2)))
            Text(text = itemEmpleado.cargo,
                modifier = Modifier.background(Color.Black)
                    .padding(horizontal = dimensionResource(R.dimen.espacio_2)),
                color = Color.White)

        }
    }
}