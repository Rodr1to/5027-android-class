package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import com.rodrigovalverde.sistema5027.utils.UserStore
import com.rodrigovalverde.sistema5027.utils.datosUsuario
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST



interface DirectoresInsertarService{
    @FormUrlEncoded
    @POST("directoresinsert.php")
    suspend fun directoresInsertar(
        @Field("nombres") nombres: String,
        @Field("peliculas") peliculas: String
    ): String
}

object RetrofitDirectoresInsertar {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    val apiService: DirectoresInsertarService = retrofit.create(DirectoresInsertarService::class.java)
}
class DirectoresInsertarActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class) // <-- AÑADIR ANOTACIÓN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                var nombres by remember { mutableStateOf("") }
                var peliculas by remember { mutableStateOf("") }

                val api = RetrofitDirectoresInsertar.apiService

                // --- AÑADIR SCAFFOLD Y TOPAPPBAR ---
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Nuevo director") },
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
                        // --- ELIMINAR TÍTULO DE AQUÍ (YA ESTÁ ARRIBA) ---
                        // Text(text = "Nuevo director",
                        //     style = MaterialTheme.typography.headlineLarge)
                        // Spacer(Modifier.height(dimensionResource(R.dimen.espacio_4)))

                        OutlinedTextField(
                            label = {Text(text = "Nombres")},
                            value = nombres,
                            onValueChange = { nombres = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                        OutlinedTextField(
                            label = {Text(text= "Peliculas")},
                            value = peliculas,
                            onValueChange = { peliculas = it },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                        OutlinedButton (onClick = {
                            lifecycleScope.launch {
                                val respuesta = api.directoresInsertar(nombres, peliculas)
                                Toast.makeText(this@DirectoresInsertarActivity,
                                    "Se ha agregado un nuevo director con codigo $respuesta",
                                    Toast.LENGTH_SHORT).show()

                                // --- CORREGIR EL LOOP ---
                                // Reemplaza esto:
                                // startActivity(Intent(this@DirectoresInsertarActivity,
                                //    DirectoresActivity::class.java))
                                // Con esto:
                                finish()
                                // --- FIN DE LA MODIFICACIÓN ---
                            }
                        }) {
                            Text(stringResource(R.string.title_activity_directores_insertar))
                        }
                    }
                }
            }
        }
    }
}