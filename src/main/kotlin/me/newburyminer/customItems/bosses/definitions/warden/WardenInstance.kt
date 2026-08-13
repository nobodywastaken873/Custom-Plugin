package me.newburyminer.customItems.bosses.definitions.warden

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.setAttr
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.bosses.CustomBossType
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.bosses.WardenBossComponent
import me.newburyminer.customItems.loot.BossLoot
import me.newburyminer.customItems.loot.providers.boss.WardenLoot
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Warden

class WardenInstance(players: MutableList<Player>, difficulty: BossDifficulty): BossInstance(players, CustomBossType.WARDEN, difficulty) {

    override val loot: BossLoot = WardenLoot
    override val bossCenter: Location = Location(CustomItems.bossWorld, -58.5, 203.0, -15.5)
    override val damageResist: Double
        get() = 1.0 / players.size
    override val maxHp: Double = 3000.0 * (if (difficulty == BossDifficulty.EASY) 1.0 else 1.8)
    override var currentHp: Double = maxHp

    init {
        boss = spawnBoss()
        EntityWrapperManager.getWrapperorNew(boss).addComponent(WardenBossComponent(this))
    }

    override val actionController: WardenController = WardenController(this)

    override fun setupBossbar() {
        bossBar.color = BarColor.BLUE
        bossBar.setTitle("The Warden")
        bossBar.progress = 1.0
    }
    override fun spawnBoss(): LivingEntity {
        val warden = getCenter().world.spawn(bossCenter, Warden::class.java) {
            it.setAttr(Attribute.MAX_HEALTH, 250.0)
            it.getAttribute(Attribute.ATTACK_DAMAGE)?.baseValue *= 1.2 * (if (difficulty == BossDifficulty.EASY) 1.0 else 1.5)
            it.health = 250.0
            it.setAI(false)
        }
        return warden
    }
    override fun endBoss() {
        super.endBoss()
        for (offset in arrayOf(Pair(1.0, 1.0), Pair(1.0, -1.0), Pair(-1.0, 1.0), Pair(-1.0, -1.0))) {
            for (x in 8..9) for (z in 8..9) {
                getLowerCenter().add(x * offset.first, 1.0, z * offset.second).block.type = Material.SOUL_FIRE
            }
        }
    }

    fun checkStunning() {
        if (actionController.reachedStunThreshold())
            actionController.endStun()
    }
    fun stun() {
        actionController.stun()
    }
    fun start() {
        actionController.startPhase(WardenController.Phase.Phase1)
    }

}