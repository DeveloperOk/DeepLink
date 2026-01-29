package com.enterprise.deeplink.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enterprise.deeplink.navigation.AppRoutes

@Composable
fun UserScreen(userKey: AppRoutes.UserKey) {

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("UserScreen")
        Text("Input: " + userKey.filter)

    }

}