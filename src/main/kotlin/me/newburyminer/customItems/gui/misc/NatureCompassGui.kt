package me.newburyminer.customItems.gui.misc

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.gui.ItemAction
import me.newburyminer.customItems.gui.PagedGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.loot.PlayerPityManager
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

class NatureCompassGui(private val player: Player, private val itemToConsume: ItemStack, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Nature Compass").style(Style.style(TextDecoration.BOLD)))
    private val allBiomes = generateBiomeMap()
    private val itemsPerPage = 35

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {

        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.GREEN_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val (world, biome) = allBiomes.getOrNull(i) ?: break

            val (material, color) = when (world) {
                Bukkit.getWorlds()[0] -> Material.GRASS_BLOCK to arrayOf(7, 171, 10)
                Bukkit.getWorlds()[1] -> Material.NETHERRACK to arrayOf(128, 31, 20)
                Bukkit.getWorlds()[2] -> Material.END_STONE to arrayOf(209, 214, 135)
                CustomItems.aridWorld -> Material.RED_SAND to arrayOf(209, 98, 33)
                else -> Material.BEDROCK to arrayOf(0, 0, 0)
            }

            val name = biome.key.value().beautify()

            val pityItem = ItemStack(material)
                .lock()
                .setItemAction(ItemAction.OPEN_SUBMENU)
                .name(Utils.text(name, color))
                .lore(Utils.text("Click to find, make sure it is in this dimension.", Utils.GRAY))
                .setTag("index", i)

            inv.addItem(pityItem)
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (allBiomes.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun attemptFindBiome(biome: Biome, useCenter: Boolean = false) {

        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            player.closeInventory()
        })

        val centerLoc = if (useCenter) Location(player.world, 0.0, 100.0, 0.0) else player.location
        val result = player.world.locateNearestBiome(
            centerLoc,
            10000, // radius in blocks
            biome
        )

        if (result == null || abs(result.location.x) > 10000.0 || abs(result.location.z) > 10000.0) {
            if (!useCenter) {attemptFindBiome(biome, true); return}
            player.sendMessage(Utils.text("Failed to find biome. Either you are in the wrong dimension, or try again somewhere else.", Utils.FAILED_COLOR))
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            return
        }

        val resultLoc = result.location
        val direction = resultLoc.clone().subtract(player.location).toVector().normalize()

        val look = player.location.clone()
        look.setDirection(direction)

        player.teleport(
            player.location.clone().apply {
                yaw = look.yaw
                pitch = look.pitch
            }
        )

        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                count++
                if (count == 40) { cancel() }
                CustomEffects.raycastParticleLine(Particle.HAPPY_VILLAGER.builder(), player.location, direction, 20.0, 3.0)
            }
        }.runTaskTimer(CustomItems.plugin, 1L, 5L)

        player.sendMessage(Utils.text("Successfully found biome, showing direction temporarily...", Utils.SUCCESS_COLOR))
        player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.9F)

        itemToConsume.amount -= 1
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                attemptFindBiome(allBiomes[clickedItem.getTag<Int>("index") ?: return].second)
            }
            else -> {}
        }
    }

    private fun generateBiomeMap(): List<Pair<World, Biome>> {
        val allBiomes = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).iterator().asSequence().toMutableList()
        val netherBiomes = listOf(Biome.NETHER_WASTES, Biome.SOUL_SAND_VALLEY, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.BASALT_DELTAS)
        val endBiomes = listOf(Biome.THE_END, Biome.SMALL_END_ISLANDS, Biome.END_MIDLANDS, Biome.END_HIGHLANDS, Biome.END_BARRENS)

        allBiomes.removeIf {
            it in netherBiomes ||
            it in endBiomes
        }

        val aridLandsBiomes = allBiomes.filter { it.key.namespace == "customworld" }
        val overworldBiomes = allBiomes.filter { it.key.namespace == "minecraft" }

        val allBiomePairs = mutableListOf<Pair<World, Biome>>()
        allBiomePairs.addAll(overworldBiomes.map { Bukkit.getWorlds()[0] to it }.sortedBy { it.second.key.value() })
        allBiomePairs.addAll(netherBiomes.map { Bukkit.getWorlds()[1] to it }.sortedBy { it.second.key.value() })
        allBiomePairs.addAll(endBiomes.map { Bukkit.getWorlds()[2] to it }.sortedBy { it.second.key.value() })
        allBiomePairs.addAll(aridLandsBiomes.map { CustomItems.aridWorld to it }.sortedBy { it.second.key.value() })

        return allBiomePairs
    }

}