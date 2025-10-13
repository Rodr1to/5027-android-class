package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.datosUsuario
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.rodrigovalverde.sistema5027.utils.UserStore
import kotlinx.coroutines.launch

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                var mostrarVentana by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.espacio_5)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.title_activity_login),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_3)))
                    Text(datosUsuario.get("nombres").toString())
                    Text(datosUsuario.get("cargo").toString())
                    Text(datosUsuario.get("empresa").toString())

                    Button(onClick = {
                        mostrarVentana = true
                    }) {
                        Text("Cerrar sesion")
                    }
                if (mostrarVentana) {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Cerrar sesion") },
                        text = { Text("¿Está seguro de que desea cerrar sesión?") },
                        confirmButton = {
                            Button(onClick = {
                                lifecycleScope.launch {
                                    val userStore = UserStore(this@PerfilActivity)
                                    userStore.guardarDatosUsuario("")
                                    finish()
                                }
                            }) { Text("Sí") }
                        },
                        dismissButton = {
                            Button(onClick = { mostrarVentana = false }) {
                                Text("No")
                            }
                        }
                    )
                }
                }
            }
        }
    }
}
