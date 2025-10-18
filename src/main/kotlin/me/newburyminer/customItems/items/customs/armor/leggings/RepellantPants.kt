package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class RepellantPants: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REPELLANT_PANTS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(27, 2, 64)
    private val name = text("Repellant Pants", color)
    private val lore = Utils.loreBlockToList(
        text("When your totem is popped, launch all nearby enemies away.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityResurrectEvent -> {
                if (ctx.itemType != EventItemType.LEGGINGS) return
                val player = ctx.player ?: return
                if (e.isCancelled) return
                for (entity in player.location.getNearbyEntities(8.0, 8.0, 8.0)) {
                    if (entity == player) continue
                    entity.velocity = entity.velocity.add(entity.location.subtract(player.location).toVector().normalize().multiply(3).add(
                        Vector(0.0, 1.0, 0.0)
                    ))
                }
            }

        }
    }

}