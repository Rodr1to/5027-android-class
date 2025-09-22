package com.rodrigovalverde.sistema5027

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp
import com.rodrigovalverde.sistema5027.ui.theme.Sistema5027Theme
import kotlinx.coroutines.delay

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var iniciarAnimacion by remember {
                mutableStateOf(false)
            }
            val scale by animateFloatAsState(
                targetValue = if(iniciarAnimacion) 1f else 3f,
                animationSpec = tween(durationMillis = 2000)
            )

            val alpha by animateFloatAsState(
                targetValue = if(iniciarAnimacion) 1f else 0f,
                animationSpec = tween(durationMillis = 4000)
            )

            val offsetY by animateDpAsState(
                targetValue = if(iniciarAnimacion) 0.dp else 1500.dp,
                animationSpec = tween(durationMillis = 2000)
            )

            val context = LocalContext.current
            val activity = context as Activity
            LaunchedEffect(key1 = true) {
                iniciarAnimacion = true
                delay(5000)


                val options = ActivityOptions.makeCustomAnimation(
                    activity,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                activity.startActivity(Intent(activity, MainActivity::class.java),
                    options.toBundle())
            }

            Sistema5027Theme {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.logo),
                        modifier = Modifier.height(240.dp)
                            .offset(y = offsetY)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                alpha = alpha
                            ))


                }
            }
        }
    }
}

