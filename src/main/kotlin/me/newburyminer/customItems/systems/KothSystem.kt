package me.newburyminer.customItems.systems

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isAfk
import me.newburyminer.customItems.bosses.Collision
import me.newburyminer.customItems.bosses.rendering.HollowCylinderRenderable
import me.newburyminer.customItems.bosses.rendering.RenderManager
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.structures.BlockLocation
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Biome
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.util.Vector
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

object KothSystem: BukkitRunnable(), TabMenuSystem.Provider {

    fun registerEvents() {
        EventRegistry.register(ListenerEntry(PlayerJoinEvent::class,
            {
                kothRemaining > 0
            },
            {e ->
                informKoth(e.player)
                // reset koth score if over
            })
        )
        EventRegistry.register(ListenerEntry(BlockBreakEvent::class,
            { e -> e.block.world == Bukkit.getWorlds()[0] },
            {e ->
                if (BlockLocation(e.block.location) !in immutableLocations) return@ListenerEntry
                e.isCancelled = true
            })
        )

        EventRegistry.register(ListenerEntry(EntityExplodeEvent::class,
            { e -> e.entity.world == Bukkit.getWorlds()[0] },
            {e ->
                val blocks = e.blockList()
                blocks.removeIf {
                    BlockLocation(it.location) in immutableLocations
                }
            })
        )

        EventRegistry.register(ListenerEntry(BlockExplodeEvent::class,
            { e -> e.block.world == Bukkit.getWorlds()[0] },
            {e ->
                val blocks = e.blockList()
                blocks.removeIf {
                    BlockLocation(it.location) in immutableLocations
                }
            })
        )
    }

    // This runs once a minute
    private val zone = ZoneId.of("America/Los_Angeles")
    override fun run() {
        if (kothRemaining <= 0 && objective.displaySlot != null) { objective.displaySlot = null }
        val currentTime = LocalTime.now(zone)

        if (currentTime.isAfter(LocalTime.of(13, 0)) && currentTime.isBefore(LocalTime.of(13, 1))) {
            Bukkit.getOnlinePlayers().forEach { player ->
                player.sendMessage(Utils.text("KOTH will start in 1 hour, at 2 PM. There must be at least 5 active, non-AFK players online at some point between" +
                        " 2:00 and 2:30 in order for KOTH to start. You can use /kothloot to check the rewards for the day.", Utils.BLUE))
            }
        }

        val startTime = LocalTime.of(14, 0)
        val endTime = LocalTime.of(14, 30)

        val inRange = currentTime.isAfter(startTime) && currentTime.isBefore(endTime)

        if (!inRange) return
        if (kothRemaining > 0) return

        // KOTH has not started, but it is between 2:00 and 2:30
        val activePlayers = Bukkit.getOnlinePlayers().filter { !it.isAfk() }.size
        if (activePlayers < 5) return

        startKoth()
    }

    private val objective = Bukkit.getScoreboardManager().mainScoreboard.getObjective("koth")
        ?: Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective(
            "koth",
            Criteria.DUMMY,
            Component.text("KOTH")
        )

    private var kothRemaining = 0 // Seconds
    private var center: Location? = null
    private var cylinder: HollowCylinderRenderable? = null
    private val renderer: RenderManager = RenderManager()
    private val immutableLocations = mutableListOf<BlockLocation>()
    fun startKoth(duration: Int = 60 * 30) {
        objective.displaySlot = DisplaySlot.PLAYER_LIST
        center = pickLocation(0)
        copyArenaTo(center ?: return)
        kothRemaining = duration
        cylinder = HollowCylinderRenderable(
            Transform(
                center?.clone()?.toVector() ?: Vector(0.0, 0.0, 0.0),
                Transform.lookRotation(Vector(0, 1, 0), Vector(0, 0, 1)),
            ),
            5.0,
            6.5,
            Material.YELLOW_STAINED_GLASS,
            segments = 10
        )
        renderer.add(cylinder ?: return)
        cylinder?.spawn(center?.world ?: return) ?: return

        Bukkit.getOnlinePlayers().forEach { informKoth(it) }
        scoreMap.clear()
        kothUpdater = object : BukkitRunnable() { override fun run() { updateKoth() } }
        kothUpdater?.runTaskTimer(CustomItems.plugin, 1L, 1L)
    }

    private fun informKoth(player: Player) {
        val loc = center ?: return
        player.sendMessage(Utils.text("KOTH has started at x: ${(loc.x / 10).roundToInt() * 10}, y: ${(loc.y / 10).roundToInt() * 10}, z: ${(loc.z / 10).roundToInt() * 10}. " +
                "It has ${ceil(kothRemaining / 60.0)} minutes left, and the two players with the most time in the center area will receive rewards. " +
                "Find more info in the KOTH section of /info.", Utils.BLUE))
        player.playSound(player.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1F)
    }
    private fun pickLocation(depth: Int): Location {
        if (depth > 100) {
            CustomItems.plugin.logger.severe("Cannot find location.")
            return Location(Bukkit.getWorlds()[0], 0.0, 100.0, 0.0)
        }

        val x = (Math.random() * 2 - 1) * 9000
        val z = (Math.random() * 2 - 1) * 9000
        val loc = Location(Bukkit.getWorlds()[0], x, 100.0, z)

        // Should not be close to spawn
        if (Vector(x, 0.0, z).length() < 1000) return pickLocation(depth + 1)

        // Should not be in the ocean
        val biome = loc.block.biome
        if (biome in listOf(
            Biome.OCEAN, Biome.DEEP_OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.WARM_OCEAN,
            Biome.FROZEN_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.DEEP_LUKEWARM_OCEAN
        )) return pickLocation(depth + 1)

        // Should not be near any chunks with >30 min spent in them
        val chunkX = loc.chunk.x
        val chunkZ = loc.chunk.z
        for (cx in (chunkX-12)..(chunkX+12)) for (cz in (chunkZ-12)..(chunkZ+12)) {
            if (loc.world.getChunkAt(cx, cz).inhabitedTime > 20 * 60 * 30) return pickLocation(depth + 1)
        }
        // Location is good, step down to the highest point
        loc.y = 320.0
        while (loc.block.isPassable && !loc.block.isLiquid) {
            loc.y -= 1
        }
        loc.y += 1
        return loc.block.location.add(0.5, 0.5, 0.5)
    }
    private fun copyArenaTo(location: Location) {
        for (x in -10..10) for (y in 172..184) for (z in -10..10) {
            val arenaBlock = CustomItems.bossWorld.getBlockAt(x, y, z)
            val worldLoc = Vector(floor(x + location.x), floor(y - 172 + location.y), floor(z + location.z))
            val worldBlock = location.world.getBlockAt(worldLoc.toLocation(location.world))

            worldBlock.type = arenaBlock.type
            worldBlock.blockData = arenaBlock.blockData

            if (arenaBlock.type != Material.AIR) {
                immutableLocations.add(BlockLocation(worldLoc.toLocation(location.world)))
            }
        }
    }

    private val scoreMap = mutableMapOf<UUID, Int>()
    private var kothUpdater: BukkitRunnable? = null
    private fun updateKoth() {
        cylinder?.transform?.rotateWorldY(0.003F)
        renderer.tick()
        if (Bukkit.getCurrentTick() % 20 == 0) {
            if (kothRemaining == 1200 || kothRemaining == 600) {
                Bukkit.getOnlinePlayers().forEach { informKoth(it) }
            }
            kothRemaining -= 1

            val start = center?.clone() ?: return
            for (player in start.getNearbyPlayers(20.0)) {
                if (Collision.cylinderIntersects(start.clone(), start.clone().add(Vector(0.0, 6.5, 0.0)), 5.0, player.boundingBox))
                    scoreMap[player.uniqueId] = (scoreMap[player.uniqueId] ?: 0) + 1
            }

            updateScores()

            if (kothRemaining <= 0) {endKoth()}
        }
    }

    private fun updateScores() {
        Bukkit.getOnlinePlayers().forEach {
            objective.getScore(it.name).score = scoreMap[it.uniqueId] ?: 0
        }
    }
    private fun endKoth() {
        objective.displaySlot = null
        immutableLocations.clear()
        center = null
        renderer.clear()
        cylinder = null

        kothUpdater?.cancel()
        kothUpdater = null
        giveRewards()
        scoreMap.clear()
    }

    private val dailyLoot: Map< LocalDate, Pair<List<ItemStack>, List<ItemStack>> > = mapOf(
        LocalDate.of(2026, 7, 23) to (listOf(
            ItemRegistry.get(CustomItem.REFRESHING_EMERALD)
        ) to listOf(
            ItemRegistry.get(CustomItem.REINFORCED_CAGE), ItemRegistry.get(CustomItem.ENCHANTED_EMERALD_APPLE)
        )),
        LocalDate.of(2026, 7, 24) to (listOf(
            ItemRegistry.get(CustomItem.GOLDEN_ZOMBIE), ItemRegistry.get(CustomItem.POCKETKNIFE_MULTITOOL), ItemRegistry.get(CustomItem.POCKETKNIFE_MULTITOOL),
        ) to listOf(
            ItemRegistry.get(CustomItem.ITEM_NODE), ItemRegistry.get(CustomItem.ITEM_NODE), ItemRegistry.get(CustomItem.ITEM_NODE), ItemRegistry.get(CustomItem.ITEM_NODE),
        )),
        LocalDate.of(2026, 7, 25) to (listOf(
            ItemRegistry.get(CustomItem.JERRY_IDOL), ItemRegistry.get(CustomItem.PEGASUS_ARMOR)
        ) to listOf(
            ItemRegistry.get(CustomItem.REDSTONE_AMALGAMATION)
        )),
        LocalDate.of(2026, 7, 26) to (listOf(
            ItemRegistry.get(CustomItem.EXPERIENCE_FLASK)
        ) to listOf(
            ItemRegistry.get(CustomItem.TRACKING_COMPASS)
        )),
        LocalDate.of(2026, 7, 27) to (listOf(
            ItemRegistry.get(CustomItem.DRINKING_HAT)
        ) to listOf(
            ItemRegistry.get(CustomItem.JETPACK_CONTROLLER_SET)
        ))
    )
    private fun giveRewards() {
        val loot = dailyLoot[LocalDate.now(ZoneId.of("America/Los_Angeles"))] ?: return
        val sorted = scoreMap.toList()
            .sortedBy { (_, value) -> value }
            .reversed()

        if (sorted.isEmpty()) {
            Bukkit.getOnlinePlayers().forEach { it.sendMessage(Utils.text("Noone attended KOTH, it has finished.", Utils.BLUE)) }
        } else if (sorted.size == 1) {
            val name = Bukkit.getOfflinePlayer(sorted.first().first).name ?: ""
            Bukkit.getOnlinePlayers().forEach { it.sendMessage(Utils.text("Only $name attended KOTH, they have received both rewards for the day.", Utils.BLUE)) }
            // give both rewards
            (loot.first + loot.second).forEach {
                Utils.addClaim(sorted.first().first, it)
            }
        } else {
            val first = sorted[0].first
            val second = sorted[1].first

            val firstName = Bukkit.getOfflinePlayer(first).name ?: ""
            val secondName = Bukkit.getOfflinePlayer(second).name ?: ""

            Bukkit.getOnlinePlayers().forEach { it.sendMessage(Utils.text(
                "$firstName and $secondName were the top two players for KOTH, they have each received their corresponding KOTH reward for the day.", Utils.BLUE
            )) }
            // give each their reward
            loot.first.forEach {
                Utils.addClaim(first, it)
            }
            loot.second.forEach {
                Utils.addClaim(second, it)
            }
        }
    }
    fun getAllRewards(): Map< LocalDate, Pair<List<ItemStack>, List<ItemStack>> > {
        return dailyLoot.toMap()
    }

    // API for tablist info
    override fun getLines(player: Player): List<Component> {
        return if (!isKothActive()) emptyList()
            else listOf(getLocationInfo(), getTopPlayerInfo())
    }
    fun isKothActive(): Boolean {
        return kothRemaining > 0
    }
    fun getLocationInfo(): Component {
        val loc = center ?: return Utils.text("")
        return Utils.text(
            "KOTH active at x: ${(loc.x / 10).roundToInt() * 10}, y: ${(loc.y / 10).roundToInt() * 10}, z: ${(loc.z / 10).roundToInt() * 10}" +
                    " for ${ceil(kothRemaining / 60.0)} minutes", arrayOf(6, 89, 191)
        )
    }
    fun getTopPlayerInfo(): Component {
        val sorted = scoreMap.toList()
            .filter { (uuid, _) -> Bukkit.getPlayer(uuid) != null }
            .sortedBy { (_, value) -> value }

        val top = sorted.lastOrNull() ?:
            return Utils.text("No players with score", arrayOf(6, 89, 191))

        val name = Bukkit.getOfflinePlayer(top.first).name ?: ""
        return Utils.text("Top player: ${name} with ${top.second} seconds", arrayOf(6, 89, 191))
    }

}