package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ImmunityBehavior : ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.IMMUNITY

    init {
        register(EntityPotionEffectEvent::class, { e ->
            e.entity is Player &&
            getPieces(e.entity as Player, set) == 4 &&
            e.action in arrayOf(EntityPotionEffectEvent.Action.ADDED, EntityPotionEffectEvent.Action.CHANGED)
        },
        {e ->
            val player = e.entity as Player
            val oldPotion = e.newEffect ?: return@register
            val flippedType = flipPotion(oldPotion.type) ?: return@register
            val newPotion = PotionEffect(flippedType, oldPotion.duration * 3, (oldPotion.amplifier + 1).coerceAtMost(2))
            player.addPotionEffect(newPotion)
        })
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