package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class SurfaceToAirMissileLauncher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SURFACE_TO_AIR_MISSILE

    private val material = Material.CROSSBOW
    private val color = arrayOf(227, 134, 11)
    private val name = text("Surface to Air Missile Launcher", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a nearly instant homing projectile that homes into players who are flying with elytra. Upon hitting them, it disables their elytra for 25 seconds. This item has a 20 second cooldown.", Utils.GRAY),
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
                val crossbow = ctx.item ?: return
                if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.SURFACE_TO_AIR_MISSILE)) {e.isCancelled = true; return}
                var flyer: Player? = null
                for (player in shooter.location.getNearbyPlayers(120.0)) {
                    if (player == e.entity.shooter) continue
                    if (player.isGliding) flyer = player
                }
                if (flyer == null) {e.isCancelled = true; return}
                e.entity.setTag("target", flyer.uniqueId)
                shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)
                e.entity.setTag("id", CustomEntity.ELYTRA_BREAKER_ARROW.id)
                e.entity.setTag("source", CustomItem.SURFACE_TO_AIR_MISSILE.name)
            }

            is ProjectileHitEvent -> {
                if (e.entity !is Arrow) return
                val hitPlayer = e.hitEntity as? Player ?: return
                e.entity.remove()
                hitPlayer.setTag("elytradisabled", 25)
                hitPlayer.playSound(e.hitEntity!!, Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F)
                hitPlayer.setCooldown(Material.ELYTRA, 500)
                hitPlayer.isGliding = false
            }


        }

    }

}