package me.newburyminer.customItems.gui.crafting

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.items.CustomEnchantments
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType
import java.io.File
import kotlin.collections.iterator

class RecipeCreationGui: CustomGui() {
    override val inv: Inventory = Bukkit.createInventory(this, 27, Utils.text("Recipe Creation").style(Style.style(TextDecoration.BOLD)))

    init {
        inv.setItem(12, ItemStack(Material.STONE))
        inv.setItem(13, ItemStack(Material.STONE))
        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
        inv.setItem(12, null)
        inv.setItem(13, null)
    }

    override fun open(player: Player) {
        player.openInventory(inventory)
    }

    override fun onClose(e: InventoryCloseEvent) {
        val chest1 = inv.getItem(12) ?: return
        val chest2 = inv.getItem(13) ?: return

        val grid = getGrid(
            chest1.getData(DataComponentTypes.CONTAINER)?.contents() ?: return,
            chest2.getData(DataComponentTypes.CONTAINER)?.contents() ?: return
        )

        /*
        recipe {
            grid {
                row(null, null, item(Material.WIND_CHARGE, 32), item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.IRON_INGOT, 64))
                row(null, null, item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.LEAD, 32), item(Material.PRISMARINE_SHARD, 32))
                row(null, null, item(Material.NETHERITE_SWORD), null, null)
                row(item(Material.PRISMARINE_SHARD, 32), item(Material.LEAD, 32), item(Material.FISHING_ROD).ench("UN3", "MN1"), null, null)
                row(item(Material.IRON_INGOT, 64), item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.WIND_CHARGE, 32), null, null)
            }
            result(CustomItem.HOOKED_CUTLASS)
        }
         */
        var finalString = "recipe {\n\tgrid {\n"
        for (row in grid) {
            var rowString = "\t\trow("
            for (item in row) {
                rowString += itemToCode(item) + ", "
            }
            rowString = rowString.substring(0, rowString.length - 2)
            rowString += ")\n"
            finalString += rowString
        }
        finalString += "\t}\n\tresult(CustomItem.)\n}"

        val outputFile = File("outrecipe.txt")
        if (!outputFile.exists()) outputFile.createNewFile()
        outputFile.writeText(finalString)
    }

    private fun getGrid(storedInv1: MutableList<ItemStack>, storedInv2: MutableList<ItemStack>): MutableList<MutableList<ItemStack>> {
        val grid = mutableListOf(
            mutableListOf<ItemStack>(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
        )

        for (i in 0..4) {
            grid[0].add(storedInv1[i])
            grid[3].add(storedInv2[i])
        }
        for (i in 9..13) {
            grid[1].add(storedInv1[i])
            grid[4].add(storedInv2[i])
        }
        for (i in 18..22) {
            grid[2].add(storedInv1[i])
        }

        return grid
    }
    private fun itemToCode(item: ItemStack): String {
        if (item.type == Material.AIR) return "null"
        val amountComponent = if (item.amount == 1) "" else ", ${item.amount}"
        if (item.getCustom() != null) return "custom(CustomItem.${item.getCustom()}${amountComponent})"

        var baseItem = "item(Material.${item.type}${amountComponent})"
        if (item.enchantments.keys.contains(CustomEnchantments.DUPLICATE)) {
            baseItem += ".checkOriginal()"
            item.removeEnchantment(CustomEnchantments.DUPLICATE)
        }

        if (item.enchantments.isNotEmpty()) {
            var enchs = ""
            for (ench in item.enchantments) {
                enchs += "\"${Utils.convertEnchToStr(ench.toPair())}\","
            }
            baseItem += ".ench(${enchs.substring(0, enchs.length-1)})"
        }

        if (item.hasData(DataComponentTypes.STORED_ENCHANTMENTS)) {
            var enchs = ""
            for (ench in item.getData(DataComponentTypes.STORED_ENCHANTMENTS)!!.enchantments()) {
                enchs += "\"${Utils.convertEnchToStr(ench.toPair())}\","
            }
            baseItem += ".storeEnch(${enchs.substring(0, enchs.length-1)})"
        }

        if (item.hasData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER)) {
            baseItem += ".setOminous(${
                item.getData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER)?.amplifier() ?: 0
            })"
        }

        if (item.hasData(DataComponentTypes.POTION_CONTENTS)) {
            val potionType = item.getData(DataComponentTypes.POTION_CONTENTS)?.potion() ?: PotionType.MUNDANE
            baseItem += ".setPotion(PotionType.${potionType})"
        }

        if (item.hasData(DataComponentTypes.INSTRUMENT)) {
            val goatHornType = item.getData(DataComponentTypes.INSTRUMENT) ?: MusicInstrument.DREAM_GOAT_HORN
            baseItem += ".goatHorn(MusicInstrument.${goatHornType})"
        }

        if (item.hasData(DataComponentTypes.FIREWORKS)) {
            val amplifier = item.getData(DataComponentTypes.FIREWORKS)?.flightDuration() ?: 0
            baseItem += ".firework(${amplifier})"
        }

        return baseItem
    }
}