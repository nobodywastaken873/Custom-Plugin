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
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class Axepick: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AXEPICK

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(96, 99, 40)
    private val name = text("Axepick", color)
    private val lore = Utils.loreBlockToList(
        text("Works as an axe and pickaxe when breaking blocks.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .tagTool(Tag.MINEABLE_AXE, 9F)
        .tagTool(Tag.MINEABLE_PICKAXE, 9F)
        .duraBroken(1)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockBreakEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val item = ctx.item ?: return
                val newMeta = item.itemMeta as Damageable
                if (Math.random() < (1.0 - 1.0 / (1 + (item.enchantments[Enchantment.UNBREAKING] ?: 0)))) return
                newMeta.damage += 1
                if (newMeta.damage == 2031) {item.amount = 0; CustomEffects.playSound(e.player.location, Sound.ENTITY_ITEM_BREAK, 1F, 1F)}
                item.itemMeta = newMeta
            }

        }

    }

}