package com.rodrigovalverde.sistema5027

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Column (
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = dimensionResource(R.dimen.espacio_4)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = stringResource(R.string.frase),
                        style = MaterialTheme.typography.titleLarge)
                    Text(text = stringResource(R.string.autor))
                }
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.logo),
                        modifier = Modifier.height(240.dp))


                }

                Column (
                    modifier = Modifier.fillMaxSize()
                        .padding(bottom = dimensionResource(R.dimen.espacio_4)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom){
                    Text(text = stringResource(R.string.saludo),
                        style = MaterialTheme.typography.displayLarge)
                    Button(onClick = {
                        startActivity(Intent(this@MainActivity, EmpezarActivity::class.java))
                    }) {
                        Text(text = stringResource(R.string.empezar))
                    }
                    Text(text = stringResource(R.string.derechos),
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.espacio_5)))
                }
            }
        }
    }
}

