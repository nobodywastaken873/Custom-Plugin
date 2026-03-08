package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.ClickCycler
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class PocketknifeMultitool: CustomItemDefinition, ClickCycler {

    override val custom: CustomItem = CustomItem.POCKETKNIFE_MULTITOOL

    private val material = Material.SHEARS
    private val color = arrayOf(166, 166, 166)
    private val name = text("Pocketknife-multitool", color)
    private val lore = Utils.loreBlockToList(
        text("Right click while sneaking to cycle through shears, flint and steel, and a brush.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setUnbreakable()
        .build()

    private val cycleItems = listOf(Material.SHEARS, Material.FLINT_AND_STEEL, Material.BRUSH)
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