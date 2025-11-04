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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class UsuarioUpdateActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(this)
        val usuarioDao = database.usuarioDao()

        // Obtenemos el ID del usuario del Intent
        val usuarioId = intent.getIntExtra("USUARIO_ID", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            Sistema5027Theme {
                val context = LocalContext.current

                // Buscamos el usuario en la BD
                val usuarioState = usuarioDao.getUsuarioById(usuarioId).collectAsState(initial = null)
                val usuario = usuarioState.value

                // Estados del formulario
                var nombres by remember { mutableStateOf("") }
                var apellidos by remember { mutableStateOf("") }
                var edad by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }

                // Cuando el usuario se cargue de la BD, llenamos los campos
                LaunchedEffect(usuario) {
                    if (usuario != null) {
                        nombres = usuario.nombres
                        apellidos = usuario.apellidos
                        edad = usuario.edad.toString()
                        fotoUri = usuario.foto?.let { Uri.parse(it) }
                    }
                }

                val galleryLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    fotoUri = uri
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Actualizar Usuario") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    if (usuario == null) {
                        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
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
                                    Text("Cambiar Foto")
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

                            // Botón de Actualizar
                            Button(
                                onClick = {
                                    val edadInt = edad.toIntOrNull()
                                    if (nombres.isNotBlank() && apellidos.isNotBlank() && edadInt != null) {
                                        val usuarioActualizado = Usuario(
                                            id = usuarioId, // Mantenemos el ID original
                                            nombres = nombres,
                                            apellidos = apellidos,
                                            edad = edadInt,
                                            foto = fotoUri?.toString()
                                        )
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            usuarioDao.update(usuarioActualizado)
                                        }
                                        Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                                        finish() // Cierra la actividad y vuelve a la lista
                                    } else {
                                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Actualizar")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botón de Eliminar
                            OutlinedButton(
                                onClick = {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        usuarioDao.delete(usuario)
                                    }
                                    Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                    finish()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}