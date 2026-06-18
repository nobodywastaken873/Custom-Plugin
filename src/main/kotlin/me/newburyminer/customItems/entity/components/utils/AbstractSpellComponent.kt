package me.newburyminer.customItems.entity.components.utils

import me.newburyminer.customItems.entity.EntityComponent
import org.bukkit.scheduler.BukkitTask

abstract class AbstractSpellComponent(protected val baseCooldown: Int, castTime: Int, startCooldown: Int = baseCooldown / 2
): EntityComponent, SpellInterface, CooldownInterface {
    override var cooldown: Int = startCooldown
    override var castingTicks: Int = 0
    override val spellDuration: Int = castTime
    override var cancelTask: BukkitTask? = null
}