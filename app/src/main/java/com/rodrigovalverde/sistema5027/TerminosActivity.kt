package com.rodrigovalverde.sistema5027

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme

class TerminosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Column (modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.espacio_3),
                        vertical = dimensionResource(R.dimen.espacio_5))){
                   Text(text = stringResource(R.string.title_activity_terminos),
                       style = MaterialTheme.typography.headlineLarge)
                   Text(text = stringResource(R.string.terminos_texto))
                    Button(onClick = {
                        finish() // para que se cierre el activity
                    }) {
                        Text(text = stringResource(R.string.cerrar))
                    }
                }
            }
        }
    }
}
