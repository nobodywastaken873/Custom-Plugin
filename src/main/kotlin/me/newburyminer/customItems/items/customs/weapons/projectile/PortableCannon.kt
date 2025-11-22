package me.newburyminer.customItems.items.customs.weapons.projectile

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.getCorners
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class PortableCannon: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PORTABLE_CANNON

    private val material = Material.CROSSBOW
    private val color = arrayOf(99, 81, 59)
    private val name = text("Portable Cannon", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a medium damage but slow moving shot, with heavy recoil.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                if (e.entity !is Arrow) return
                if (e.entity.shooter !is Player) return
                val shooter = e.entity.shooter as Player

                val direction = shooter.location.direction.normalize().multiply(-1)
                shooter.velocity = shooter.velocity.add(direction)

                e.entity.velocity = e.entity.velocity.multiply(0.6)
                EntityWrapperManager.getWrapperorNew(e.entity).addComponent(CustomDamageProjectile(HitEffects(
                    CustomDamageApply(18.0, DamageType.ARROW, overrideSource = shooter),
                )))

                CustomEffects.particleCloud(ParticleBuilder(Particle.SMOKE), e.entity.location, 20, 1.0, 0.0)
                CustomEffects.playSound(shooter.location, Sound.ENTITY_GENERIC_EXPLODE, 0.4F, 1.8F)

                shooter.setCooldown(CustomItem.PORTABLE_CANNON, 1.0)
            }

        }

    }

}