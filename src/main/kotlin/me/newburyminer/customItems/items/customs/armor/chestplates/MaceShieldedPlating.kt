package me.newburyminer.customItems.items.customs.armor.chestplates

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MaceShieldedPlating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MACE_SHIELDED_PLATING

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(89, 84, 92)
    private val name = text("Mace Shielded Plating", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.CHESTPLATE) return
                val player = ctx.player ?: return
                if (e.entity !is Player) return
                if (e.damager !is Player) return
                if (e.damageSource.damageType != DamageType.MACE_SMASH) return
                e.damage *= 0.4
            }

        }
    }

}