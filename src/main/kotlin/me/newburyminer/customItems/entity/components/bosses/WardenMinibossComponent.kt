package me.newburyminer.customItems.entity.components.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.bosses.behaviors.WardenInstance
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.OvermaxVillagerComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Warden
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.math.pow

class WardenMinibossComponent(private val instance: WardenInstance?): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.WARDEN_MINIBOSS_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return WardenMinibossComponent(null)
        }
    }

    private var damageTick = 0
    private var lastDamage: Double = 0.0
    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        if (instance == null) return

        when (val e = ctx.event) {

            is EntityDamageEvent -> {
                reduceDamage(e, wrapper)
                reduceDamageOutput(e, wrapper)
            }

            is EntityDeathEvent -> {
                instance.stun()
            }

        }
    }

    fun reduceDamage(e: EntityDamageEvent, wrapper: EntityWrapper) {
        if (instance == null) return

        if (e.entity != wrapper.entity) { return }
        if (damageTick == Bukkit.getCurrentTick() && lastDamage == e.damage) { return }

        if (e.damageSource.damageType == DamageType.MACE_SMASH) e.damage *= 0.1
        e.damage *= (1.0 / (5.0 * instance.playerCount).pow(0.8))

        damageTick = Bukkit.getCurrentTick()
        lastDamage = e.damage

        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as LivingEntity).noDamageTicks = 0
        })
    }
    fun reduceDamageOutput(e: EntityDamageEvent, wrapper: EntityWrapper) {
        if (e !is EntityDamageByEntityEvent) return
        if (e.damager != wrapper.entity) return
        if (e.damageSource.damageType != DamageType.SONIC_BOOM) return
        e.damage *= 0.4
    }

}