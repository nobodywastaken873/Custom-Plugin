package me.newburyminer.customItems.helpers

interface DeserializationConversion {
    fun Any?.asInt(): Int {
        return (this as Number).toInt()
    }
    fun Any?.asDouble(): Double {
        return (this as Number).toDouble()
    }
    fun Any?.asFloat(): Float {
        return (this as Number).toFloat()
    }
    fun Any?.asString(): String {
        return (this as String)
    }
    fun Any?.asBoolean(): Boolean {
        return (this as Boolean)
    }
}