package com.rodrigovalverde.sistema5027

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.rodrigovalverde.sistema5027.pages.ClientesActivity
import com.rodrigovalverde.sistema5027.pages.DirectoresActivity
import com.rodrigovalverde.sistema5027.pages.EmpleadosActivity
import com.rodrigovalverde.sistema5027.pages.ProvedoresActivity
import com.rodrigovalverde.sistema5027.pages.TiendaActivity
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme

class InicioActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titulos = arrayOf("Proveedores", "Empleados", "Tienda", "Clientes", "Directores", "Salir")
        var iconosCard = arrayOf(
            Icons.Filled.AccountBox, Icons.Filled.Face, Icons.Filled.ShoppingCart,
            Icons.Filled.Person, Icons.Filled.Notifications, Icons.Filled.Close
        )
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                Scaffold (
                    topBar = {
                        TopAppBar(title = {Text(text = stringResource(R.string.title_activity_inicio))},
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                titleContentColor = Color.White
                            ))
                    }
                ){
                    Column (modifier = Modifier.padding(it)) {
                        AsyncImage(model = "https://www.telemundo.com/sites/nbcutelemundo/files/images/promo/article/2016/07/15/mujer-comprando-rabanos-en-farmers-market.jpg",
                            contentDescription = null)
                        Text(
                            text = stringResource(R.string.title_activity_inicio),
                            style = MaterialTheme.typography.headlineLarge
                        )

                        LazyVerticalGrid(
                            modifier = Modifier.padding(dimensionResource(R.dimen.espacio_3)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.espacio_2)),
                            columns = GridCells.Fixed(2)
                        ) {
                            items(titulos.size) {index ->
                                Card(modifier = Modifier.clickable{
                                    seleccionarCard(index)
                            }){
                                    Column(
                                        modifier = Modifier.padding(dimensionResource(R.dimen.espacio_4))
                                    ) {
                                        Icon(iconosCard[index], contentDescription = null)
                                        Text(text = titulos[index])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun seleccionarCard(index: Int) {
        Log.d("Posicion",index.toString())
        when (index) {
            0 -> startActivity(Intent(this, ProvedoresActivity::class.java))
            1 -> startActivity(Intent(this, EmpleadosActivity::class.java))
            2 -> startActivity(Intent(this, TiendaActivity::class.java))
            3 -> startActivity(Intent(this, ClientesActivity::class.java))
            4 -> startActivity(Intent(this, DirectoresActivity::class.java))
            else -> finish()
        }
    }
}
