package me.newburyminer.customItems.effects

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

class AttributeData(
    val amount: Double,
    val attribute: Attribute,
    val operation: AttributeModifier.Operation,
)