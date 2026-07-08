package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.structures.CustomLootTable
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.KeyedBossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

abstract class BossInstance(
    protected val players: MutableList<Player>,
    val bossType: CustomBossType
) {

    protected abstract fun spawnBoss(): LivingEntity
    lateinit var boss: LivingEntity
        protected set

    protected val bossBar: KeyedBossBar = Bukkit.getServer().createBossBar(NamespacedKey(CustomItems.plugin, UUID.randomUUID().toString()), "", BarColor.RED, BarStyle.SEGMENTED_6)
    abstract fun setupBossbar()
    fun updateBossbar() {
        bossBar.progress = hpPercent
    }

    // Starting
    init {
        for (player in players) {
            bossBar.addPlayer(player)
        }
        setupBossbar()
    }

    abstract val loot: CustomLootTable
    fun bossWin() {
        val toLoot = players.toMutableList()
        endBoss()
        giveLoot(toLoot)
    }
    open fun giveLoot(lootPlayers: List<Player>) {
        lootPlayers.forEach { player -> loot.roll().forEach {
            player.addItemorDrop(it)
        }}
    }

    protected abstract val bossCenter: Location
    fun getCenter(): Location {return bossCenter.clone()}
    fun getLowerCenter(): Location {
        val newCenter = bossCenter.clone()
        newCenter.y = 201.0
        return newCenter
    }


    val playerCount: Int get() = players.size
    val currentPlayers: List<Player> = players.toList()
    fun removePlayer(player: Player) {
        if (player !in players) return
        players.remove(player)
        player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
        player.gameMode = GameMode.SURVIVAL
        bossBar.removePlayer(player)
    }
    fun prunePlayers() {
        players.iterator().forEach {
            if (it.world != CustomItems.bossWorld)
                removePlayer(it)
        }
        players.removeIf { it.world != CustomItems.bossWorld }
    }

    // Entity handling
    protected val activeEntities: MutableList<Entity> = mutableListOf()
    private fun checkEntities() {
        activeEntities.removeIf {
            !it.isValid
        }
    }


    val hpPercent: Double get() = boss.health / boss.getAttribute(Attribute.MAX_HEALTH)!!.baseValue

    abstract val actionController: ActionController

    open fun tick() {
        checkEntities()
        prunePlayers()

        actionController.tick()
        if (players.isEmpty()) { endBoss() }
    }

    // Call on death, or when stopping server
    private var markedForRemoval = false
    fun getToRemove(): Boolean = markedForRemoval
    open fun endBoss() {
        actionController.cancelAll()
        cancelTasks()
        boss.remove()
        activeEntities.forEach { it.remove() }
        players.forEach {
            it.teleport(it.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
            it.gameMode = GameMode.SURVIVAL
        }
        bossBar.removeAll()
        markedForRemoval = true
    }

    // TODO: make this not bad and actually check hitboxes, probably add a mobprovider function so that the boss can determine what mobs it wants to spawn
    protected val mobSpawnRadius: Double = 15.0
    fun getValidMobSpawn(): Location {
        var possOffset = Vector(Utils.randomRange(-mobSpawnRadius, mobSpawnRadius), 0.0, Utils.randomRange(-mobSpawnRadius, mobSpawnRadius))
        while (bossCenter.clone().add(possOffset).block.type != Material.AIR ||
            bossCenter.clone().add(possOffset).add(0.0, 1.0, 0.0).block.type != Material.AIR ||
            bossCenter.clone().add(possOffset).getNearbyPlayers(5.0).isNotEmpty()
        ) {
            possOffset = Vector(Utils.randomRange(-mobSpawnRadius, mobSpawnRadius), 0.0, Utils.randomRange(-mobSpawnRadius, mobSpawnRadius))
        }
        return bossCenter.clone().add(possOffset).block.location
    }

    protected val tasks: MutableList<Int> = mutableListOf()
    fun cancelTasks() {
        tasks.forEach { Bukkit.getScheduler().cancelTask(it) }
    }

    fun playSound(loc: Location, sound: Sound, volume: Float, pitch: Float) {
        players.forEach {
            it.playSound(loc, sound, volume, pitch)
        }
    }

}