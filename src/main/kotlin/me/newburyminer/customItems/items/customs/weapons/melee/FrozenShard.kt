package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FrozenShard: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FROZEN_SHARD

    private val material = Material.IRON_SWORD
    private val color = arrayOf(102, 193, 209)
    private val name = text("Frozen Shard", color)
    private val lore = Utils.loreBlockToList(
        text("On hit, prevent a player from walking, jumping, or taking knockback for 6 seconds, with a 60s cooldown. They can still pearl or use other movement items. This sword also has a 1/5 chance of inflicting Slowness 1 on hit for 5 seconds.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-2.4MA", "ATD+9.0MA")
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val player = e.damager as? Player ?: return
                if (Math.random() < 1.0 / 5.0 && e.entity is LivingEntity) {
                    (e.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 100, 0))
                }
                if (!player.inventory.itemInMainHand.offCooldown(e.damager as Player)) return
                val hitPlayer = e.entity as? Player ?: return

                EffectManager.applyEffect(hitPlayer, CustomEffectType.ATTRIBUTE,
                    EffectData(6 * 20, attributeData = AttributeData(-100.0, Attribute.MOVEMENT_SPEED, AttributeModifier.Operation.ADD_SCALAR)))
                EffectManager.applyEffect(hitPlayer, CustomEffectType.ATTRIBUTE,
                    EffectData(6 * 20, attributeData = AttributeData(-100.0, Attribute.JUMP_STRENGTH, AttributeModifier.Operation.ADD_SCALAR)))
                EffectManager.applyEffect(hitPlayer, CustomEffectType.ATTRIBUTE,
                    EffectData(6 * 20, attributeData = AttributeData(100.0, Attribute.KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER)))

                player.inventory.itemInMainHand.setCooldown(e.damager as Player, 60.0)
            }

        }

    }

}