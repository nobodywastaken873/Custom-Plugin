package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.ElytraBreakerFirework
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class SurfaceToAirMissileLauncher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SURFACE_TO_AIR_MISSILE

    private val material = Material.CROSSBOW
    private val color = arrayOf(227, 134, 11)
    private val name = text("Surface to Air Missile Launcher", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a nearly instant homing projectile that homes into players who are flying with elytra. Upon hitting them, it disables their elytra for 25 seconds. This item has a 20 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom) &&
            e.entity is AbstractArrow
        },
        {e ->
            val shooter = e.entity.shooter as? Player ?: return@register

            var flyer: Player? = shooter.location.getNearbyPlayers(120.0)
                .filter { it.isGliding && it != shooter }
                .minByOrNull { it.location.subtract(shooter.location).toVector().angle(shooter.location.direction) }
            if (flyer == null) {
                e.isCancelled = true
                shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 10.0)
                return@register
            }

            shooter.playSound(shooter.location, Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 2.0F, 0.2F)
            e.isCancelled = true
            val missile = shooter.world.spawn(shooter.location.add(0.0, 1.5, 0.0), Firework::class.java) {
                it.shooter = shooter as LivingEntity
                val newMeta = it.fireworkMeta
                newMeta.addEffects(
                    FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.BLACK, Color.GRAY, Color.ORANGE)
                        .withFade(Color.GRAY)
                        .trail(true)
                        .build()
                )
                newMeta.power = 100
                it.fireworkMeta = newMeta
            }
            EntityWrapperManager.getWrapperorNew(missile).addComponent(
                ElytraBreakerFirework(HitEffects(
                    CustomDamageApply(25.0, DamageType.ARROW, 0, overrideSource = shooter),
                ), 500, flyer)
            )
            shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)
        })
    }

}