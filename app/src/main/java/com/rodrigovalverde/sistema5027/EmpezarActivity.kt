package com.rodrigovalverde.sistema5027

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.pages.UsuariosActivity

class EmpezarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Column {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Image(
                            painterResource(R.drawable.foto_empezar),
                            contentDescription = stringResource(R.string.foto_empezar),
                            modifier = Modifier.height(400.dp).fillMaxWidth(), // Añadido fillMaxWidth
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            modifier = Modifier.padding(dimensionResource(R.dimen.espacio_3)),
                            text = stringResource(R.string.empezar),
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(dimensionResource(R.dimen.espacio_3)),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.texto_empezar))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    startActivity(Intent(this@EmpezarActivity, UsuariosActivity::class.java))
                                }
                            ) {
                                Text(text = "Usuarios (Room)")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = {
                                    startActivity(Intent(this@EmpezarActivity, TerminosActivity::class.java))
                                }) {
                                    Text(text = stringResource(R.string.title_activity_terminos))
                                }
                                Button(onClick = {
                                    startActivity(Intent(this@EmpezarActivity, InicioActivity::class.java))
                                }) {
                                    Text(text = stringResource(R.string.title_activity_inicio))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
