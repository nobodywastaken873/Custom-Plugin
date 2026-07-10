package me.newburyminer.customItems.entity.components.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.entity.*
import org.bukkit.Bukkit
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

class WardenBossComponent(private val instance: WardenInstance?): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.WARDEN_BOSS_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return WardenBossComponent(null)
        }
    }

    private var damageTick = 0
    private var lastDamage: Double = 0.0
    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        if (instance == null) return
        when (val e = ctx.event) {

            is EntityDamageEvent -> {

                if (e.entity != wrapper.entity) { return }
                //if (damageTick == Bukkit.getCurrentTick() && lastDamage == e.damage) { return }

                if (e.damageSource.damageType == DamageType.MACE_SMASH) e.damage *= 0.1
                e.damage *= (1.0 / (12.0 * instance.playerCount))

                damageTick = Bukkit.getCurrentTick()
                lastDamage = e.damage

                //if (instance.hpPercent >= 1.0) instance.start()

                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {

                    instance.updateBossbar()
                    instance.checkStunning()

                    (e.entity as LivingEntity).noDamageTicks = 0
                })
            }

            is EntityDeathEvent -> {
                instance.bossWin()
            }

        }
    }

}