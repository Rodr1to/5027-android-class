package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.pages.ui.theme.Sistema5027Theme

class ClientesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Column (modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    Text(stringResource(R.string.title_activity_clientes),
                        style = MaterialTheme.typography.headlineLarge)
                    Button(onClick = {
                        startActivity(Intent(this@ClientesActivity, LoginActivity::class.java))
                    }) {
                        Text("Ingrese al área de clientes")
                    }
                }
            }
        }
    }
}