package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface DirectoresActualizarService{
    @FormUrlEncoded
    @POST("directoresupdate.php")
    suspend fun directoresActualizar(
        @Field("iddirector") iddirector: Int,
        @Field("nombres") nombres: String,
        @Field("peliculas") peliculas: String
    ): String
}

object RetrofitDirectoresActualizar {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    val apiService: DirectoresActualizarService = retrofit.create(DirectoresActualizarService::class.java)
}

class DirectoresActualizarActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class) // <-- AÑADIR ANOTACIÓN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val bundle = intent.extras //para poder leer datos que se envían a este activity
        val viddirector = bundle!!.getInt("iddirector")
        val vnombres = bundle.getString("nombres")
        val vpeliculas = bundle.getString("peliculas")

        setContent {
            Sistema5027Theme {
                var iddirector by remember { mutableStateOf(viddirector.toString()) }
                var nombres by remember { mutableStateOf(vnombres) }
                var peliculas by remember { mutableStateOf(vpeliculas) }
                val api = RetrofitDirectoresActualizar.apiService

                // --- AÑADIR SCAFFOLD Y TOPAPPBAR ---
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Actualizar director") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) { // finish() vuelve a la lista
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // --- FIN DE LA MODIFICACIÓN ---

                    Column (modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding) // <-- USAR PADDING DEL SCAFFOLD
                        .padding(dimensionResource(R.dimen.espacio_4)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        // --- ELIMINAR TÍTULO DE AQUÍ ---
                        // Text(text = "Actualizar director",
                        //     style = MaterialTheme.typography.headlineLarge)
                        // Spacer(Modifier.height(dimensionResource(R.dimen.espacio_4)))

                        OutlinedTextField(
                            label = {Text(text = "Codigo")},
                            value = iddirector.toString(),
                            onValueChange = {  },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.espacio_4)))
                        OutlinedTextField(
                            label = {Text(text = "Nombres")},
                            value = nombres.toString(),
                            onValueChange = { nombres = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                        OutlinedTextField(
                            label = {Text(text= "Peliculas")},
                            value = peliculas.toString(),
                            onValueChange = { peliculas = it },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                        OutlinedButton (onClick = {
                            lifecycleScope.launch {
                                val respuesta = api.directoresActualizar(Integer.parseInt(iddirector), nombres.toString(), peliculas.toString())
                                Toast.makeText(this@DirectoresActualizarActivity,
                                    "Se ha actualizado el director con codigo $iddirector",
                                    Toast.LENGTH_SHORT).show()

                                // --- CORREGIR EL LOOP ---
                                // Reemplaza esto:
                                // startActivity(Intent(this@DirectoresActualizarActivity,
                                //    DirectoresActivity::class.java))
                                // Con esto:
                                finish()
                                // --- FIN DE LA MODIFICACIÓN ---
                            }
                        }) {
                            Text(stringResource(R.string.title_activity_directores_actualizar))
                        }
                    }
                }
            }
        }
    }
}