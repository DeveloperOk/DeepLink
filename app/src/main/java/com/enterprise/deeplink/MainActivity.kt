package com.enterprise.deeplink

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enterprise.deeplink.navigation.AppNavigation
import com.enterprise.deeplink.ui.theme.DeepLinkTheme

class MainActivity : ComponentActivity() {

    //Links
    //https://medium.com/@kadirtas02/deep-link-handling-in-jetpack-navigation-3-e1c6383d2dd4
    //https://developer.android.com/guide/navigation/navigation-3/recipes/deeplinks-basic
    //Run below commands from adb.exe folder
    //.\adb.exe shell am start -n com.enterprise.deeplink/.MainActivity -a android.intent.action.VIEW -d "https://www.myapp.com/home"
    //.\adb.exe shell am start -n com.enterprise.deeplink/.MainActivity -a android.intent.action.VIEW -d "https://www.myapp.com/users/test"
    //.\adb.exe shell am start -n com.enterprise.deeplink/.MainActivity -a android.intent.action.VIEW -d "https://www.myapp.com/search?firstName=fahrettin\&lastName=ok\&age=36"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uri: Uri? = intent.data

        setContent {
            DeepLinkTheme {
                DeepLinkApp(uri = uri)
            }
        }
    }
}

@Composable
fun DeepLinkApp(uri: Uri?) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            AppNavigation(uri = uri)

        }
    }
}

