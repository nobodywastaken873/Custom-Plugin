package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.SniperFireworkProjectile
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class RpgLauncher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.RPG_LAUNCHER
    private val material = Material.CROSSBOW
    private val color = arrayOf(171, 94, 5)
    private val name = text("RPG Launcher", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a high velocity firework rocket with a small explosion radius, dealing 0.65 more damage per block travelled.", Utils.GRAY),
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
            val firework = shooter.world.spawn(e.entity.location, Firework::class.java) {
                it.isShotAtAngle = true
                it.velocity = (e.entity.shooter as Player).location.direction.normalize().multiply(6)
                val newMeta = it.fireworkMeta
                newMeta.addEffects(
                    FireworkEffect.builder()
                        .withColor(Color.RED)
                        .with(FireworkEffect.Type.BALL)
                        .build()
                )
                newMeta.power = 10
                it.fireworkMeta = newMeta
                it.shooter = e.entity.shooter
            }

            shooter.setCooldown(CustomItem.RPG_LAUNCHER, 10.0)

            EntityWrapperManager.getWrapperorNew(firework).addComponent(SniperFireworkProjectile(0.65))
            e.entity.remove()
        })
    }

}