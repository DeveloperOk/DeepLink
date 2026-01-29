package com.enterprise.deeplink.deeplink

import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import com.enterprise.deeplink.navigation.AppRoutes

val deepLinkPatterns: List<DeepLinkPattern<out NavKey>> = listOf(
    // Exact match: "https://www.myapp.com/home"
    //Example: "https://www.myapp.com/home"
    DeepLinkPattern(
        AppRoutes.HomeKey.serializer(),
        "https://www.myapp.com/home".toUri()
    ),

    // Path arguments: "https://www.myapp.com/users/{filter}"
    //Example: "https://www.myapp.com/users/test"
    DeepLinkPattern(
        AppRoutes.UserKey.serializer(),
        "https://www.myapp.com/users/{filter}".toUri()
    ),

    //Query arguments: "https://www.myapp.com/search?firstName=...&age=..."
    //Example: "https://www.myapp.com/search?firstName=fahrettin\&lastName=ok\&age=36"
    DeepLinkPattern(
        AppRoutes.SearchKey.serializer(),
        "https://www.myapp.com/search?firstName={firstName}&lastName={lastName}&age={age}".toUri()
    )
)