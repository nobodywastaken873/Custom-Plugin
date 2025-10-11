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

class PolarizedMagnet: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.POLARIZED_MAGNET

    private val material = Material.IRON_NUGGET
    private val color = arrayOf(255, 36, 36)
    private val name = text("Polarized Magnet", color)
    private val lore = Utils.loreBlockToList(
        text("Hold right click to pull in all nearby entities. Left click while sneaking to toggle an item pull mode that pulls in all nearby items even when you are not holding it.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (ctx.itemType != EventItemType.MAINHAND) return
                    if (!e.player.isSneaking) return
                    if (!e.player.offCooldown(CustomItem.POLARIZED_MAGNET)) return
                    e.player.setTag("polarizedmagnetitempull", !(e.player.getTag<Boolean>("polarizedmagnetitempull")?:false))
                    if (e.player.getTag<Boolean>("polarizedmagnetitempull")!!) item.name(text("Polarized Magnet", arrayOf(36, 36, 255), bold = true))
                    else item.name(text("Polarized Magnet", arrayOf(255, 36, 36), bold = true))
                    CustomEffects.playSound(e.player.location, Sound.BLOCK_CANDLE_PLACE, 1F, 0.9F)
                    e.player.setCooldown(CustomItem.POLARIZED_MAGNET, 0.5)
                } else {
                    if (!ctx.itemType.isHand()) return
                    e.player.setTag("polarizedmagnetpulling", 4)
                    CustomEffects.playSound(e.player.location, Sound.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 1.0F, 1.1F)
                }
            }

        }

    }

}
