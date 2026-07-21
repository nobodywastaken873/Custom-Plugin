package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FairyEars: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FAIRY_EARS

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(221, 140, 237)
    private val name = text("Fairy Ears", color)
    private val lore = Utils.loreBlockToList(
        text("Shrink down to under 1 block in height, but lose 75% of your max HP.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.SCALE, -0.5, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.MAX_HEALTH, -0.75, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.HEAD),
        )
        .build()

}