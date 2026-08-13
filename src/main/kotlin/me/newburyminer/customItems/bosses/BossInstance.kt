package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.loot.BossLoot
import me.newburyminer.customItems.loot.LootContext
import me.newburyminer.customItems.loot.PlayerLootManager
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.KeyedBossBar
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.max

abstract class BossInstance(
    protected val players: MutableList<Player>,
    val bossType: CustomBossType,
    val difficulty: BossDifficulty
) {

    abstract val maxHp: Double
    abstract var currentHp: Double
        protected set
    abstract val damageResist: Double
    fun reduceHp(amount: Double, damageType: DamageType) {
        currentHp -=
            (
                if (damageType == DamageType.MACE_SMASH) amount * 0.1
                else amount
            ) * damageResist

        if (currentHp <= 0.0) {
            bossWin()
        }
    }

    protected abstract fun spawnBoss(): LivingEntity
    lateinit var boss: LivingEntity
        protected set

    protected val bossBar: KeyedBossBar = Bukkit.getServer().createBossBar(NamespacedKey(CustomItems.plugin, UUID.randomUUID().toString()), "", BarColor.RED, BarStyle.SEGMENTED_6)
    abstract fun setupBossbar()
    fun updateBossbar() {
        bossBar.progress = hpPercent.coerceAtLeast(0.0)
    }

    // Starting
    init {
        for (player in players) {
            bossBar.addPlayer(player)
        }
        setupBossbar()
    }

    abstract val loot: BossLoot
    fun bossWin() {
        val toLoot = players.toList()
        endBoss()
        giveLoot(toLoot)
    }
    open fun giveLoot(lootPlayers: List<Player>) {
        lootPlayers.forEach { player ->
            val diff = if (difficulty == BossDifficulty.EASY) "normal" else "hard"
            val context = LootContext(loot.id, diff, 0)
            PlayerLootManager.addLoot(context, player)
            player.sendMessage(Utils.text("Loot has been added to your /lootmenu!", Utils.GRAY))
            player.playSound(player.location, Sound.BLOCK_VAULT_EJECT_ITEM, 1.0F, 1.0F)
        }
    }

    val bottomY: Double
        get() = getLowerCenter().y
    protected abstract val bossCenter: Location
    fun getCenter(): Location {return bossCenter.clone()}
    fun getLowerCenter(): Location {
        val newCenter = bossCenter.clone()
        newCenter.y = 201.0
        return newCenter
    }


    val playerCount: Int get() = players.size
    val currentPlayers: List<Player>
        get() {return players.toList()}
    fun removePlayer(player: Player) {
        if (player !in players) return
        players.remove(player)
        player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
        player.gameMode = GameMode.SURVIVAL
        bossBar.removePlayer(player)
    }
    fun prunePlayers() {
        players.toList().forEach {
            if (it.world != CustomItems.bossWorld)
                removePlayer(it)
        }
        players.removeIf { it.world != CustomItems.bossWorld }
    }

    // Entity handling
    protected val activeEntities: MutableList<Entity> = mutableListOf()
    fun addEntity(entity: Entity) { activeEntities.add(entity) }
    private fun checkEntities() {
        activeEntities.removeIf {
            !it.isValid
        }
    }


    val hpPercent: Double get() = currentHp / maxHp

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

    protected val tasks: MutableList<Int> = mutableListOf()
    fun cancelTasks() {
        tasks.forEach { Bukkit.getScheduler().cancelTask(it) }
    }

    fun playSound(loc: Location, sound: Sound, volume: Float, pitch: Float) {
        players.forEach {
            it.playSound(loc, sound, SoundCategory.HOSTILE, volume, pitch, 1L)
        }
    }

}