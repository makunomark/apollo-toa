package com.toa.apollo_toa

import kotlin.reflect.*
import kotlin.reflect.full.cast
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.kotlinProperty

class ApolloToa {

    /**
     * Convert single non-nested object T
     */
    fun <T : Any> convert(input: Any, kClass: KClass<T>): T {

        val objectMap: MutableMap<Any, Any?> = HashMap<Any, Any?>()

        input::class.declaredMemberProperties.forEach { member ->
            member.isAccessible = true
            objectMap.put(member.name, member.getter.call(input))
        }

        val instance = kClass.createInstance()

        instance::class.memberProperties.forEach { kprop ->
            kprop.isAccessible = true
            (kprop as KMutableProperty<*>).setter.call(instance, objectMap.get(kprop.name))
        }

        return instance
    }

    /**
     * Convert list of non-nested objects <T>
     */
    fun<T: Any> convertList(listInput: List<Any>?, kClass: KClass<T>): List<T> {
        val tlist = ArrayList<T>()

        listInput?.forEach { item ->
            tlist.add(convert(item, kClass))
        }

        return tlist
    }
}
