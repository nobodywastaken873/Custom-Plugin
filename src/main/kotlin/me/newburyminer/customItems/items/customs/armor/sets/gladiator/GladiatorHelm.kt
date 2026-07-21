package me.newburyminer.customItems.items.customs.armor.sets.gladiator

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class GladiatorHelm: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GLADIATORS_HELM

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(204, 116, 2)
    private val name = text("Gladiator Helm", color)
    private val lore = Utils.loreBlockToList(
        text("Deal an additional 10% damage with spears.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Gladiator's Set", Utils.GRAY),
        text("Right click with a spear to charge forward, with a 5s cooldown. Gain 20% additional damage with spears.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.005, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
        )
        .setArmorSet(ArmorSet.GLADIATOR)
        .build()

}