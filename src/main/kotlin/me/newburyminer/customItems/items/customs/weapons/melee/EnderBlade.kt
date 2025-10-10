package me.newburyminer.customItems.items.customs.weapons.melee

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
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class EnderBlade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENDER_BLADE

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(11, 79, 82)
    private val name = text("Ender Blade", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to teleport forward 12 blocks, with a 7 second cooldown. Right click while sneaking to teleport forward 12 blocks, and make all hits be critical for the next 5 seconds, with a 15 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-2.2MA", "ENI+0.1MA", "ATD+8.5MA")
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                if (damager.getTag<Int>("enderbladecrittime") == 0 || damager.getTag<Int>("enderbladecrittime") == null) return
                if (e.isCritical) return
                e.damage *= 1.5
                CustomEffects.playSound(e.entity.location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.8F, 1.0F)
                CustomEffects.particle(Particle.CRIT.builder(), e.entity.location, 20, 0.5, 0.5)
            }

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (!item.offCooldown(e.player)) return
                if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
                val startLoc = e.player.eyeLocation.clone()
                val toAdd = e.player.location.direction.clone().normalize().multiply(0.05)
                for (i in 1..120) {
                    startLoc.add(toAdd)
                    if (!startLoc.add(toAdd).block.isPassable || !startLoc.clone().add(Vector(0, 1, 0)).block.isPassable) {
                        startLoc.subtract(toAdd.multiply(2))
                        break
                    }
                }
                startLoc.pushOut(e.player.width)
                e.player.teleport(startLoc)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_SHULKER_TELEPORT, 1.0F, 0.94F)
                if (e.player.isSneaking) {
                    item.setCooldown(e.player, 15.0)
                    e.player.setTag("enderbladecrittime", 6)
                } else {
                    item.setCooldown(e.player, 6.5)
                }
            }

        }

    }

}