package com.enterprise.deeplink.deeplink

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.encoding.CompositeDecoder
import java.io.Serializable


class DeepLinkPattern<T : NavKey>(
    val serializer: KSerializer<T>,
    val uriPattern: Uri
) {
    /**
     * Help differentiate if a path segment is an argument or a static value
     */
    private val regexPatternFillIn = Regex("\\{(.+?)\\}")

    // TODO make these lazy
    /**
     * parse the path into a list of [PathSegment]
     *
     * order matters here - path segments need to match in value and order when matching
     * requested deeplink to supported deeplink
     */
    val pathSegments: List<PathSegment> = buildList {
        uriPattern.pathSegments.forEach { segment ->
            // first, check if it is a path arg
            var result = regexPatternFillIn.find(segment)
            if (result != null) {
                // if so, extract the path arg name (the string value within the curly braces)
                val argName = result.groups[1]!!.value
                // from [T], read the primitive type of this argument to get the correct type parser
                val elementIndex = serializer.descriptor.getElementIndex(argName)
                if (elementIndex == CompositeDecoder.UNKNOWN_NAME) {
                    throw IllegalArgumentException(
                        "Path parameter '{$argName}' defined in the DeepLink $uriPattern does not exist in the Serializable class '${serializer.descriptor.serialName}'."
                    )
                }

                val elementDescriptor = serializer.descriptor.getElementDescriptor(elementIndex)
                // finally, add the arg name and its respective type parser to the map
                add(PathSegment(argName, true, getTypeParser(elementDescriptor.kind)))
            } else {
                // if its not a path arg, then its just a static string path segment
                add(PathSegment(segment, false, getTypeParser(PrimitiveKind.STRING)))
            }
        }
    }

    /**
     * Parse supported queries into a map of queryParameterNames to [TypeParser]
     *
     * This will be used later on to parse a provided query value into the correct KType
     */
    val queryValueParsers: Map<String, TypeParser> = buildMap {
        uriPattern.queryParameterNames.forEach { paramName ->
            val elementIndex = serializer.descriptor.getElementIndex(paramName)
            if (elementIndex == CompositeDecoder.UNKNOWN_NAME) {
                throw IllegalArgumentException(
                    "Query parameter '$paramName' defined in the DeepLink $uriPattern does not exist in the Serializable class '${serializer.descriptor.serialName}'."
                )
            }
            val elementDescriptor = serializer.descriptor.getElementDescriptor(elementIndex)
            this[paramName] = getTypeParser(elementDescriptor.kind)
        }
    }

    /**
     * Metadata about a supported path segment
     */
    class PathSegment(
        val stringValue: String,
        val isParamArg: Boolean,
        val typeParser: TypeParser
    )
}

/**
 * Parses a String into a Serializable Primitive
 */
private typealias TypeParser = (String) -> Serializable

private fun getTypeParser(kind: SerialKind): TypeParser {
    return when (kind) {
        PrimitiveKind.STRING -> Any::toString
        PrimitiveKind.INT -> String::toInt
        PrimitiveKind.BOOLEAN -> String::toBoolean
        PrimitiveKind.BYTE -> String::toByte
        PrimitiveKind.CHAR -> String::toCharArray
        PrimitiveKind.DOUBLE -> String::toDouble
        PrimitiveKind.FLOAT -> String::toFloat
        PrimitiveKind.LONG -> String::toLong
        PrimitiveKind.SHORT -> String::toShort
        else -> throw IllegalArgumentException(
            "Unsupported argument type of SerialKind:$kind. The argument type must be a Primitive."
        )
    }
}