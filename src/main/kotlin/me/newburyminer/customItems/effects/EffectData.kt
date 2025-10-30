package me.newburyminer.customItems.effects

data class EffectData(
    val duration: Int,
    val attributeData: AttributeData? = null,
    val unique: Boolean = false
) {
    @Suppress("UNCHECKED_CAST")
    companion object {
        fun deserialize(map: Map<String, Any?>): EffectData {
            val duration = map["duration"] as Int
            val attribute = AttributeData.deserialize(map["attribute"] as Map<String, Any>?)
            val unique = map["unique"] as Boolean
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