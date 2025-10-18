package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.timeSinceCombatTimeStamp
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setTag("storedexp", 0)
        .build()

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