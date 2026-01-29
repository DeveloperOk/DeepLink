package com.enterprise.deeplink.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.enterprise.deeplink.deeplink.DeepLinkMatcher
import com.enterprise.deeplink.deeplink.DeepLinkRequest
import com.enterprise.deeplink.deeplink.KeyDecoder
import com.enterprise.deeplink.deeplink.deepLinkPatterns
import com.enterprise.deeplink.screens.HomeScreen
import com.enterprise.deeplink.screens.SearchScreen
import com.enterprise.deeplink.screens.UserScreen

@Composable
fun AppNavigation(uri: Uri?) {

    val key: NavKey = parseDeepLink(uri) ?: AppRoutes.HomeKey

    //With rememberNavBackStack, backstack survives the configuration changes such as screen rotation etc.
    val backStack = rememberNavBackStack(
        //NavigationStartScreen
        key
    )

    NavDisplay(
        backStack = backStack,

        onBack = { backStack.removeLastOrNull() },

        //In order to clear viewmodel, on navigation back
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        entryProvider = entryProvider {

            entry<AppRoutes.HomeKey> {
                HomeScreen()
            }

            entry<AppRoutes.UserKey> { userKey ->
                UserScreen(userKey = userKey)
            }

            entry<AppRoutes.SearchKey> { searchKey ->
                SearchScreen(searchKey = searchKey)
            }

        }
    )
}


private fun parseDeepLink(uri: Uri?): NavKey? {
    return uri?.let {
        val request = DeepLinkRequest(it)

        val match = deepLinkPatterns.firstNotNullOfOrNull { pattern ->
            val tempDeepLinkMatcher = DeepLinkMatcher(request, pattern)
            tempDeepLinkMatcher.match()
        }

        match?.let {
            KeyDecoder(match.args).decodeSerializableValue(match.serializer)
        } as NavKey?
    }
}