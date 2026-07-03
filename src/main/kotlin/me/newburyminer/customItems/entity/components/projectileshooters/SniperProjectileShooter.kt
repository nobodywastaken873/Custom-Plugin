package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Mob

class SniperProjectileShooter(
    baseCooldown: Int,
    private val damage: HitEffects,
    private val projectileType: ProjectileType
): AbstractSpellComponent(baseCooldown, 30) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "baseCooldown" to baseCooldown,
            "damage" to damage.serialize(),
            "projectileType" to projectileType.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SNIPER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SniperProjectileShooter(
                map["baseCooldown"].asInt(),
                HitEffects.deserialize(map["damage"]),
                ProjectileType.valueOf(map["projectileType"].asString()),
            )
        }
    }

    override fun tick(wrapper: EntityWrapper) {
        if (castingTicks > 0) {
            castingTicks--

            if (castingTicks <= 0) {

                val mob = wrapper.entity as? Mob ?: return
                val targetLoc = mob.target?.location ?: return

                val clazz = projectileType.clazz
                val velocity = targetLoc.subtract(mob.location).toVector()
                mob.launchProjectile(clazz, velocity) {
                    EntityWrapperManager.getWrapperorNew(it)
                        .addComponent(CustomDamageProjectile(damage))
                    it.setTag("spellsummoned", true)
                }

                CustomEffects.playSound(wrapper.entity.location, Sound.ITEM_CROSSBOW_SHOOT, 3.0F, 0.8F)
                applyCooldown(baseCooldown)
            }

        }

        else if (wrapper.entity.ticksLived % 10 == 0) {
            reduceCooldown(10)
            if (!offCooldown()) return
            if (wrapper.isCasting()) return

            val shooter = wrapper.entity as? Mob ?: return
            val target = shooter.target ?: return
            if (!shooter.hasLineOfSight(target)) return

            startCasting(wrapper)
            CustomEffects.playSound(wrapper.entity.location, Sound.BLOCK_SNIFFER_EGG_CRACK, 3.0F, 0.5F)
        }
    }
}