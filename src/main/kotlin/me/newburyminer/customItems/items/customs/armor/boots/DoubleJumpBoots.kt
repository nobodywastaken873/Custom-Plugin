package me.newburyminer.customItems.items.customs.armor.boots

import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack

class DoubleJumpBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DOUBLE_JUMP_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(171, 230, 245)
    private val name = text("Double Jump Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+3.0FE","ART+3.0FE","KNR+0.1FE","SAF+8.0FE")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerToggleFlightEvent -> {
                if (ctx.itemType != EventItemType.BOOTS) return
                if (e.player.gameMode in arrayOf(GameMode.CREATIVE, GameMode.SPECTATOR)) return
                e.isCancelled = true
                e.player.allowFlight = false
                if (!e.player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return
                e.player.velocity = e.player.location.direction.multiply(1.0).setY(0.7)
                e.player.setCooldown(CustomItem.DOUBLE_JUMP_BOOTS, 5.0)
            }

        }
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(5 to {player -> resetDoubleJump(player)})

    private fun resetDoubleJump(player: Player) {
        if (player.allowFlight) return
        if (!player.isOnGround) return
        if (!player.inventory.boots.isItem(CustomItem.DOUBLE_JUMP_BOOTS)) return
        if (!player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return
        player.allowFlight = true
    }

}
