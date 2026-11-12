package me.newburyminer.customItems.bosses.definitions.wither

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.random
import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionController
import me.newburyminer.customItems.bosses.AttackContext
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossPhase
import me.newburyminer.customItems.bosses.BossState
import me.newburyminer.customItems.bosses.actions.DelayAction
import me.newburyminer.customItems.bosses.actions.RepeatAction
import me.newburyminer.customItems.bosses.definitions.warden.WardenAttacks
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.util.Vector

class WitherController(
    bossInstance: WitherInstance
): ActionController(bossInstance) {

    private val bossEntity = boss.boss

    sealed class Phase(val threshold: Double, val cycles: Int): BossPhase<Phase> {
        object Intro : Phase(100.0, 0)
        object Phase1 : Phase(1.0, 4)
        object Phase2 : Phase(0.6, 3)
        object Enraged : Phase(0.2, 1)

        override val entries: List<Phase>
            get() = listOf(Intro, Phase1, Phase2, Enraged)
    }

    var phase: Phase = Phase.Intro
        private set
    var state: BossState = BossState.ACTIVE
        private set
    var cycle: Int = 0
        private set
    private var attackCounter = 0

    override fun tick() {
        super.tick()

        //println("ticking: ${phase}, ${phase.next()}, ${cycle}, ${phase.entries}")

        if (numActiveTasksOf(ActionCategory.PRIMARY) == 0 && state != BossState.INACTIVE) {
            add(selectAttack())
        }
    }

    fun stun() {
        state = BossState.INACTIVE
        bossEntity.isInvulnerable = true
        bossEntity.setGravity(false)
        bossEntity.teleport(bossEntity.location.add(Vector(0.0, 6.0, 0.0)))
        endTasks(ActionCategory.PRIMARY, ActionCategory.SECONDARY, ActionCategory.PASSIVE)

        add(WitherAttacks.summonWitherMini(buildContext()))
        //for (player in boss.currentPlayers) player.sendMessage(Utils.text("The boss is stunned!", Utils.GRAY))
    }
    fun endStun() {
        state = BossState.ACTIVE
        bossEntity.isInvulnerable = false
        bossEntity.setGravity(true)
        bossEntity.teleport(bossEntity.location.add(Vector(0.0, -6.0, 0.0)))

        add() //TODO: corruption back
    }
    fun reachedStunThreshold(): Boolean {
        return state == BossState.STUNNED && (boss.hpPercent < (
                phase.threshold - ((cycle + 1) * (phase.threshold - phase.next().threshold) / phase.cycles)
            ) ||
                boss.hpPercent < phase.next().threshold)
    }

    fun startPhase(phase: Phase) {
        this.phase = phase
        val context = buildContext()
        cycle = 0
        when (phase) {
            Phase.Phase1 -> {
                add(WardenAttacks.centerPusher(context))
                add(WardenAttacks.repeatingSpawn(buildContext()))
                add(selectAttack())
            }
            Phase.Phase2 -> {
                add(WardenAttacks.summonWardenMini(buildContext()))
                add(WardenAttacks.centerPusher(context))
                add(selectAttack())
            }
            Phase.Enraged -> {
                bossEntity.isInvulnerable = false
                bossEntity.setAI(true)
                add(selectAttack())
            }
            Phase.Intro -> {}
        }

        CustomEffects.playSound(boss.getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 0.8F + 0.2F * (phase.ordinal - 1))
        bossEntity.location.getNearbyPlayers(6.0).forEach {
            it.velocity = it.location.subtract(bossEntity.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
    }

    private fun selectAttack(): BossAction {
        val ctx = buildContext()
        //println("picking $ctx")
        val selected = when (phase) {
            Phase.Phase1 -> {
                if (attackCounter >= 7) {
                    WardenAttacks.flameSquares(ctx)
                } else {
                    val random = Math.random()

                    val attackWeights = mapOf(
                        WardenAttacks::safeCircles to 0.65,
                        WardenAttacks::flameLasers to 1.0,
                        WardenAttacks::sonicBoom to 1.0,
                        WardenAttacks::flameSquares to (if (attackCounter > 3) 0.7 else 0.0)
                    )

                    val attack = attackWeights.random()
                    if (attack == WardenAttacks::mobWave) {summonedMobWave = true}
                    attack(ctx)
                }
            }
            Phase.Phase2 -> {
                if (!summonedMobWave) {
                    val random = Math.random()
                    when {
                        random < 0.35 -> WardenAttacks.flameLasers(ctx)
                        random < 0.70 -> WardenAttacks.sonicBoom(ctx)
                        else -> {summonedMobWave = true; WardenAttacks.mobWave(ctx)}
                    }
                } else {
                    val random = Math.random()
                    when {
                        random < 0.5 -> WardenAttacks.flameLasers(ctx)
                        else -> WardenAttacks.sonicBoom(ctx)
                    }
                }
            }
            Phase.Enraged -> {
                WardenAttacks.mobWave(ctx)
            }
            else -> {
                DelayAction(boss, 10)
            }
        }

        attackCounter++

        return selected
    }

    private fun buildContext(): AttackContext<Phase> {
        val scalingCycle = when (phase) {
            Phase.Phase1 -> 0
            Phase.Phase2 -> 1
            Phase.Enraged -> 2
            else -> 0
        }
        val ctx = AttackContext(phase, cycle, scalingCycle, 2, boss.difficulty, boss)
        return ctx
    }

}