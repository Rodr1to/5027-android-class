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
import com.rodrigovalverde.sistema5027.models.Proveedor
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProveedoresService {
    @GET("proveedores")
    suspend fun getProveedores(): List<Proveedor>
}

class ProvedoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = Retrofit.Builder()
            .baseUrl("https://servicios.campus.pe/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProveedoresService::class.java)

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaProveedores by remember {
                    mutableStateOf<List<Proveedor>?> (null)}

                LaunchedEffect(Unit) {
                    listaProveedores = api.getProveedores()
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){
                        if(isLoading){
                            LinearProgressIndicator()
                        } else {
                            LazyColumn {
                                /*
                                items(listaProveedores!!.size) {
                                    Text(text = listaProveedores!![it].nombreempresa)
                                    Text(text = listaProveedores!![it].nombrecontacto)
                                    Text(text = listaProveedores!![it].cargocontacto)
                                }
                                */
                                items(items = listaProveedores!!){ itemProveedor ->
                                   DibujarProveedor(itemProveedor)
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

fun DibujarProveedor(itemProveedor: Proveedor) {
    Text(text = itemProveedor.nombreempresa)
    Text(text = itemProveedor.nombrecontacto)
    Text(text = itemProveedor.cargocontacto)
}