package com.toa.apollo_toa

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class ApolloToaImpl : ApolloToa {

    /**
     * Convert single non-nested object T
     */
    override fun <T : Any> convert(input: Any, kClass: KClass<T>): T {

        val objectMap: MutableMap<Any, Any?> = HashMap<Any, Any?>()

        input::class.declaredMemberProperties.forEach { member ->

            member.isAccessible = true
            objectMap.put(stripUnderscores(member.name), member.getter.call(input))
        }

        val instance = kClass.createInstance()

        instance::class.memberProperties.forEach { kprop ->
            kprop.isAccessible = true
            (kprop as KMutableProperty<*>).setter.call(
                instance,
                objectMap.get(stripUnderscores(kprop.name))
            )
        }

        return instance
    }

    /**
     * Convert list of non-nested objects List<T>
     */
    override fun <T : Any> convertList(listInput: List<Any>?, kClass: KClass<T>): List<T> {
        val tlist = ArrayList<T>()

        listInput?.forEach { item ->
            tlist.add(convert(item, kClass))
        }

        return tlist
    }


    /**
     * Strip underscores from properties with underscores
     */
    override fun stripUnderscores(name: String): String {
        val nameArray = name.split("_")
        var outString = ""

        nameArray.forEachIndexed { index, s ->
            when (index) {
                0 -> outString = outString.plus(s)
                else -> outString = outString.plus(s.capitalize())
            }
        }
        return outString
    }


}
