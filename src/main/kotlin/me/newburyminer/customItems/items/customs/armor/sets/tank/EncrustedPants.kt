package me.newburyminer.customItems.items.customs.armor.sets.tank

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class EncrustedPants: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCRUSTED_PANTS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(130, 61, 14)
    private val name = text("Encrusted Pants", color)
    private val lore = Utils.loreBlockToList(
        text("All mobs in a 40 block radius will aggro on you instead of any other player.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Tank Set", Utils.GRAY),
        text("Sneak to gain 10 absorption hearts, with a 60 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 5.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .setArmorSet(ArmorSet.TANK)
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityTargetEvent -> {
                if (ctx.itemType != EventItemType.LEGGINGS) return
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                e.target = player
            }

        }
    }

}