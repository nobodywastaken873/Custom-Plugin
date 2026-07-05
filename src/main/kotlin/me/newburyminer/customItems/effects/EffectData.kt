package me.newburyminer.customItems.effects

import me.newburyminer.customItems.helpers.DeserializationConversion

data class EffectData(
    val duration: Int,
    val attributeData: AttributeData? = null,
    val unique: Boolean = false
) {
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
}