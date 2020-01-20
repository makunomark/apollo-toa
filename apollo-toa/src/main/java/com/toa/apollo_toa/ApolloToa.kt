package com.toa.apollo_toa

import kotlin.reflect.KClass

interface ApolloToa {
    fun <T : Any> convert(input: Any, kClass: KClass<T>): T

    fun <T : Any> convertList(listInput: List<Any>?, kClass: KClass<T>): List<T>

    fun stripUnderscores(name: String): String
}