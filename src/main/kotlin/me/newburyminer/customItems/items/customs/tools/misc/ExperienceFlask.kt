package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.timeSinceCombatTimeStamp
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
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
        .build()

    private val expTag = "storedexp"
    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom)
        },
        {e ->
            val flask = e.item ?: return@register
            val storedExp = flask.getTag<Int>(expTag) ?: 0

            if (e.player.timeSinceCombatTimeStamp() < 20 * 60 * 5) {
                e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)

                val remainingTime = 20 * 60 * 5 - e.player.timeSinceCombatTimeStamp()
                e.player.sendMessage(
                    text("You cannot use this for another ${remainingTime / 20 / 60}m, ${remainingTime / 20 % 60}s.", Utils.FAILED_COLOR)
                )
                return@register
            }

            if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                if (e.hand != EquipmentSlot.HAND) return@register

                if (e.player.isSneaking) {
                    flask.setTag(expTag, storedExp + e.player.calculateTotalExperiencePoints())
                    e.player.level = 0
                    e.player.exp = 0F
                    CustomEffects.playSoundToPlayer(e.player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.7F)
                }

                else {
                    e.player.giveExp(storedExp, false)
                    flask.setTag(expTag, 0)
                    CustomEffects.playSoundToPlayer(e.player, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.9F)
                }
            }

            else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                e.player.swingHand(e.hand ?: return@register)
                if (e.player.isSneaking) {
                    e.player.giveExp(storedExp.coerceAtMost(1395), false)
                    flask.setTag(expTag, storedExp - storedExp.coerceAtMost(1395))
                }

                else {
                    e.player.giveExp(storedExp.coerceAtMost(1395), true)
                    flask.setTag(expTag, storedExp - storedExp.coerceAtMost(1395))
                }
            }

            flask.loreBlock(text("Stored experience: ${flask.getTag<Int>(expTag) ?: 0}", arrayOf(73, 209, 10)),
                text(""),
                text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY))
            e.player.setCooldown(CustomItem.EXPERIENCE_FLASK, 0.5)

        })
    }

}