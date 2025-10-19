package me.newburyminer.customItems

import me.newburyminer.customItems.Utils.Companion.combatTime
import me.newburyminer.customItems.Utils.Companion.compassCooldown
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isTracking
import me.newburyminer.customItems.Utils.Companion.remainingCompassTime
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.effects.CustomEffectBootstrapper
import me.newburyminer.customItems.effects.EffectEventHandler
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entities.EntityListeners
import me.newburyminer.customItems.entities.bosses.BossListeners
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.gui.GuiEventHandler
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSetBootstrapper
import me.newburyminer.customItems.items.armorsets.ArmorSetEventHandler
import me.newburyminer.customItems.recipes.RecipeRegistry
import me.newburyminer.customItems.structures.LootListener
import me.newburyminer.customItems.systems.EnchantmentListener
import me.newburyminer.customItems.systems.GraveListener
import me.newburyminer.customItems.systems.SystemsListener
import me.newburyminer.customItems.systems.materials.MaterialConverterBootstrapper
import me.newburyminer.customItems.systems.playertask.PlayerTaskHandler
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class CustomItems : JavaPlugin() {
    companion object {
        lateinit var plugin: Plugin
        lateinit var aridWorld: World
        lateinit var bossWorld: World
        lateinit var bossListener: BossListeners
    }

    private lateinit var cooldownTask: BukkitTask
    private lateinit var entityListener: EntityListeners
    private lateinit var systemsListener: SystemsListener

    override fun onEnable() {
        logger.info("Custom items has successfully loaded!")
        plugin = this
        aridWorld = this.server.getWorld(Key.key("minecraft:new_dimension"))!!
        aridWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        aridWorld.time = 20000
        bossWorld = Bukkit.createWorld(WorldCreator("testworld"))!!
        bossWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        bossWorld.time = 20000
        loadBosses()
        CustomBoss.init()
        ItemBootstrapper.registerAll(this)
        ArmorSetBootstrapper.registerAll(this)
        RecipeRegistry.registerAll()
        entityListener = EntityListeners()
        bossListener = BossListeners()
        systemsListener = SystemsListener()
        registerListeners()
        systemsListener.run()
        this.run()
        PlayerTaskHandler.runTaskTimer(this, 0L, 1L)
        EffectManager.runTaskTimer(this, 0L, 1L)
        entityListener.run()
        bossListener.run()
        MaterialConverterBootstrapper.registerAll()
        CustomEffectBootstrapper.registerAll()
    }

    private fun loadBosses() {
        for (x in -7..6) for (z in -7..6) {
            aridWorld.setChunkForceLoaded(x, z, true)
        }
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(entityListener, this)
        server.pluginManager.registerEvents(LootListener(), this)
        server.pluginManager.registerEvents(bossListener, this)
        server.pluginManager.registerEvents(DurabilityListener(), this)
        server.pluginManager.registerEvents(systemsListener, this)
        server.pluginManager.registerEvents(ItemEventHandler(), this)
        server.pluginManager.registerEvents(ArmorSetEventHandler(), this)
        server.pluginManager.registerEvents(EffectEventHandler(), this)
        server.pluginManager.registerEvents(GraveListener(), this)
        server.pluginManager.registerEvents(EnchantmentListener(), this)
        server.pluginManager.registerEvents(GuiEventHandler(), this)
    }

    private fun run() {
        cooldownTask = Bukkit.getServer().scheduler.runTaskTimer(this, Runnable {
            for (player in this.server.onlinePlayers) {
                var cooldownString = ""
                for (cooldown in CustomItem.entries) {
                    for (i in cooldown.cds) {
                        val prevTimeLeft = player.getTag<Double>("${cooldown.name}_$i") ?: 0.1
                        if (prevTimeLeft >= 0.1) {
                            player.setTag("${cooldown.name}_$i", prevTimeLeft - 0.1)
                            cooldownString += "${cooldown.realName + " " + i} - ${prevTimeLeft.round(1)}s "
                        }
                    }
                }
                var component = Component.text("$cooldownString ").color(TextColor.color(255, 255, 255))
                if (player.isInCombat()) {
                    component = component.append(
                        Utils.text("In Combat - ${((player.combatTime() / 20.0) * 10).toInt() / 10.0}s ",
                            Utils.FAILED_COLOR, bold = true))
                }
                if (player.isBeingTracked()) {
                    component = component.append(
                        Utils.text("Being Tracked - ${((player.remainingCompassTime() / 20.0) * 10).toInt() / 10.0}s ",
                            Utils.FAILED_COLOR, bold = true))
                }
                if (player.isTracking()) {
                    component = component.append(
                        Utils.text("Tracking Remaining - ${((player.compassCooldown() / 20.0) * 10).toInt() / 10.0}s ",
                            Utils.SUCCESS_COLOR, bold = true))
                }
                if (!component.content().all { it == " ".first() } || player.isInCombat() || player.isBeingTracked() || player.isTracking())
                    player.sendActionBar(component)
            }
        }, 0L, 2L)
    }

    override fun onDisable() {
        cooldownTask.cancel()
        entityListener.cancel()
        bossListener.cancelAll()
        systemsListener.cancel()
        PlayerTaskHandler.cancelAll()
        closeMenus()
    }

    private fun closeMenus() {
        for (player in Bukkit.getServer().onlinePlayers) {
            val openInventory = player.openInventory
            if (openInventory?.topInventory?.holder is CustomGui) {
                (openInventory.topInventory.holder as CustomGui).onClose(InventoryCloseEvent(openInventory, InventoryCloseEvent.Reason.UNKNOWN))
            }

            player.closeInventory()
        }
    }


}
