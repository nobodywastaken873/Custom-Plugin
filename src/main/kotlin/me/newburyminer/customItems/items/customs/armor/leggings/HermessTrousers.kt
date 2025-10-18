package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class HermessTrousers: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HERMESS_TROUSERS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(145, 192, 219)
    private val name = text("Hermes's Trousers", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 7.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.04, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.WATER_MOVEMENT_EFFICIENCY, 0.3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.STEP_HEIGHT, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    override fun handle(ctx: EventContext) {}

}