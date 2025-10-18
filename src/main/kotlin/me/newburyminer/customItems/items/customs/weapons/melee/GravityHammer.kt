package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class GravityHammer: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GRAVITY_HAMMER

    private val material = Material.NETHERITE_AXE
    private val color = arrayOf(55, 46, 66)
    private val name = text("Gravity Hammer", color)
    private val lore = Utils.loreBlockToList(
        text("On a fully charged hit, increase your opponent's gravity significantly for 7 seconds, with a 20 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_SPEED, -3.3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 15.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                val damaged = e.entity as? Player ?: return
                if (!damager.offCooldown(CustomItem.GRAVITY_HAMMER)) return
                if (damager.attackCooldown.toDouble() != 1.0) return
                CustomEffects.playSound(damaged.location, Sound.ITEM_MACE_SMASH_AIR, 1.0F, 1.2F)
                EffectManager.applyEffect(damaged, CustomEffectType.ATTRIBUTE,
                    EffectData(7 * 20, attributeData = AttributeData(2.0, Attribute.GRAVITY, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
                )
                damager.setCooldown(CustomItem.GRAVITY_HAMMER, 20.0)
            }

        }

    }

}