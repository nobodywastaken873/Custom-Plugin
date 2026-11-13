package me.newburyminer.customItems.bosses.definitions.wither

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.angle
import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.AttackContext
import me.newburyminer.customItems.bosses.AttackHelper
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.Collision
import me.newburyminer.customItems.bosses.actions.DelayedAction
import me.newburyminer.customItems.bosses.actions.FixedRepeatAction
import me.newburyminer.customItems.bosses.actions.MultiAction
import me.newburyminer.customItems.bosses.actions.MultiParticleCylinderAttack
import me.newburyminer.customItems.bosses.actions.ParticleCylinderAttack
import me.newburyminer.customItems.bosses.actions.ParticleLineAttack
import me.newburyminer.customItems.bosses.actions.PlaneAttack
import me.newburyminer.customItems.bosses.actions.RepeatAction
import me.newburyminer.customItems.bosses.actions.RepeatingPlaneAttack
import me.newburyminer.customItems.bosses.actions.SummonMobsAction
import me.newburyminer.customItems.bosses.definitions.warden.WardenController
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.bosses.rendering.RenderManager
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.bosses.rendering.combinator.CylinderCombinator
import me.newburyminer.customItems.bosses.rendering.shapes.Circle
import me.newburyminer.customItems.bosses.rendering.shapes.NegativeShape
import me.newburyminer.customItems.bosses.rendering.shapes.Polygon
import me.newburyminer.customItems.bosses.rendering.shapes.ShapeLayer
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.bosses.WardenMinibossComponent
import me.newburyminer.customItems.entity.components.bosses.WitherBlobComponent
import me.newburyminer.customItems.entity.components.bosses.WitherBossComponent
import me.newburyminer.customItems.entity.components.bosses.WitherMinibossComponent
import me.newburyminer.customItems.entity.components.projectiles.EffectAuraComponent
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.velocity.DelayedStartVelocity
import me.newburyminer.customItems.entity.velocity.StoppedStartVelocity
import me.newburyminer.customItems.entity.velocity.VelocityProvider
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicSkeleton
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenCreature
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenZombie
import me.newburyminer.customItems.mobprovider.mobs.bosses.wither.CleansingBlob
import me.newburyminer.customItems.mobprovider.mobs.bosses.wither.WitheredBrute
import me.newburyminer.customItems.structures.EncounterStyle
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Marker
import org.bukkit.entity.Warden
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

object WitherAttacks : AttackHelper {

    private val particleSettings = ParticleSettings(
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(245, 232, 201), 1.0F)), 5,
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(87, 57, 20), 1.0F)), preConcentration = 1.25, concentration = 1.25
    )
    private val floorSettings = ParticleSettings(
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(247, 2, 2), 3.0F)), 5,
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(125, 1, 11), 3.0F)), preConcentration = 1.0, concentration = 2.0
    )
    private val cylinderSettings = ParticleSettings(
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(245, 232, 201), 1.0F)), 5,
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(87, 57, 20), 1.0F)), preConcentration = 0.0, concentration = 0.6
    )

    fun gridLasers(ctx: AttackContext<WitherController.Phase>): BossAction {

        val attackProvider = {
            val laserCount = linear(20 to 34, ctx)
            val randomX = Math.random()
            val randomZ = Math.random()
            val linePairs = (0..laserCount).map { index ->
                // going from -19.5 to 19.5 so -19.5 + i * 39.0/lasercount
                val xOffset = -19.5 + index * 39.0 / laserCount + randomX
                val start = ctx.boss.getCenter().add(Vector(xOffset, 1.0, -20.0))
                val end = start.clone().add(Vector(0.0, 0.0, 40.0))
                return@map start to end
            } + (0..laserCount).map { index ->
                // going from -19.5 to 19.5 so -19.5 + i * 39.0/lasercount
                val zOffset = -19.5 + index * 39.0 / laserCount + randomZ
                val start = ctx.boss.getCenter().add(Vector(-20.0, 1.0, zOffset))
                val end = start.clone().add(Vector(40.0, 0.0, 0.0))
                return@map start to end
            }

            MultiParticleCylinderAttack(
                ctx.boss,
                linePairs.toMap().keys.toList(),
                linePairs.toMap().values.toList(),
                linear(0.1 to 0.15, ctx),
                linear(40 to 30, ctx),
                cylinderSettings,
                SoundSettings(Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT),
                HitEffects(damage(linear(10.0 to 18.0, ctx), CustomDamageType.ALL_BYPASS_NO_CD),
                    CustomKnockbackApply(1.5 * Math.random(), 1.0, 1.5 * Math.random()))
            )
        }

        return RepeatAction(ctx.boss, attackProvider, linear(3 to 4, ctx), 0)
    }

    fun decayCircles(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.PRIMARY
            private val timeline = ActionTimeline()
            override fun start() {
                boss.currentPlayers.forEach {
                    player -> player.playSound(boss.boss, Sound.ENTITY_WITCH_THROW, 1.0F, 0.7F)
                }

                for (player in ctx.boss.currentPlayers) {
                    player.world.spawn(player.location, Marker::class.java) {
                        val newWrapper = EntityWrapperManager.getWrapperorNew(it)
                        newWrapper.addComponent(EffectAuraComponent(
                            linear(4.0 to 6.0, ctx) * 1.0 / ctx.boss.playerCount.toDouble().pow(1.0 / 3.0),
                            2.0,
                            linear(200 to 300, ctx),
                            linear(60 to 80, ctx),
                            player,
                            linear(40 to 60, ctx),
                            HitEffects(damage(linear(12.0 to 18.0, ctx), CustomDamageType.BURNING_NO_CD)),
                            5,
                            ParticleTheme.BLACKSTONE,
                            ctx.boss.boss
                        ))
                    }
                }

                timeline.at(140) {
                    finish()
                }
            }

            override fun tick() {
                timeline.tick()
            }
        }
    }

    fun laserSpray(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.PRIMARY
            private val timeline = ActionTimeline()
            override fun start() {
                boss.currentPlayers.forEach {
                    player -> player.playSound(boss.boss, Sound.ENTITY_WITCH_THROW, 1.0F, 0.7F)
                }

                val delays = listOf(20, 40, 60, 80, 100, 120).take(linear(3 to 6, ctx))

                delays.forEach { delay ->
                    timeline.at(delay) {
                        boss.currentPlayers.forEach {
                            player -> player.playSound(boss.boss, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, 1.3F)
                        }

                        boss.currentPlayers.forEach {player -> repeat(2) {
                            player.world.spawn(boss.getCenter().add(0.0, 3.0, 0.0), Marker::class.java) {
                                val wrapper = EntityWrapperManager.getWrapperorNew(it)
                                wrapper.addComponent(MagicMissileComponent(
                                    0.4,
                                    StoppedStartVelocity(1.2, 0.01, HomingSystem.Type.NONE, 20, 3.0),
                                    Vector(0 + Math.random() * 2, 1 + Math.random() * 2, 0 + Math.random() * 2),
                                    HitEffects(damage(linear(30.0 to 50.0, ctx), CustomDamageType.MAGIC_NO_CD)),
                                    ParticleTheme.BLACKSTONE,
                                    boss.boss,
                                    player
                                ))
                            }
                        } }
                    }
                }

                timeline.at(delays.size * 20 + 60) {
                    finish()
                }
            }

            override fun tick() {
                timeline.tick()
            }
        }
    }

    private val spawningProvider = MobProvider(EncounterStyle.GRUNT_ONLY,
        WardenZombie,
        WardenSkeleton,
        MobEntry(WardenCreature, 0.5)
    )
    fun mobCircles(ctx: AttackContext<WitherController.Phase>): BossAction {
        return MultiAction(ctx.boss,
            listOf(
                RepeatAction(ctx.boss, {
                    SummonMobsAction(ctx.boss,
                        spawningProvider,
                        (linear(2.0 to 3.0, ctx) * ctx.boss.playerCount.toDouble().pow(0.7)).roundToInt(),
                        linear(120 to 100, ctx),
                        particleSettings,
                        ctx,
                        SoundSettings(Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 0.5F, 0.5F, 1, Sound.ITEM_TRIDENT_RETURN)
                    )
                }, linear(3 to 3, ctx)),
                RepeatAction(ctx.boss, {
                    val circles = mutableListOf<ShapeLayer>()
                    for (i in 0..3) {
                        val angle = Random.nextDouble() * Math.PI / 2.0 + (Math.PI / 2.0 * i)
                        val radius = Random.nextDouble() * 12.0 + 3.0
                        val offset = Vector(radius * cos(angle), 0.0, radius * sin(angle))
                        circles.add(ShapeLayer(Circle(ctx.boss.bottomY, ctx.boss.getLowerCenter().toVector().add(offset),
                            linear(3.0 to 2.0, ctx)), ShapeLayer.Operation.ADD))
                    }
                    PlaneAttack(
                        ctx.boss,
                        NegativeShape(
                            ctx.boss.bottomY,
                            *circles.toTypedArray(),
                        ),
                        ctx.boss.bottomY,
                        linear(80 to 50, ctx),
                        above = true,
                        floorSettings,
                        SoundSettings(Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_LAVA_EXTINGUISH),
                        HitEffects(
                            damage(linear(60.0 to 80.0, ctx), CustomDamageType.BURNING_NO_CD),
                            CustomKnockbackApply(0.0, 2.0, 0.0)
                        )
                    )
                }, linear(4 to 4, ctx), linear(18 to 12, ctx))
            )
        )
    }

    fun sectorSweep(ctx: AttackContext<WitherController.Phase>): BossAction {
        val attackProvider = {

            val angle = Math.PI * 2 * Math.random()
            val endAngle = angle + Math.PI / 3
            val y = ctx.boss.bottomY
            val startPoint = ctx.boss.getCenter().add(Vector(22.0*cos(angle), 0.0, 22.0*sin(angle))).toVector()
            val endPoint = ctx.boss.getCenter().add(Vector(22.0*cos(endAngle), 0.0, 22.0*sin(endAngle))).toVector()

            val sector = NegativeShape(y, ShapeLayer(
                Circle(y, ctx.boss.getCenter().toVector(), 5.0), ShapeLayer.Operation.ADD
            ), ShapeLayer(
                Polygon(listOf(startPoint, endPoint, ctx.boss.getCenter().toVector()), y), ShapeLayer.Operation.SUBTRACT
            ))
            PlaneAttack(
                ctx.boss,
                sector,
                ctx.boss.bottomY,
                linear(40 to 30, ctx),
                above = true,
                floorSettings,
                SoundSettings(Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_PLAYER_ATTACK_SWEEP),
                HitEffects(
                    damage(linear(45.0 to 60.0, ctx), CustomDamageType.BURNING_NO_CD),
                    CustomKnockbackApply(0.0, 1.0, 0.0)
                )
            )
        }

        return RepeatAction(ctx.boss, attackProvider, linear(3 to 4, ctx), linear(18 to 12, ctx))
    }

    fun rotatingBeam(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.SECONDARY
            private val timeline = ActionTimeline()
            private val renderManager = RenderManager()
            override fun start() {
                boss.currentPlayers.forEach {
                        player -> player.playSound(boss.boss, Sound.ENTITY_WITCH_THROW, 1.0F, 0.7F)
                }


                val cylinders = computeOffsets().map { offset ->
                    val start = boss.getCenter().toVector()
                    val end = boss.getCenter().toVector().add(offset)
                    CylinderCombinator(
                        Transform(start, Transform.lookRotation(end.clone().subtract(start))),
                        0.6,
                        start.clone().subtract(end).length(),
                        Material.RED_STAINED_GLASS,
                        Particle.DUST.builder().color(235, 52, 52),
                        0.25,
                        boss.boss.world
                    )
                }
                cylinders.forEach {
                    it.spawn(boss.boss.world)
                    renderManager.add(it)
                }

                val rotationSpeed = linear(0.02 to 0.04, ctx).toFloat()
                var currentRotation = 0.0F
                timeline.repeat(20, 100000) {
                    tickDamage(currentRotation)
                    currentRotation += rotationSpeed
                    tickCylinders(cylinders, currentRotation)
                }


            }

            fun computeOffsets(offset: Float = 0.0F): List<Vector> {
                val angles = when (ctx.phase) {
                    WitherController.Phase.Phase2 -> listOf(0.0 + offset, Math.PI / 2 + offset, Math.PI + offset, 3 * Math.PI / 2 + offset)
                    else -> listOf(0.0 + offset, Math.PI + offset)
                }
                return angles.map { angle ->
                    Vector(22.0 * cos(angle), 0.0, 22.0 * sin(angle))
                }
            }

            fun tickCylinders(cylinders: List<CylinderCombinator>, angle: Float) {
                val offsets = computeOffsets(angle)
                cylinders.forEachIndexed { index, combinator ->
                    combinator.transform.setRotation(offsets[index], Vector(0, 1, 0))
                }
            }

            fun tickDamage(currentRotation: Float) {
                val effects = HitEffects(
                    damage(linear(35.0 to 60.0, ctx), CustomDamageType.MELEE_NO_CD),
                    CustomKnockbackApply(0.0, 1.0, 0.0)
                )
                val offsets = computeOffsets(currentRotation)
                offsets.forEach {
                    val start = boss.getCenter()
                    val end = boss.getCenter().add(it)
                    for (player in boss.currentPlayers) {
                        val box = player.boundingBox
                        if (Collision.cylinderIntersects(start, end, 0.6, box))
                            effects.apply(player, boss.boss)
                    }
                }

            }

            override fun cancel() {
                super.cancel()
                renderManager.clear()
            }
            override fun tick() {
                timeline.tick()
                renderManager.tick()
            }
        }
    }

    fun corruptionSpread(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.SECONDARY
            private val timeline = ActionTimeline()

            val circle = Circle(ctx.boss.bottomY, ctx.boss.getLowerCenter().toVector(), 19.0)
            val attack = RepeatingPlaneAttack(
                ctx.boss,
                NegativeShape(
                    ctx.boss.bottomY,
                    ShapeLayer(Polygon(getCorners(ctx.boss.getLowerCenter().toVector(), 20.0), ctx.boss.bottomY), ShapeLayer.Operation.ADD),
                    ShapeLayer(circle, ShapeLayer.Operation.SUBTRACT),
                ),
                ctx.boss.bottomY,
                linear(80 to 50, ctx),
                100000,
                5,
                above = true,
                floorSettings,
                SoundSettings(Sound.INTENTIONALLY_EMPTY, 0.5F, 1.5F, 10, Sound.INTENTIONALLY_EMPTY),
                HitEffects(
                    damage(linear(25.0 to 40.0, ctx), CustomDamageType.BURNING_NO_CD),
                    CustomKnockbackApply(0.0, 0.3, 0.0)
                )
            )
            var corruptionAmount = 0.0
            var corruptionRate = 0.0
            override fun start() {
                 updateCorruptionRate(ctx.phase, ctx.difficulty)

                attack.start()
                timeline.repeat(20, 100000) {
                    corruptionAmount += corruptionRate
                    if (corruptionAmount > 1.0) {
                        boss.currentPlayers.forEach { it.damage(1000.0, boss.boss) }
                    }
                    circle.radius = 19.0 * (1.0 - corruptionAmount)
                }

                var corruptionCleanserExists = false
                timeline.every(20, 100000, 10) {
                    if (corruptionCleanserExists && !ctx.boss.anyHasComponent(WitherBlobComponent::class)) {
                        corruptionAmount -= 0.2
                        corruptionCleanserExists = false
                    } else if (!corruptionCleanserExists && ctx.boss.anyHasComponent(WitherBlobComponent::class)) {
                        corruptionCleanserExists = true
                    }
                    updateCorruptionRate((boss.actionController as WitherController).phase, boss.difficulty)
                }
            }

            fun updateCorruptionRate(phase: WitherController.Phase, difficulty: BossDifficulty) {
                corruptionRate = when (phase) {
                    WitherController.Phase.Phase1 -> 1.0
                    WitherController.Phase.Phase2 -> 1.5
                    WitherController.Phase.Enraged -> 2.5
                    else -> 1.0
                } * when (difficulty) {
                    BossDifficulty.EASY -> 1.0
                    BossDifficulty.HARD -> 1.5
                } * 0.01 / 20.0
            }

            override fun tick() {
                attack.tick()
                timeline.tick()
            }

            override fun cancel() {
                super.cancel()
                attack.cancel()
            }
        }
    }

    fun summonBlob(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.PRIMARY
            private val timeline = ActionTimeline()
            override fun start() {
                val possLocs = mutableListOf<Location>()
                for ((first, second) in arrayOf(Pair(8.0, 8.0), Pair(8.0, -8.0), Pair(-8.0, 8.0), Pair(-8.0, -8.0))) {
                    val loc = ctx.boss.getLowerCenter().add(first, 0.0, second)
                    possLocs.add(loc)
                }
                val loc = possLocs.random()

                CustomEffects.playSound(loc, Sound.ENTITY_WARDEN_EMERGE, 3.0F, 1.0F)
                CustomEffects.particleCloud(
                    Particle.SOUL.builder(),
                    loc.clone().add(0.0, 1.5, 0.0),
                    40,
                    2.0,
                    0.0
                )

                val factor = when (ctx.difficulty) {
                    BossDifficulty.EASY -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
                    BossDifficulty.HARD -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
                } * 30.0

                val context = MobContext(factor, loc)
                val entity = CleansingBlob.build(context).createEntity(context)
                boss.addEntity(entity)

                finish()
            }

            override fun tick() {
                timeline.tick()
            }
        }
    }

    fun summonMiniboss(ctx: AttackContext<WitherController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.SECONDARY
            private val timeline = ActionTimeline()
            override fun start() {
                val loc = boss.getCenter()

                timeline.at(20) {
                    CustomEffects.playSound(loc, Sound.ENTITY_WARDEN_EMERGE, 3.0F, 0.6F)
                    CustomEffects.particleCloud(
                        Particle.SOUL.builder(),
                        loc.clone().add(0.0, 1.5, 0.0),
                        80,
                        2.0,
                        0.0
                    )

                    val factor = when (ctx.difficulty) {
                        BossDifficulty.EASY -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
                        BossDifficulty.HARD -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
                    } * 30.0

                    val context = MobContext(factor, loc)
                    val entity = WitheredBrute.build(context).createEntity(context)
                    EntityWrapperManager.getWrapperorNew(entity).addComponent(WitherMinibossComponent(ctx.boss as WitherInstance, linear(1000.0 to 2000.0, ctx)))
                    boss.addEntity(entity)

                    finish()
                }
            }

            override fun tick() {
                timeline.tick()
            }
        }
    }

}