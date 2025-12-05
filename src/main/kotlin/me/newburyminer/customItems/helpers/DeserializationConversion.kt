package me.newburyminer.customItems.helpers

interface DeserializationConversion {
    fun Any?.toInt(): Int {
        return (this as Number).toInt()
    }
    fun Any?.toDouble(): Double {
        return (this as Number).toDouble()
    }
    fun Any?.toFloat(): Float {
        return (this as Number).toFloat()
    }
    fun Any?.toString(): String {
        return (this as String)
    }
    fun Any?.toBoolean(): Boolean {
        return (this as Boolean)
    }
}