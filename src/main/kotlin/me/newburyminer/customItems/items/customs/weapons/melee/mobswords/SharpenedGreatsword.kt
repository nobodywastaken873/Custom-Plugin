package me.newburyminer.customItems.items.customs.weapons.melee.mobswords

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addExtraSlayer
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class SharpenedGreatsword: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHARPENED_GREATSWORD

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(227, 174, 104)
    private val name = Utils.text("Sharpened Greatsword", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Melee weapon type: ", Utils.GRAY),
        Utils.text("Hitbox Margins: +0.2 blocks, Range: 3.7 blocks.", Utils.GRAY),
        Utils.text("Deals 30% increased damage to Arid Lands mobs.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 10.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.MINIMUM_ATTACK_CHARGE, 0.5F)
        .setData(
            DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.2F).minReach(0.0F).maxReach(3.7F).build()
        )
        .setLore(lore)
        .apply {
            this.addExtraSlayer(0.3)
        }
        .build()

}