package com.enterprise.deeplink.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {

    @Serializable
    data object HomeKey : AppRoutes, NavKey

    @Serializable
    data class UserKey(val filter: String) : AppRoutes, NavKey

    @Serializable
    data class SearchKey(
        val firstName: String?,
        val lastName: String?,
        val age: Int?
    ) : AppRoutes, NavKey

}