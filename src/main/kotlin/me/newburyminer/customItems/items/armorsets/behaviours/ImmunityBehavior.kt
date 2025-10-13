package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import me.newburyminer.customItems.items.armorsets.ArmorSetEventContext
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ImmunityBehavior : ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.IMMUNITY

    override fun handle(ctx: ArmorSetEventContext) {
        when (val e = ctx.event) {

            is EntityPotionEffectEvent -> {
                if (ctx.pieceCount != 4) return
                val player = ctx.player
                if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
                val oldPotion = e.newEffect ?: return
                val flippedType = flipPotion(oldPotion.type) ?: return
                val newPotion = PotionEffect(flippedType, oldPotion.duration * 3, oldPotion.amplifier + 1)
                player.addPotionEffect(newPotion)
            }

        }
    }

    private fun flipPotion(type: PotionEffectType): PotionEffectType? {
        return when (type) {
            PotionEffectType.SLOWNESS -> PotionEffectType.SPEED
            PotionEffectType.MINING_FATIGUE -> PotionEffectType.HASTE
            PotionEffectType.NAUSEA -> PotionEffectType.FIRE_RESISTANCE
            PotionEffectType.BLINDNESS -> PotionEffectType.INVISIBILITY
            PotionEffectType.HUNGER -> PotionEffectType.SATURATION
            PotionEffectType.WEAKNESS -> PotionEffectType.STRENGTH
            PotionEffectType.POISON -> PotionEffectType.REGENERATION
            PotionEffectType.WITHER -> PotionEffectType.REGENERATION
            PotionEffectType.LEVITATION -> PotionEffectType.RESISTANCE
            PotionEffectType.SLOW_FALLING -> PotionEffectType.HEALTH_BOOST
            PotionEffectType.DARKNESS -> PotionEffectType.RESISTANCE
            else -> null
        }
    }

}