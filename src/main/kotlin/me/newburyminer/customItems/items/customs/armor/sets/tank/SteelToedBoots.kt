package me.newburyminer.customItems.items.customs.armor.sets.tank

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class SteelToedBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.STEEL_TOED_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(99, 97, 85)
    private val name = text("Steel-toed Boots", color)
    private val lore = Utils.loreBlockToList(
        text(""),
        text("Full Set Bonus (4 pieces): Tank Set", Utils.GRAY),
        text("Sneak to gain 10 absorption hearts, with a 60 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 7.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .setArmorSet(ArmorSet.TANK)
        .build()

    override fun handle(ctx: EventContext) {
    }

}