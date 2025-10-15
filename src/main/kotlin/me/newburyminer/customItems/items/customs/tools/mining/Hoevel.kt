package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.duraBroken
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.tagTool
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class Hoevel: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HOEVEL

    private val material = Material.NETHERITE_SHOVEL
    private val color = arrayOf(99, 45, 40)
    private val name = text("Hoevel", color)
    private val lore = Utils.loreBlockToList(
        text("Works as a shovel and hoe when breaking blocks.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .tagTool(Tag.MINEABLE_HOE, 9F)
        .tagTool(Tag.MINEABLE_SHOVEL, 9F)
        .duraBroken(1)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockBreakEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val item = ctx.item ?: return
                val newMeta = item.itemMeta as Damageable
                if (Math.random() < (1.0 - 1.0 / (1 + (item.enchantments[Enchantment.UNBREAKING] ?: 0)))) return
                newMeta.damage += 1
                item.itemMeta = newMeta
                if (newMeta.damage == 2031) {
                    val event = PlayerItemBreakEvent(e.player, item)
                    Bukkit.getPluginManager().callEvent(event)
                    CustomEffects.playSound(e.player.location, Sound.ENTITY_ITEM_BREAK, 1F, 1F)
                }
            }

        }

    }

}