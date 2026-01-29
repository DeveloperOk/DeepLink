package com.enterprise.deeplink.deeplink

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer


data class DeepLinkMatchResult<T : NavKey>(
    val serializer: KSerializer<T>,
    val args: Map<String, Any>
)
