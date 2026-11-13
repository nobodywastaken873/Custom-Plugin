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

        add(WitherAttacks.summonMiniboss(buildContext()))
        add(WitherAttacks.rotatingBeam(buildContext()))
        //for (player in boss.currentPlayers) player.sendMessage(Utils.text("The boss is stunned!", Utils.GRAY))
    }
    fun endStun() {
        state = BossState.ACTIVE
        bossEntity.isInvulnerable = false
        bossEntity.setGravity(true)
        bossEntity.teleport(bossEntity.location.add(Vector(0.0, -6.0, 0.0)))

        cancelAll()
        add(WitherAttacks.corruptionSpread(buildContext()))
    }
    fun checkStun() {
        if (boss.hpPercent < phase.next().threshold && !phase.isLast()) {
            startPhase(phase.next())
        }
    }

    fun startPhase(phase: Phase) {
        this.phase = phase
        when (phase) {
            Phase.Phase1 -> {
                cancelAll()
                add(WitherAttacks.corruptionSpread(buildContext()))
                add(selectAttack())
            }
            Phase.Phase2 -> {
                cancelAll()
                stun()
            }
            Phase.Enraged -> {}
            Phase.Intro -> {}
        }

        CustomEffects.playSound(boss.getCenter(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 0.8F + 0.2F * (phase.ordinal - 1))
    }

    private fun selectAttack(): BossAction {
        val ctx = buildContext()
        //println("picking $ctx")
        val selected = when (phase) {
            Phase.Phase1 -> {
                if (attackCounter >= 4) {
                    attackCounter = 0
                    WitherAttacks.summonBlob(ctx)
                } else {
                    val attackWeights = mapOf(
                        WitherAttacks::gridLasers to 1.0,
                        WitherAttacks::decayCircles to 0.8,
                        WitherAttacks::laserSpray to 1.2,
                        WitherAttacks::mobCircles to 0.7,
                        WitherAttacks::sectorSweep to 1.4,
                    )

                    val attack = attackWeights.random()
                    attack(ctx)
                }
            }
            Phase.Phase2 -> {
                if (attackCounter >= 4) {
                    attackCounter = 0
                    WitherAttacks.summonBlob(ctx)
                } else {
                    val attackWeights = mapOf(
                        WitherAttacks::gridLasers to 1.0,
                        WitherAttacks::decayCircles to 0.8,
                        WitherAttacks::laserSpray to 1.2,
                        WitherAttacks::mobCircles to 0.7,
                        WitherAttacks::sectorSweep to 1.4,
                    )

                    val attack = attackWeights.random()
                    attack(ctx)
                }
            }
            Phase.Enraged -> {
                if (attackCounter >= 4) {
                    attackCounter = 0
                    WitherAttacks.summonBlob(ctx)
                } else {
                    val attackWeights = mapOf(
                        WitherAttacks::gridLasers to 1.0,
                        WitherAttacks::decayCircles to 0.8,
                        WitherAttacks::laserSpray to 1.2,
                        WitherAttacks::sectorSweep to 1.4,
                    )

                    val attack = attackWeights.random()
                    attack(ctx)
                }
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
        val ctx = AttackContext(phase, scalingCycle, scalingCycle, 2, boss.difficulty, boss)
        return ctx
    }

}