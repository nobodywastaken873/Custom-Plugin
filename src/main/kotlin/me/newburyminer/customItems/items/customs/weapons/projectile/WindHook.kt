package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.*
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class WindHook: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WIND_HOOK

    private val material = Material.BOW
    private val color = arrayOf(211, 195, 219)
    private val name = text("Wind Hook", color)
    private val lore = Utils.loreBlockToList(
        text("Shoot to launch a hook that pulls you in upon landing.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val item = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.WIND_HOOK)) {e.isCancelled = true; return}
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.WIND_HOOK.name)
                (e.entity as Arrow).color = Color.fromRGB(211, 195, 219)
                shooter.setCooldown(CustomItem.WIND_HOOK, 15.0)
                shooter.stopSound(Sound.ENTITY_ARROW_SHOOT)
                CustomEffects.playSound(shooter.location, Sound.ENTITY_BREEZE_JUMP, 1F, 0.8F)
            }

            is ProjectileHitEvent -> {
                val arrow = e.entity as Arrow
                arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                val shooter = e.entity.shooter as Player
                val uuid = shooter.uniqueId
                pullTime[uuid] = 50
                pullCoords[uuid] = e.entity.location.clone()
                shooter.setTag("windhookpullcoords", "${e.entity.location.x},${e.entity.location.y},${e.entity.location.z}")
                shooter.setTag("windhookpulltime", 50)
            }

        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(1 to {player -> windHookPull(player)})

    private val pullTime = mutableMapOf<UUID, Int>()
    private val pullCoords = mutableMapOf<UUID, Location>()
    private fun windHookPull(player: Player) {
        val uuid = player.uniqueId
        if ((pullTime[uuid] ?: 0) > 0) {
            val timeLeft = pullTime[uuid] ?: return
            pullTime[uuid] = timeLeft - 1
            val pullLoc = (pullCoords[uuid] ?: return).clone()
            if (pullLoc.world != player.world) return
            val direction = pullLoc.subtract(player.location)

            if (direction.length() < 6.0) pullTime[uuid] = 0
            val toAdd = direction.toVector().normalize().multiply(3)
            player.velocity = toAdd.clone().add(Vector(0.0, 0.4, 0.0))
            CustomEffects.particleLine(
                Particle.DOLPHIN.builder(), player.location, pullLoc, 400
            )
            if (Bukkit.getCurrentTick() % 20 == 0) {
                CustomEffects.playSound(
                    player.location,
                    arrayOf(Sound.ENTITY_BREEZE_IDLE_AIR, Sound.ENTITY_BREEZE_IDLE_GROUND).random(),
                    1F,
                    1.2F
                )
            }
        }
    }

}