package me.newburyminer.customItems.systems

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.structures.BlockLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.roundToInt

object AridLandsSystem: BukkitRunnable() {

    private var isDimensionOpen: Boolean? = null
    private fun checkDimensionOpen(): Boolean {
        return Bukkit.getWorlds()[0].getBlockAt(0, 100, 0).type == Material.REINFORCED_DEEPSLATE
    }
    fun registerEvents() {
        isDimensionOpen = checkDimensionOpen()
        // prevent placing blocks that are not cobblestone, blackstone, cobbled deepslate, stone, deepslate, dirt, coarse dirt, sand, gravel
        EventRegistry.register(ListenerEntry(BlockPlaceEvent::class,
            { e ->
                e.player.world == CustomItems.aridWorld
            },
            {e ->
                if (e.block.type in listOf(
                    Material.COBBLESTONE, Material.BLACKSTONE, Material.COBBLED_DEEPSLATE, Material.STONE, Material.DEEPSLATE, Material.DIRT, Material.COARSE_DIRT,
                    Material.SAND, Material.GRAVEL, Material.RED_SAND, Material.SANDSTONE, Material.DRIPSTONE_BLOCK, Material.RED_SANDSTONE, Material.BASALT,
                    Material.GRANITE, Material.ANDESITE, Material.DIORITE, Material.TERRACOTTA, Material.WHITE_TERRACOTTA, Material.BROWN_TERRACOTTA,
                    Material.ORANGE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.RED_TERRACOTTA, Material.SMOOTH_BASALT, Material.TUFF, Material.MAGMA_BLOCK,
                    Material.CRYING_OBSIDIAN, Material.OBSIDIAN)) return@ListenerEntry

                e.isCancelled = true
            })
        )
        // Prevent players stasising out from the Arid Lands
        EventRegistry.register(ListenerEntry(PlayerTeleportEvent::class,
            { e ->
                e.player.world == CustomItems.aridWorld &&
                e.to.world != CustomItems.aridWorld &&
                e.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL
            },
            {e ->
                e.isCancelled = true
            })
        )

        registerProtectionListeners()
    }
    private fun registerProtectionListeners() {
        EventRegistry.register(ListenerEntry(BlockBreakEvent::class,
            { e -> isDimensionOpen == true && isBlockInPortal(e.block) },
            { it.isCancelled = true })
        )
        EventRegistry.register(ListenerEntry(BlockPlaceEvent::class,
            { e -> isDimensionOpen == true && isBlockInPortal(e.block) },
            { it.isCancelled = true })
        )
        EventRegistry.register(ListenerEntry(EntityExplodeEvent::class,
            { e ->
                isDimensionOpen == true &&
                (e.entity.world == CustomItems.aridWorld || e.entity.world == Bukkit.getWorlds()[0])
            },
            { e ->
                e.blockList().removeIf { isBlockInPortal(it) }
            })
        )
        EventRegistry.register(ListenerEntry(BlockExplodeEvent::class,
            { e ->
                isDimensionOpen == true &&
                (e.block.world == CustomItems.aridWorld || e.block.world == Bukkit.getWorlds()[0])
            },
            { e ->
                e.blockList().removeIf { isBlockInPortal(it) }
            })
        )
    }
    private fun isBlockInPortal(block: Block): Boolean {
        if (block.world != CustomItems.aridWorld && block.world != Bukkit.getWorlds()[0]) return false
        if (block.x !in -4..4 || block.y !in 99..105 || block.z !in -3..3) return false
        return true
    }

    fun spawnPortals() {
        // Inform online players
        Bukkit.getOnlinePlayers().forEach {
            it.sendMessage(Utils.text("The Arid Lands dimension is now open. Head to the portal at 0, 100, 0 in the overworld to enter!", Utils.FAILED_COLOR))
        }

        // make sure that it is empty first
        for (x in -4..4) for (y in 99..105) for (z in -3..3) {
            Bukkit.getWorlds()[0].getBlockAt(x, y, z).type = Material.AIR
            CustomItems.aridWorld.getBlockAt(x, y, z).type = Material.AIR
        }

        // create portal, always use same y-level, check that they are not already there
        for (x in -3..3) for (y in 100..104)  {
            if (x in -2..2 && y in 101..103) continue
            Bukkit.getWorlds()[0].getBlockAt(x, y, 0).type = Material.REINFORCED_DEEPSLATE
            CustomItems.aridWorld.getBlockAt(x, y, 0).type = Material.REINFORCED_DEEPSLATE
        }

        // make platform around portal
        for (x in -4..4) for (z in -3..3) {
            Bukkit.getWorlds()[0].getBlockAt(x, 99, z).type = Material.CRYING_OBSIDIAN
            CustomItems.aridWorld.getBlockAt(x, 99, z).type = Material.CRYING_OBSIDIAN
        }

        // create model entities
        Bukkit.getWorlds()[0].spawn(Location(Bukkit.getWorlds()[0], -2.0, 101.0, 0.375), BlockDisplay::class.java) {
            it.transformation = Transformation(Vector3f(), Quaternionf(0.0, 0.0, 0.0, 1.0),
                Vector3f(5F, 3F, 0.25F), Quaternionf(0.0, 0.0, 0.0, 1.0))
            it.block = Material.CYAN_STAINED_GLASS.createBlockData()
        }
        CustomItems.aridWorld.spawn(Location(Bukkit.getWorlds()[0], -2.0, 101.0, 0.375), BlockDisplay::class.java) {
            it.transformation = Transformation(Vector3f(), Quaternionf(0.0, 0.0, 0.0, 1.0),
                Vector3f(5F, 3F, 0.25F), Quaternionf(0.0, 0.0, 0.0, 1.0))
            it.block = Material.CYAN_STAINED_GLASS.createBlockData()
        }

        isDimensionOpen = true
    }

    // Runs at a rate of 1hz (once per second)
    override fun run() {
        // add -65% mining eff below y=-60
        val aridPlayers = CustomItems.aridWorld.players
        aridPlayers.forEach { player ->
            if (player.y < -60)
                EffectManager.applyEffect(player, CustomEffectType.ATTRIBUTE, EffectData(220,
                    AttributeData(-0.65, Attribute.MINING_EFFICIENCY, Operation.MULTIPLY_SCALAR_1), true))
        }

        if (isDimensionOpen == true) {
            // check if player in portal location in either dimension
            val boundingBox = BoundingBox.of(Vector(4, 100, 1), Vector(-3, 105, 0))

            Bukkit.getWorlds()[0].players.forEach { player ->
                if (boundingBox.contains(player.boundingBox)) {
                    player.teleport(Location(CustomItems.aridWorld, 0.5, 100.0, 2.5), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
                    player.playSound(player.location, Sound.BLOCK_PORTAL_TRAVEL, 2f, 0.8f)
                }
            }

            aridPlayers.forEach { player ->
                if (boundingBox.contains(player.boundingBox)) {
                    player.teleport(Location(Bukkit.getWorlds()[0], 0.5, 100.0, 2.5), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
                    player.playSound(player.location, Sound.BLOCK_PORTAL_TRAVEL, 2f, 0.8f)
                }
            }
        }
        // ???
    }

}