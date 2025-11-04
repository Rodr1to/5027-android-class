package com.rodrigovalverde.sistema5027.pages

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil3.compose.rememberAsyncImagePainter
import com.rodrigovalverde.sistema5027.room.AppDatabase
import com.rodrigovalverde.sistema5027.room.Usuario
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioInsertActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(this)
        val usuarioDao = database.usuarioDao()

        setContent {
            Sistema5027Theme {
                val context = LocalContext.current
                var nombres by remember { mutableStateOf("") }
                var apellidos by remember { mutableStateOf("") }
                var edad by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }

                val galleryLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    fotoUri = uri
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Nuevo Usuario") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        OutlinedTextField(
                            value = nombres,
                            onValueChange = { nombres = it },
                            label = { Text("Nombres") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = apellidos,
                            onValueChange = { apellidos = it },
                            label = { Text("Apellidos") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = edad,
                            onValueChange = { edad = it },
                            label = { Text("Edad") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(Modifier.fillMaxWidth()) {
                            Button(onClick = { galleryLauncher.launch("image/*") }) {
                                Text("Elegir Foto")
                            }
                            Spacer(Modifier.width(16.dp))
                            if (fotoUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = fotoUri),
                                    contentDescription = "Foto seleccionada",
                                    modifier = Modifier.size(50.dp)
                                )
                            } else {
                                Icon(Icons.Filled.Person, "Sin foto", modifier = Modifier.size(50.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                val edadInt = edad.toIntOrNull()
                                if (nombres.isNotBlank() && apellidos.isNotBlank() && edadInt != null) {
                                    val nuevoUsuario = Usuario(
                                        nombres = nombres,
                                        apellidos = apellidos,
                                        edad = edadInt,
                                        foto = fotoUri?.toString()
                                    )
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        usuarioDao.insert(nuevoUsuario)
                                    }
                                    Toast.makeText(context, "Usuario guardado", Toast.LENGTH_SHORT).show()
                                    finish() // Cierra la actividad y vuelve a la lista
                                } else {
                                    Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}