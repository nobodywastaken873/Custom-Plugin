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

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

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
