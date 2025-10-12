package com.rodrigovalverde.sistema5027.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.lifecycleScope
import com.rodrigovalverde.sistema5027.R
import com.rodrigovalverde.sistema5027.models.Producto
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import com.rodrigovalverde.sistema5027.utils.API_URL
import com.rodrigovalverde.sistema5027.utils.UserStore
import com.rodrigovalverde.sistema5027.utils.datosUsuario
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    @FormUrlEncoded
    @POST("login.php")
    suspend fun getLogin(
        @Field("correotelefono") correotelefono: String,
        @Field("clave") clave: String
    ): String
}
object RetrofitClientLogin {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    val apiService: LoginService = retrofit.create(LoginService::class.java)
}

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema5027Theme {
                var correotelefono by remember { mutableStateOf("") }
                var clave by remember { mutableStateOf("") }
                var estadoCheck by remember { mutableStateOf(false) }

                val api = RetrofitClientLogin.apiService

                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.espacio_4)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    Text(stringResource(R.string.title_activity_login),
                        style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_4)))
                    OutlinedTextField(
                        label = {Text(stringResource(R.string.correo_telefono))},
                        value = correotelefono,
                        onValueChange = { correotelefono = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                    OutlinedTextField(
                        label = {Text(stringResource(R.string.clave))},
                        value = clave,
                        onValueChange = { clave = it },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Checkbox(checked = estadoCheck,
                            onCheckedChange = { estadoCheck = it})
                        Text("Guardar sesión")

                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.espacio_2)))
                    OutlinedButton (onClick = {
                        lifecycleScope.launch {
                            val respuesta = api.getLogin(correotelefono, clave)
                            Log.d("LOGIN", respuesta)
                            if(respuesta == "-1"){
                                Toast.makeText(this@LoginActivity,
                                    "La cuenta no existe", Toast.LENGTH_SHORT).show()
                            } else if (respuesta == "-2"){
                                Toast.makeText(this@LoginActivity,
                                    "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@LoginActivity,
                                    "Bienvenido", Toast.LENGTH_SHORT).show()
                                datosUsuario = JSONArray(respuesta).getJSONObject(0)

                                if(estadoCheck){
                                    var userStore = UserStore(this@LoginActivity)
                                    userStore.guardarDatosUsuario(respuesta)

                                }
                                startActivity(Intent(this@LoginActivity, PerfilActivity::class.java))

                            }
                        }
                    }) {
                        Text(stringResource(R.string.title_activity_login))
                    }
                }
            }
        }
    }
}
