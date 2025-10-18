package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class Toolbelt: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOOLBELT

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(125, 63, 5)
    private val name = text("Toolbelt", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.BLOCK_INTERACTION_RANGE, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    override fun handle(ctx: EventContext) {}

}