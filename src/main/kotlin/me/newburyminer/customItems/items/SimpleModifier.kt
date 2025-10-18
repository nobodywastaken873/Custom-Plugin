package me.newburyminer.customItems.items

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup

data class SimpleModifier(
    val attribute: Attribute,
    val amount: Double,
    val operation: AttributeModifier.Operation,
    val slot: EquipmentSlotGroup)