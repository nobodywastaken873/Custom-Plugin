package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.WindHookArrow
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom)
        },
        {e ->
            val shooter = e.entity.shooter as? Player ?: return@register
            EntityWrapperManager.getWrapperorNew(e.entity).addComponent(WindHookArrow())

            (e.entity as Arrow).color = Color.fromRGB(211, 195, 219)
            shooter.setCooldown(custom, 15.0)
            shooter.stopSound(Sound.ENTITY_ARROW_SHOOT)
            CustomEffects.playSoundToPlayer(shooter, Sound.ENTITY_BREEZE_JUMP, 1F, 0.8F)
        })

        register(ProjectileHitEvent::class, { e ->
            EntityWrapperManager.getWrapper(e.entity.uniqueId)?.hasComponent(WindHookArrow::class) ?: false
        },
        {e ->
            val arrow = e.entity as Arrow
            arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            val shooter = e.entity.shooter as Player
            val uuid = shooter.uniqueId
            pullTime[uuid] = 50
            pullCoords[uuid] = e.entity.location.clone()
            //shooter.setTag("windhookpullcoords", "${e.entity.location.x},${e.entity.location.y},${e.entity.location.z}")
            //shooter.setTag("windhookpulltime", 50)
        })
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
            val direction = pullLoc.clone().subtract(player.location)

            if (direction.length() < 6.0) pullTime[uuid] = 0
            val toAdd = direction.toVector().normalize().multiply(1.5)
            player.velocity = toAdd.clone().add(Vector(0.0, 0.4, 0.0))

            CustomEffects.particleLine(Particle.DOLPHIN.builder(), player.location, pullLoc, 400)
            if (Bukkit.getCurrentTick() % 20 == 0) {
                CustomEffects.playSoundToPlayer(player, arrayOf(Sound.ENTITY_BREEZE_IDLE_AIR, Sound.ENTITY_BREEZE_IDLE_GROUND).random(), 1F, 1.2F)
            }
        }
    }

}