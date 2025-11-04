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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.rodrigovalverde.sistema5027.room.AppDatabase
import com.rodrigovalverde.sistema5027.room.Usuario
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuariosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(this)
        val usuarioDao = database.usuarioDao()

        setContent {
            Sistema5027Theme {
                UsuariosScreen(usuarioDao)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(usuarioDao: com.rodrigovalverde.sistema5027.room.UsuarioDao) {
    val context = LocalContext.current
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val usuariosList by usuarioDao.getAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Gestión de Usuarios (Room)", style = MaterialTheme.typography.headlineLarge)

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

            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Elegir Foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nombres.isNotBlank() && apellidos.isNotBlank()) {
                        val nuevoUsuario = Usuario(
                            nombres = nombres,
                            apellidos = apellidos,
                            foto = fotoUri?.toString()
                        )
                        scope.launch(Dispatchers.IO) {
                            usuarioDao.insert(nuevoUsuario)
                        }
                        // Limpiar campos
                        nombres = ""
                        apellidos = ""
                        fotoUri = null
                        Toast.makeText(context, "Usuario guardado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Complete los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(usuariosList) { usuario ->
                    UsuarioItem(usuario = usuario, onEliminar = {
                        scope.launch(Dispatchers.IO) {
                            usuarioDao.delete(usuario)
                        }
                    })
                    Divider()
                }
            }
        }
    }
}

@Composable
fun UsuarioItem(usuario: Usuario, onEliminar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (usuario.foto != null) {
            Image(
                painter = rememberAsyncImagePainter(model = Uri.parse(usuario.foto)),
                contentDescription = "Foto de ${usuario.nombres}",
                modifier = Modifier.size(50.dp)
            )
        } else {
            Icon(Icons.Filled.Person, contentDescription = "Sin foto", modifier = Modifier.size(50.dp))
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Text("${usuario.nombres} ${usuario.apellidos}")
        }

        IconButton(onClick = onEliminar) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}