package me.newburyminer.customItems.effects

data class EffectData(
    val duration: Int,
    val attributeData: AttributeData? = null,
    val unique: Boolean = false
)