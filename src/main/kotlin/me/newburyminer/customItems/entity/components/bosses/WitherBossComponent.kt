package me.newburyminer.customItems.entity.components.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.bosses.definitions.wither.WitherInstance
import me.newburyminer.customItems.entity.*
import org.bukkit.Bukkit
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.math.pow

class WitherBossComponent(private val instance: WitherInstance?): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.WITHER_BOSS_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return WitherBossComponent(null)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (instance == null) return@register

            if (e.damage < 7.0) e.damage = 0.0001
            instance.reduceHp(e.damage, e.damageSource.damageType)
            e.damage = 0.0

            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {

                instance.updateBossbar()
                instance.checkStun()

                (e.entity as LivingEntity).noDamageTicks = 0
            })
        })

        register(EntityDeathEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (instance == null) return@register
            instance.bossWin()
        })
    }

}