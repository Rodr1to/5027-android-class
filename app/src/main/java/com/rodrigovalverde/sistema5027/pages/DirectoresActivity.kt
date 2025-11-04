package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <-- 1. IMPORTAR FLECHA
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.models.Director
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.pages.ui.theme.color1
import com.rodrigovalverde.sistema5027.pages.ui.theme.color2
import com.rodrigovalverde.sistema5027.utils.API_URL
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

// Interfaz para el servicio que devuelve JSON (tu código original)
interface DirectoresService {
    @GET("directores.php")
    suspend fun getDirectores(): List<Director>
}

// Interfaz para el servicio que devuelve TEXTO (para el delete)
interface DirectoresDeleteService {
    @FormUrlEncoded
    @POST("directoresdelete.php")
    suspend fun deleteDirector(
        @Field("iddirector") idDirector: Int
    ): String // Espera una respuesta de texto simple
}

// Cliente de Retrofit para JSON (tu código original)
object RetrofitClientDirectores {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiservice: DirectoresService = retrofit.create(DirectoresService::class.java)
}

// NUEVO Cliente de Retrofit para TEXTO
object RetrofitClientDirectoresDelete {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(ScalarsConverterFactory.create()) // Usa el convertidor de texto
        .build()

    val apiservice: DirectoresDeleteService = retrofit.create(DirectoresDeleteService::class.java)
}

class DirectoresActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class) // <-- 2. AÑADIR ANOTACIÓN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiJson = RetrofitClientDirectores.apiservice
        val apiDelete = RetrofitClientDirectoresDelete.apiservice // Nuevo cliente

        setContent {
            Sistema5027Theme {
                var isLoading by remember { mutableStateOf(true) }
                var listaDirectores by remember { mutableStateOf<List<Director>>(emptyList()) }

                // Estados para el diálogo de eliminación
                var showDialog by remember { mutableStateOf(false) }
                var directorAEliminar by remember { mutableStateOf<Director?>(null) }
                val context = LocalContext.current

                fun cargarDirectores() {
                    lifecycleScope.launch {
                        isLoading = true
                        try {
                            listaDirectores = apiJson.getDirectores()
                        } catch (e: Exception) {
                            Log.e("DirectoresActivity", "Error al cargar: ${e.message}")
                        }
                        isLoading = false
                    }
                }

                // Cargar datos al iniciar
                LaunchedEffect(Unit) {
                    cargarDirectores()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    // --- 3. AÑADIR EL TOPAPPBAR ---
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.title_activity_directores),
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) { // finish() vuelve atrás
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            }
                        )
                    },
                    // --- FIN DE LA MODIFICACIÓN ---

                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            // Tu código original apunta a Insertar
                            startActivity(Intent(this, DirectoresInsertarActivity::class.java))
                        }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {

                        // --- 4. ELIMINAR EL TÍTULO DE AQUÍ (YA ESTÁ ARRIBA) ---
                        // Text(
                        //     text = stringResource(R.string.title_activity_directores),
                        //     modifier = Modifier.padding(dimensionResource(R.dimen.espacio_1)),
                        //     style = MaterialTheme.typography.headlineLarge
                        // )

                        if (isLoading) {
                            LinearProgressIndicator()
                        } else {
                            LazyColumn(
                                // Añadimos padding para que no se pegue a la barra
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.espacio_1))
                            ) {
                                items(items = listaDirectores) { itemDirector ->
                                    DibujarDirector(
                                        itemDirector,
                                        onEditClick = {
                                            seleccionarDirector(itemDirector)
                                        },
                                        onDeleteClick = {
                                            directorAEliminar = itemDirector
                                            showDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Diálogo de confirmación
                if (showDialog && directorAEliminar != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmar Eliminación") },
                        text = { Text("¿Estás seguro de que deseas eliminar a ${directorAEliminar!!.nombres}?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    lifecycleScope.launch {
                                        try {
                                            // Usamos el cliente de TEXTO para la llamada de eliminación
                                            val response = apiDelete.deleteDirector(directorAEliminar!!.iddirector)
                                            Log.d("DirectoresActivity", "Respuesta de eliminación: $response")
                                            Toast.makeText(context, "Director eliminado", Toast.LENGTH_SHORT).show()
                                            cargarDirectores() // Recargar la lista
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                            Log.e("DirectoresActivity", "Error al eliminar: ${e.message}")
                                        }
                                        showDialog = false
                                        directorAEliminar = null
                                    }
                                }
                            ) { Text("Eliminar") }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }

    fun seleccionarDirector(itemDirector: Director) {
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
fun DibujarDirector(
    itemDirector: Director,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.espacio_2))
            .border(BorderStroke(3.dp, color2))
            .padding(dimensionResource(R.dimen.espacio_3))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onEditClick)
            ) {
                Text(
                    text = itemDirector.iddirector.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = color1
                )
                Text(text = itemDirector.nombres)
                Text(text = itemDirector.peliculas)
            }
            // Botón de eliminar
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}