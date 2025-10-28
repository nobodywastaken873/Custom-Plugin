package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.items.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.abs

class WindChargeCannon: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WIND_CHARGE_CANNON

    private val material = Material.CROSSBOW
    private val color = arrayOf(201, 240, 238)
    private val name = text("Wind Charge Cannon - Homing", color)
    private val lore = Utils.loreBlockToList(
        text("Shoot to launch a cluster of two wind charges, with a 7 second cooldown. Left click to cycle between homing mode and straight mode.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                if (e.entity !is Arrow) return
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.WIND_CHARGE_CANNON)) {e.isCancelled = true; return}
                val mode = crossbow.getTag<Int>("mode") ?: 0

                val windCharges = mutableListOf<WindCharge>()
                for (i in 0..1) {
                    val windCharge = e.entity.world.spawn(e.entity.location, WindCharge::class.java) {
                        it.velocity = e.entity.velocity.multiply(0.7)
                        it.shooter = shooter
                    }
                    windCharges.add(windCharge)
                }

                if (mode == 0) {
                    shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 7.0)
                    for (windCharge in windCharges) {
                        var closest: Entity? = null
                        var closestAngle: Double = Math.PI / 3
                        for (entity in shooter.getNearbyEntities(60.0, 60.0, 60.0)) {
                            if (entity !is LivingEntity) continue
                            val angle = entity.location.subtract(shooter.location).toVector().angle(shooter.location.direction)
                            if (abs(angle) < abs(closestAngle)) {
                                closest = entity
                                closestAngle = angle.toDouble()
                            }
                        }
                        val target = closest?.uniqueId ?: shooter.uniqueId
                        windCharge.setTag("target", target)
                        windCharge.setTag("id", CustomEntity.WIND_CANNON_CHARGE.id)
                        windCharge.setTag("source", CustomItem.WIND_CHARGE_CANNON.name)
                        activeHomingTime = 600
                    }
                } else {
                    shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 5.0)
                }

                e.entity.remove()
            }

            is PlayerInteractEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val item = ctx.item ?: return
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
                val mode = item.getTag<Int>("mode") ?: 0
                item.setTag("mode", if (mode == 1) 0 else 1)
                item.name(text("Wind Charge Cannon - ${if (mode == 1) "Homing" else "Straight"}", arrayOf(201, 240, 238), bold = true))
                e.player.playSound(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.0F)
            }


        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(1 to {player -> homingWindChargeUpdate(player)})

    private var activeHomingTime = 0
    private fun homingWindChargeUpdate(player: Player) {
        if (activeHomingTime > 0) --activeHomingTime
        else return

        for (entity in player.getNearbyEntities(60.0, 60.0, 60.0)) {
            if (entity.type != EntityType.WIND_CHARGE) continue
            if (entity.getTag<Int>("id") != CustomEntity.WIND_CANNON_CHARGE.id) continue
            if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
            entity.setTag("tick", Bukkit.getServer().currentTick)
            val target = Bukkit.getEntity(entity.getTag<UUID>("target")!!) ?: continue
            //val newDirection = entity.velocity.add(target.location.subtract(entity.location).toVector().normalize().multiply(1.5))
            //entity.velocity = newDirection.normalize().multiply(currentVelocity)
            val cross = entity.velocity.getCrossProduct(target.location.subtract(entity.location).toVector())
            val angle = entity.velocity.angle(target.location.subtract(entity.location).toVector())
            val newDirection = entity.velocity.rotateAroundAxis(cross, angle.coerceAtMost((Math.PI / 24).toFloat()).toDouble())
            entity.velocity = newDirection
        }
    }

}