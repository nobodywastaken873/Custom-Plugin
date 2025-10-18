package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class ReinforcedCage: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REINFORCED_CAGE

    private val material = Material.CHAIN
    private val color = arrayOf(105, 101, 100)
    private val name = text("Reinforced Cage", color)
    private val lore = Utils.loreBlockToList(
        text("Type: NONE STORED", Utils.GRAY),
        text(""),
        text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.action != Action.RIGHT_CLICK_BLOCK) return
                if (!e.player.offCooldown(CustomItem.REINFORCED_CAGE)) return
                val loc = e.clickedBlock!!.location
                loc.add(Vector(0.5, 1.0, 0.5))
                val entityAsString = item.getTag<String>("storedmob") ?: return
                if (entityAsString == "") return
                e.isCancelled = true
                Bukkit.getEntityFactory().createEntitySnapshot(entityAsString).createEntity(loc)
                item.setTag<String>("storedmob", "")
                CustomEffects.playSound(loc, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1F, 1.2F)
                item.loreBlock(
                    text("Type: NONE STORED", Utils.GRAY),
                    text(""),
                    text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
                )
                e.player.setCooldown(CustomItem.REINFORCED_CAGE, 0.5)
            }

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.rightClicked.type in arrayOf(
                        EntityType.WARDEN, EntityType.ENDER_DRAGON, EntityType.ARROW, EntityType.TEXT_DISPLAY, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY,
                        EntityType.AREA_EFFECT_CLOUD, EntityType.BREEZE_WIND_CHARGE, EntityType.DRAGON_FIREBALL, EntityType.END_CRYSTAL, EntityType.ENDER_PEARL,
                        EntityType.EVOKER_FANGS, EntityType.EXPERIENCE_ORB, EntityType.EXPERIENCE_BOTTLE, EntityType.EYE_OF_ENDER, EntityType.FALLING_BLOCK,
                        EntityType.FIREBALL, EntityType.FIREWORK_ROCKET, EntityType.FISHING_BOBBER, EntityType.GLOW_ITEM_FRAME, EntityType.ITEM_FRAME,
                        EntityType.INTERACTION, EntityType.ITEM, EntityType.LEASH_KNOT, EntityType.LINGERING_POTION, EntityType.MARKER, EntityType.PAINTING,
                        EntityType.PLAYER, EntityType.SHULKER_BULLET, EntityType.SMALL_FIREBALL, EntityType.SPECTRAL_ARROW, EntityType.SPLASH_POTION,
                        EntityType.TRIDENT, EntityType.WIND_CHARGE, EntityType.WITHER, EntityType.WITHER_SKULL
                    )) return
                if (e.rightClicked.getTag<Int>("id") != null) return
                if (e.rightClicked.getTag<Int>("bossid") != null) return
                if (!e.player.offCooldown(CustomItem.REINFORCED_CAGE)) return
                if (item.getTag<String>("storedmob") !in arrayOf(null, "")) return
                e.isCancelled = true
                val entity = e.rightClicked

                val snapshot = entity.createSnapshot()!!.asString
                e.player.inventory.itemInMainHand.setTag("storedmob", snapshot)

                e.player.inventory.itemInMainHand.loreBlock(
                    text("Type: ${entity.type}", Utils.GRAY),
                    text(""),
                    text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
                )
                if (entity is InventoryHolder) entity.inventory.clear()
                entity.remove()
                e.player.setCooldown(CustomItem.REINFORCED_CAGE, 0.5)
            }

        }

    }

}