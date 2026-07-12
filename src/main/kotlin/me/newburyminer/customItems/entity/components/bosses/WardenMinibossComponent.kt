package me.newburyminer.customItems.entity.components.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import org.bukkit.Bukkit
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector
import kotlin.math.pow

class WardenMinibossComponent(private val instance: WardenInstance?, hp: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.WARDEN_MINIBOSS_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return WardenMinibossComponent(null, 0.5)
        }
    }

    //private var damageTick = 0
    //private var lastDamage: Double = 0.0

    private var health = hp

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (instance == null) return@register

            if (e.damageSource.damageType == DamageType.MACE_SMASH) e.damage *= 0.1

            /*val difficultyFactor = when (instance.difficulty) {
                BossDifficulty.EASY -> 1.0
                BossDifficulty.HARD -> 0.6
            }

            e.damage *= (1.0 / (5.0 * instance.playerCount).pow(0.8)) * difficultyFactor*/

            health -= e.finalDamage
            e.damage = 0.0

            if (health < 0.0) e.damage = 10000.0

            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                (e.entity as LivingEntity).noDamageTicks = 0
            })
        })

        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.damager == wrapper.entity &&
            e.damageSource.damageType == DamageType.SONIC_BOOM
        },
        {e ->
            if (instance == null) return@register
            val multiple = when (instance.difficulty) {
                BossDifficulty.EASY -> 0.5
                BossDifficulty.HARD -> 0.7
            }
            e.damage *= multiple
        })

        register(EntityDeathEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (instance == null) return@register
            instance.stun()
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
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
    }*/

}