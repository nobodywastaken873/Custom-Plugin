package me.newburyminer.customItems.effects

import me.newburyminer.customItems.helpers.DeserializationConversion

data class EffectData(
    val duration: Int,
    val attributeData: AttributeData? = null,
    val unique: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (other !is EffectData) return false
        if (other === this) return true
        if (attributeData != other.attributeData) return false
        if (unique != other.unique) return false
        return true
    }

    @Suppress("UNCHECKED_CAST")
    companion object: DeserializationConversion {
        fun deserialize(map: Map<String, Any?>): EffectData {
            val duration = map["duration"].asInt()
            val attribute = AttributeData.deserialize(map["attribute"] as Map<String, Any>?)
            val unique = map["unique"].asBoolean()
            return EffectData(duration, attribute, unique)
        }
    }

    fun serialize(): Map<String, Any?> {
        return mapOf(
            "duration" to duration,
            "attribute" to attributeData?.serialize(),
            "unique" to unique
        )
    }

    override fun hashCode(): Int {
        var result = duration
        result = 31 * result + unique.hashCode()
        result = 31 * result + (attributeData?.hashCode() ?: 0)
        return result
    }
}