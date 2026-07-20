package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.roundToInt

class PocketWormhole: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.POCKET_WORMHOLE

    private val material = Material.BREEZE_ROD
    private val color = arrayOf(211, 158, 240)
    private val name = text("Pocket Wormhole", color)
    private val lore = Utils.loreBlockToList(
        text("Location: NONE STORED"),
        text(""),
        text("Right click to bind to a location to Point A. Then, later, sneak right click at Point B to teleport to Point A, and then after 2 minutes, you will teleported back to Point B. Cannot be used in combat or whilst being tracked. Consumed on use.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR)
        },
        {e ->

            val cage = e.item ?: return@register
            val loc = e.player.location

            if (!e.player.isSneaking) {
                cage.setTag("storedloc", loc)
                e.isCancelled = true

                e.player.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 0.6F)
                cage.loreBlock(
                    text(
                        "Location: x: ${loc.x.roundToInt()}, y: ${loc.y.roundToInt()}, z: ${loc.z.roundToInt()} in ${loc.world.key.value().beautify()}",
                        Utils.GRAY
                    ),
                    text(""),
                    text("Right click to bind to a location to Point A. Then, later, sneak right click at Point B to teleport to Point A, and then after 2 minutes, you will teleported back to Point B. Cannot be used in combat or whilst being tracked. Consumed on use.", Utils.GRAY),
                )
                e.player.setCooldown(custom, 0.5)
            }

            else {
                if (EffectManager.hasEffect(e.player, CustomEffectType.POCKET_WORMHOLE_REMAINING)) return@register
                if (cage.getTag<Location>("storedloc") == null) return@register
                if (e.player.isInCombat() || e.player.isBeingTracked()) return@register

                EffectManager.applyEffect(e.player, CustomEffectType.POCKET_WORMHOLE_REMAINING, EffectData(2400))
                e.player.setTag("pocketwormholeorigin", e.player.location)
                e.player.teleport(cage.getTag<Location>("storedloc") ?: return@register)
                e.player.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1.2F)
                cage.amount -= 1

                e.player.setCooldown(custom, 0.5)
            }
        })
    }

}