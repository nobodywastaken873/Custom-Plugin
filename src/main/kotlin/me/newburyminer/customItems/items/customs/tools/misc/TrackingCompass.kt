package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.compassCooldown
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.isTracking
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.CompassGui
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import java.util.*

class TrackingCompass: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRACKING_COMPASS

    private val material = Material.COMPASS
    private val color = arrayOf(7, 121, 186)
    private val name = text("Tracking Compass", color)
    private val lore = Utils.loreBlockToList(
        text("Allows you to track other players, right click to open menu. Tracking a player will cost additional resources.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                if (e.player.isTracking() && e.player.compassCooldown() < 30 * 60 * 20) {
                    val player = Bukkit.getPlayer(e.player.getTag<UUID>("trackingplayer")!!)
                    var loc = player?.location
                    var name = player?.name
                    if (player == null) {
                        val offlinePlayer = Bukkit.getOfflinePlayer(e.player.getTag<UUID>("trackingplayer")!!)
                        loc = offlinePlayer.location
                        name = offlinePlayer.name
                    }
                    val newMeta = e.player.inventory.itemInMainHand.itemMeta as CompassMeta
                    newMeta.lodestone = loc!!
                    newMeta.isLodestoneTracked = false
                    e.player.inventory.itemInMainHand.itemMeta = newMeta
                    val worldName = when (loc.world) {
                        Bukkit.getWorlds()[0] -> "overworld"
                        Bukkit.getWorlds()[1] -> "nether"
                        Bukkit.getWorlds()[2] -> "end"
                        CustomItems.aridWorld -> "arid lands"
                        else -> "unknown"
                    }
                    e.player.sendMessage(Utils.text("$name is currently in the $worldName.", Utils.SUCCESS_COLOR))
                } else {
                    CompassGui(e.player).open(e.player)
                }
            }

        }
    }

}