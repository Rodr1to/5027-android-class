package com.rodrigovalverde.sistema5027.pages

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.datosUsuario

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.espacio_4)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    Text(stringResource(R.string.title_activity_perfil),
                        style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_3)))
                    Text(datosUsuario.get("nombres").toString())
                    Text(datosUsuario.get("cargo").toString())
                    Text(datosUsuario.get("empresa").toString())
                }
            }
        }
    }
}
