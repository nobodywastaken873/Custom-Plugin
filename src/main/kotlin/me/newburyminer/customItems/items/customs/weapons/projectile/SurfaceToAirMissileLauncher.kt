package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.ElytraBreakerFirework
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

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

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.SURFACE_TO_AIR_MISSILE)) {e.isCancelled = true; return}
                shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)
                var flyer: Player? = null
                for (player in shooter.location.getNearbyPlayers(120.0)) {
                    if (player == e.entity.shooter) continue
                    if (player.isGliding) flyer = player
                }
                if (flyer == null) { e.isCancelled = true; return }
                shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)

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
            }

        }

    }

}