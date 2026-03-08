package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.LandmineArrow
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class LandmineLauncher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LANDMINE_LAUNCHER

    private val material = Material.BOW
    private val color = arrayOf(107, 80, 77)
    private val name = text("Landmine Launcher", color)
    private val lore = Utils.loreBlockToList(
        text(
            "Shoots landmines that do not hit players and will not despawn. Left click with this item to detonate all of your shot landmines.",
            Utils.GRAY
        )
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
            EntityWrapperManager.getWrapperorNew(e.entity).addComponent(LandmineArrow())

            (e.entity as Arrow).color = Color.fromRGB(61, 57, 56)
            shooter.setCooldown(CustomItem.LANDMINE_LAUNCHER, 10.0)
            (e.entity as Arrow).pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            shooter.playSound(shooter.location, Sound.ENTITY_BLAZE_SHOOT, 0.7F, 1.7F)
        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            isLeftClick(e)
        },
        {e ->
            val player = e.player
            for (entity in e.player.world.entities) {
                if (entity.type != EntityType.ARROW) continue
                val arrow = entity as Arrow
                if (EntityWrapperManager.getWrapper(entity.uniqueId)
                        ?.hasComponent(LandmineArrow::class) != true) return@register

                if (arrow.shooter != player) continue
                entity.world.createExplosion(entity.location, 6.0F, false, true, e.player)
                entity.remove()
            }
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BIT, 0.7F, 0.5F)
        })
    }

}