package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.behaviors.ClickCycler
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class NetheriteMultitool: CustomItemDefinition, ClickCycler {

    override val custom: CustomItem = CustomItem.NETHERITE_MULTITOOL

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(89, 14, 7)
    private val name = text("Netherite Multitool", color)
    private val lore = Utils.loreBlockToList(
        text("Right click while sneaking to cycle through a netherite pickaxe, axe, shovel, and hoe.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    private val cycleItems = listOf(Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE)
    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.hand == EquipmentSlot.HAND &&
            e.player.offCooldown(custom) &&
            isRightClick(e) &&
            e.player.isSneaking
        },
        {e ->
            cycleItem(e.item ?: return@register, cycleItems)
            e.player.setCooldown(custom, 0.1)
        })
    }

}