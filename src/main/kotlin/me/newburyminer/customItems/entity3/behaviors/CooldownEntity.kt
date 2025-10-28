package me.newburyminer.customItems.entity3.behaviors

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity3.CustomEntityDefinition
import org.bukkit.entity.Entity

abstract class CooldownEntity(private val cooldowns: Int): CustomEntityDefinition {

    // ALL VALUES ARE IN SECONDS, SUBJECT TO CHANGE
    // cooldown index to cooldown period
    private val cooldownMap = mutableMapOf<Int, Int>()
    init {

    }

    override fun convert(entity: Entity) {
        super.convert(entity)
        (0..<cooldowns).forEach {
            entity.setTag("cooldown${it}", 5)
        }
    }

    override val tasks: MutableMap<Int, (Entity) -> Unit> =
        mutableMapOf(20 to {player -> reduceCooldowns(player)})

    private fun reduceCooldowns(entity: Entity) {
        (0..<cooldowns).forEach {
            val current = entity.getTag<Int>("cooldown$it") ?: 0
            if (current != 0)
                entity.setTag("cooldown${it}", current - 1)
        }
    }

    fun getCooldown(entity: Entity, index: Int): Int? {
        return entity.getTag<Int>("cooldown${index}")
    }

    fun setCooldown(entity: Entity, index: Int, amount: Int) {
        entity.setTag<Int>("cooldown${index}", amount)
    }

}