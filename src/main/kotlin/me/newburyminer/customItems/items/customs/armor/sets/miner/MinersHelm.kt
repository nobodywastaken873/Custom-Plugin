package me.newburyminer.customItems.items.customs.armor.sets.miner

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MinersHelm: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MINERS_HELM

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(122, 119, 69)
    private val name = text("Miner's Helm", color)
    private val lore = Utils.loreBlockToList(
        text("Gain a slight boost in mining speed.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (3 pieces): Miner's Set", Utils.GRAY),
        text("Gain permanent Haste II.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.MINING_EFFICIENCY, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
        )
        .setArmorSet(ArmorSet.MINER)
        .build()

}