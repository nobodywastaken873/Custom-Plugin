package me.newburyminer.customItems.items.customs.armor.sets.assassin

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class AssassinsLoafers: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ASSASSINS_LOAFERS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(32, 2, 112)
    private val name = text("Assassin's Loafers", color)
    private val lore = Utils.loreBlockToList(
        text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
        text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.005, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ATTACK_SPEED, 0.05, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .setArmorSet(ArmorSet.ASSASSIN)
        .build()

    override fun handle(ctx: EventContext) {}

}