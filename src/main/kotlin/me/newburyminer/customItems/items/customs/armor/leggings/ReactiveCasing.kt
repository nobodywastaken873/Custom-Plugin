package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class ReactiveCasing: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REACTIVE_CASING

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(116, 138, 117)
    private val name = Utils.text("Reactive Casing", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Upon entering combat, gain Resistance IV (10s), Fire Resistance (20s), and Regeneration II (20s).", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 9.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    // CURRENTLY, FUNCTIONALITY IS IN SystemsListener#putInCombat()

}