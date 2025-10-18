package me.newburyminer.customItems.items.customs.armor.sets.warrior

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class WarriorGreaves: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WARRIOR_GREAVES

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(204, 116, 2)
    private val name = text("Warrior Greaves", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 7.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.MAX_HEALTH, 1.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .setArmorSet(ArmorSet.WARRIOR)
        .build()

    override fun handle(ctx: EventContext) {}

}