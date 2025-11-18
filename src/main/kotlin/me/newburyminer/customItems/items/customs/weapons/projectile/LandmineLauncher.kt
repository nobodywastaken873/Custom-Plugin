package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.projectiles.LandmineArrow
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.damage.DamageSource
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRemoveEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class LandmineLauncher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LANDMINE_LAUNCHER

    private val material = Material.BOW
    private val color = arrayOf(107, 80, 77)
    private val name = text("Landmine Launcher", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val item = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.LANDMINE_LAUNCHER)) {e.isCancelled = true; return}

                EntityWrapperManager.getWrapperorNew(e.entity).addComponent(LandmineArrow())

                (e.entity as Arrow).color = Color.fromRGB(61, 57, 56)
                shooter.setCooldown(CustomItem.LANDMINE_LAUNCHER, 10.0)
                (e.entity as Arrow).pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }

            is PlayerInteractEvent -> {
                val player = ctx.player ?: return
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
                for (entity in e.player.world.entities) {
                    if (entity.type != EntityType.ARROW) continue
                    val arrow = entity as Arrow
                    if (EntityWrapperManager.getWrapper(entity.uniqueId)
                            ?.hasComponent(LandmineArrow::class) != true) return

                    if (arrow.shooter != player) continue
                    entity.world.createExplosion(entity.location, 6.0F, false, true, e.player)
                    entity.remove()
                }
            }
        }

    }

}