package me.newburyminer.customItems.bosses.definitions.wither

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.setAttr
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.bosses.CustomBossType
import me.newburyminer.customItems.bosses.definitions.warden.WardenController
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.bosses.WardenBossComponent
import me.newburyminer.customItems.entity.components.bosses.WitherBossComponent
import me.newburyminer.customItems.loot.BossLoot
import me.newburyminer.customItems.loot.providers.boss.WardenLoot
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Warden
import org.bukkit.entity.Wither

class WitherInstance(players: MutableList<Player>, difficulty: BossDifficulty): BossInstance(players, CustomBossType.WITHER, difficulty) {

    override val loot: BossLoot = WardenLoot // TODO
    override val bossCenter: Location = Location(CustomItems.bossWorld, -58.5, 201.0, 59.5)
    override val damageResist: Double
        get() = 1.0 / players.size
    override val maxHp: Double = 4500.0 * (if (difficulty == BossDifficulty.EASY) 1.0 else 1.8)
    override var currentHp: Double = maxHp

    init {
        boss = spawnBoss()
        EntityWrapperManager.getWrapperorNew(boss).addComponent(WitherBossComponent(this))
    }

    override val actionController: WitherController = WitherController(this)

    override fun setupBossbar() {
        bossBar.color = BarColor.RED
        bossBar.setTitle("The Wither")
        bossBar.progress = 1.0
    }
    override fun spawnBoss(): LivingEntity {
        val wither = getCenter().world.spawn(bossCenter, Wither::class.java) {
            it.setAttr(Attribute.MAX_HEALTH, 250.0)
            //it.getAttribute(Attribute.ATTACK_DAMAGE)?.baseValue *= 1.2 * (if (difficulty == BossDifficulty.EASY) 1.0 else 1.5)
            it.health = 250.0
            it.setAI(false)
        }
        return wither
    }

    fun checkStun() {
        actionController.checkStun()
    }
    fun endStun() {
        actionController.endStun()
    }
    fun start() {
        actionController.startPhase(WitherController.Phase.Phase1)
    }

}