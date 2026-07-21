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

class AutoInjectingLeggings: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AUTO_INJECTING_LEGGINGS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(235, 160, 12)
    private val name = Utils.text("Auto-injecting Leggings", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Upon entering combat, gain Speed II (4m), Strength II (4m), and Fire Resistance (8m).", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 7.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    // CURRENTLY, FUNCTIONALITY IS IN SystemsListener#putInCombat()

}