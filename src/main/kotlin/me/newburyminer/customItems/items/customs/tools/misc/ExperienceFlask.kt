package me.newburyminer.customItems.items.customs.tools.misc

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.timeSinceCombatTimeStamp
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.entities.bosses.BossListeners
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Container
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class ExperienceFlask: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.EXPERIENCE_FLASK

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(170, 242, 24)
    private val name = text("Experience Flask", color)
    private val lore = Utils.loreBlockToList(
        text("Stored experience: 0", arrayOf(73, 209, 10)),
        text(""),
        text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .setTag("storedexp", 0)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val flask = ctx.item ?: return
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (ctx.itemType != EventItemType.MAINHAND) return
                    if (!e.player.offCooldown(CustomItem.EXPERIENCE_FLASK)) return
                    if (e.player.timeSinceCombatTimeStamp() < 20 * 60 * 5) {
                        e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                        return
                    }
                    if (e.player.isSneaking) {
                        e.player.giveExp(flask.getTag<Int>("storedexp")!!, false)
                        flask.setTag("storedexp", 0)
                        CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.1F)
                    } else {
                        flask.setTag("storedexp", e.item!!.getTag<Int>("storedexp")!! + e.player.calculateTotalExperiencePoints())
                        e.player.level = 0
                        e.player.exp = 0F
                        CustomEffects.playSound(e.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.9F)
                    }
                    flask.loreBlock(text("Stored experience: ${flask.getTag<Int>("storedexp")!!}", arrayOf(73, 209, 10)),
                        text(""),
                        text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY))
                    e.player.setCooldown(CustomItem.EXPERIENCE_FLASK, 0.5)
                } else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                    if (!ctx.itemType.isHand()) return
                    if (!e.player.offCooldown(CustomItem.EXPERIENCE_FLASK)) return
                    if (e.player.timeSinceCombatTimeStamp() < 20 * 60 * 5) {
                        e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                        return
                    }
                    if (e.player.isSneaking) {
                        e.player.giveExp(flask.getTag<Int>("storedexp")!!.coerceAtMost(1395), false)
                        flask.setTag("storedexp", flask.getTag<Int>("storedexp")!! - flask.getTag<Int>("storedexp")!!.coerceAtMost(1395))
                    } else {
                        e.player.giveExp(flask.getTag<Int>("storedexp")!!.coerceAtMost(1395), true)
                        flask.setTag("storedexp", flask.getTag<Int>("storedexp")!! - flask.getTag<Int>("storedexp")!!.coerceAtMost(1395))
                    }
                    e.item!!.loreBlock(text("Stored experience: ${e.item!!.getTag<Int>("storedexp")!!}", arrayOf(73, 209, 10)),
                        text(""),
                        text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY))
                    e.player.setCooldown(CustomItem.EXPERIENCE_FLASK, 0.5)
                }
            }

        }

    }

}
