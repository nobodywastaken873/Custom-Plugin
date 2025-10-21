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
import me.newburyminer.customItems.entity.CustomEntity
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
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
                e.entity.setTag("target", flyer.uniqueId)
                shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)
                e.entity.setTag("id", CustomEntity.ELYTRA_BREAKER_ARROW.id)
                e.entity.setTag("source", CustomItem.SURFACE_TO_AIR_MISSILE.name)
                activeHomingTime = 400
            }

            is ProjectileHitEvent -> {
                if (e.entity !is Arrow) return
                val hitPlayer = e.hitEntity as? Player ?: return
                e.entity.remove()
                EffectManager.applyEffect(hitPlayer, CustomEffectType.ELYTRA_DISABLED, 25 * 20)
                hitPlayer.playSound(e.hitEntity!!, Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F)
                hitPlayer.setCooldown(Material.ELYTRA, 500)
                hitPlayer.isGliding = false
            }


        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(1 to {player -> surfaceToAirMissile(player)})

    private var activeHomingTime = 0
    private fun surfaceToAirMissile(player: Player) {
        if (activeHomingTime > 0) --activeHomingTime
        else return

        for (entity in player.getNearbyEntities(60.0, 60.0, 60.0)) {
            if (entity.type != EntityType.ARROW || (entity as Arrow).isInBlock) continue
            if (entity.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_ARROW.id) continue
            if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
            entity.setTag("tick", Bukkit.getServer().currentTick)
            // here is null
            val target = Bukkit.getEntity(entity.getTag<UUID>("target") ?: continue) ?: continue
            val newDirection = target.location.subtract(entity.location).toVector().add(Vector(0.0, 0.5, 0.0))
            entity.velocity = newDirection.normalize().multiply((target.location.subtract(entity.location).length() / 8).coerceAtLeast(8.0))
        }
    }

}