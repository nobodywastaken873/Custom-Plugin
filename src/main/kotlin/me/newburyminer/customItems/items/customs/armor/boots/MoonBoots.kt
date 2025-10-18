package me.newburyminer.customItems.items.customs.armor.boots

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MoonBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MOON_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(191, 218, 245)
    private val name = text("Moon Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.GRAVITY, -0.84, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.JUMP_STRENGTH, 0.05, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.SAFE_FALL_DISTANCE, 20.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .build()

    override fun handle(ctx: EventContext) {}

}