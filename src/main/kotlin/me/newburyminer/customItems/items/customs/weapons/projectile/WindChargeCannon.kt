package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.HomingProjectile
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.WindCharge
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

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

    init {
        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom) &&
            e.entity is AbstractArrow
        },
        {e ->
            val shooter = e.entity.shooter as? Player ?: return@register
            val crossbow = getActiveRanged(e) ?: return@register
            val mode = crossbow.getTag<Int>("mode") ?: 0

            val windCharges = mutableListOf<WindCharge>()
            for (i in 0..1)
                windCharges.add(e.entity.world.spawn(e.entity.location, WindCharge::class.java) {
                    it.velocity = e.entity.velocity.multiply(0.7)
                    it.shooter = shooter
                })

            if (mode == 0) {
                val closest = shooter.getNearbyEntities(60.0, 60.0, 60.0)
                    .filterIsInstance<LivingEntity>()
                    .minByOrNull { it.location.subtract(shooter.location).toVector().angle(shooter.location.direction) }
                    ?: shooter

                for (windCharge in windCharges)
                    EntityWrapperManager.getWrapperorNew(windCharge).addComponent(
                        HomingProjectile(Math.PI / 24, HomingSystem.Type.BOTH_SCALED, closest)
                    )
                shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 7.0)
            } else {
                shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 5.0)
            }
            e.entity.remove()
        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            isLeftClick(e)
        },
        {e ->
            val item = e.item ?: return@register
            val mode = item.getTag<Int>("mode") ?: 0
            item.setTag("mode", if (mode == 1) 0 else 1)
            item.name(text("Wind Charge Cannon - ${if (mode == 1) "Homing" else "Straight"}", arrayOf(201, 240, 238), bold = true))
            e.player.playSound(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.0F)
        })
    }

}