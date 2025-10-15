package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import me.newburyminer.customItems.items.armorsets.ArmorSetEventContext
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AssassinBehavior: ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.ASSASSIN

    override fun handle(ctx: ArmorSetEventContext) {
        when (val e = ctx.event) {

            is EntityDamageEvent -> {
                if (ctx.pieceCount != 4) return
                val player = ctx.player
                if (e.damageSource.damageType in arrayOf(DamageType.DROWN, DamageType.IN_WALL)) return
                player.setTag("assassinsstep", 0)
                if (player.getAttribute(Attribute.MOVEMENT_SPEED)!!.getModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed")) != null) {
                    player.getAttribute(Attribute.MOVEMENT_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"))
                    player.getAttribute(Attribute.ATTACK_DAMAGE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"))
                    player.getAttribute(Attribute.ATTACK_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"))
                    player.getAttribute(Attribute.SCALE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"))
                }
            }

            is ProjectileHitEvent -> {
                val pieces = ctx.pieceCount
                val player = ctx.player
                if (e.entity !is Arrow) return
                if (Math.random() >= 0.125 * pieces) return
                e.isCancelled = true
                e.entity.remove()
                CustomEffects.playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.2F)
            }

        }
    }

    override val period: Int
        get() = 20
    override fun runTask(player: Player) {

        val speed = player.getAttribute(Attribute.MOVEMENT_SPEED) ?: return
        if (speed.getModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed")) != null) {
            speed.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"))
            player.getAttribute(Attribute.ATTACK_DAMAGE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"))
            player.getAttribute(Attribute.ATTACK_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"))
            player.getAttribute(Attribute.SCALE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"))
        }

        val fullSet = (
                player.inventory.helmet?.isItem(CustomItem.ASSASSINS_HOOD) == true &&
                player.inventory.chestplate?.isItem(CustomItem.ASSASSINS_ROBE) == true &&
                player.inventory.leggings?.isItem(CustomItem.ASSASSINS_LEGGINGS) == true &&
                player.inventory.boots?.isItem(CustomItem.ASSASSINS_LOAFERS) == true
        )
        if (!fullSet) return

        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 25, 0, false, false))
        val currentStep = player.getTag<Int>("assassinsstep") ?: 0
        if (currentStep < 10) player.setTag("assassinsstep", currentStep + 1)
        player.getAttribute(Attribute.MOVEMENT_SPEED)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"), 0.004 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.ATTACK_DAMAGE)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"), 0.6 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.ATTACK_SPEED)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"), 0.02 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.SCALE)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"), -0.03 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
    }

}