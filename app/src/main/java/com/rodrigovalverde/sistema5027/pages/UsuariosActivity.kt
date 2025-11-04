package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <-- IMPORTAR FLECHA
import androidx.compose.material.icons.filled.Add
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

class UsuariosActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(this)
        val usuarioDao = database.usuarioDao()

        setContent {
            Sistema5027Theme {
                val context = LocalContext.current
                val usuariosList by usuarioDao.getAll().collectAsState(initial = emptyList())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Gestión de Usuarios (Room)") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            // Inicia la actividad de Inserción
                            context.startActivity(Intent(context, UsuarioInsertActivity::class.java))
                        }) {
                            Icon(Icons.Filled.Add, "Añadir Usuario")
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(usuariosList) { usuario ->
                            UsuarioItem(
                                usuario = usuario,
                                onUsuarioClick = {
                                    // Inicia la actividad de Actualización
                                    val intent = Intent(context, UsuarioUpdateActivity::class.java)
                                    intent.putExtra("USUARIO_ID", usuario.id)
                                    context.startActivity(intent)
                                }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioItem(usuario: Usuario, onUsuarioClick: (Usuario) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUsuarioClick(usuario) } // <-- Hacemos el item clickeable
            .padding(vertical = 8.dp),
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
            Text("${usuario.apellidos}, ${usuario.nombres}", style = MaterialTheme.typography.titleMedium)
            Text("Edad: ${usuario.edad}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}