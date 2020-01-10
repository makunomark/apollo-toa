package com.toa.apollo_toa

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ApolloToa {

    fun <T : Any> convert(
        it: Any,
        kClass: KClass<T>
    ): KClass<T> {

        for (kCallable in it::class.members) {
            println(
                kCallable.name + " - " + kCallable.returnType + " - " + it.getField(kCallable.name)
            )
        }

//        println(readInstanceProperty<String>(it, "name") + "@@@@@@@--_!!!")

        return kClass
    }

    @Throws(IllegalAccessException::class, ClassCastException::class)
    inline fun <reified T> Any.getField(fieldName: String): T? {
        this::class.memberProperties.forEach { kCallable ->
            if (fieldName == kCallable.name) {
                return kCallable.getter.call(this) as T?
            }
        }
        return null
    }
}
