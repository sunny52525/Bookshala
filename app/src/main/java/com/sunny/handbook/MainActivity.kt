package com.sunny.handbook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.FirebaseApp
import com.sunny.handbook.data.PreferenceManager
import com.sunny.handbook.ui.screen.LoginScreen
import com.sunny.handbook.ui.theme.HandbookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandbookTheme {
                LoginScreen()
            }
        }

        installSplashScreen()
    }

    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this)
        PreferenceManager.initialize(this)
        PreferenceManager.isLogin.then {
            Intent(this, Home::class.java).run {
                startActivity(this)
                finish()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Hello $name!"
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HandbookTheme {
        Greeting("Android")
    }
}