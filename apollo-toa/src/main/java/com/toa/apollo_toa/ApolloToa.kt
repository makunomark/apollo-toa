package com.toa.apollo_toa

import kotlin.reflect.*
import kotlin.reflect.full.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class ApolloToa {

    fun <T : Any> convert(it: Any, kClass: KClass<T>): KClass<T> {

        val objectMap: MutableMap<Any, Any?> = HashMap<Any, Any?>()

        it::class.memberProperties.forEach {member->
            member.isAccessible = true
            objectMap.put(member.name, member.getter.call(it))
        }


        kClass.memberProperties.forEach{kclProp ->
            (kclProp as KMutableProperty<*>).setter.call(kClass, objectMap.get(kclProp.name))
            println("${kclProp.name} -> ${kclProp.getter.call(kClass)}")
        }
 //TODO Cast kclProp to KMutableProperty in order .access setter https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-mutable-property1/index.html

        return kClass
    }
}
