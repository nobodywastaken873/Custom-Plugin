package me.newburyminer.customItems.bosses.definitions.warden

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.AttackContext
import me.newburyminer.customItems.bosses.AttackHelper
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.actions.DelayedAction
import me.newburyminer.customItems.bosses.actions.MultiAction
import me.newburyminer.customItems.bosses.actions.ParticleCylinderAttack
import me.newburyminer.customItems.bosses.actions.ParticleLineAttack
import me.newburyminer.customItems.bosses.actions.PlaneAttack
import me.newburyminer.customItems.bosses.actions.RepeatAction
import me.newburyminer.customItems.bosses.actions.RepeatingPlaneAttack
import me.newburyminer.customItems.bosses.actions.SummonMobsAction
import me.newburyminer.customItems.bosses.rendering.shapes.Circle
import me.newburyminer.customItems.bosses.rendering.shapes.NegativeShape
import me.newburyminer.customItems.bosses.rendering.shapes.Polygon
import me.newburyminer.customItems.bosses.rendering.shapes.ShapeLayer
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.bosses.WardenMinibossComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import me.newburyminer.customItems.mobprovider.mobs.BasicSkeleton
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenCreature
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenSkeleton
import me.newburyminer.customItems.mobprovider.mobs.bosses.warden.WardenZombie
import me.newburyminer.customItems.structures.EncounterStyle
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Warden
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

object WardenAttacks : AttackHelper {

    private val particleSettings = ParticleSettings(
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(102, 226, 232), 1.0F)), 5,
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(50, 117, 120), 1.0F))
    )
    private val floorSettings = ParticleSettings(
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(247, 2, 2), 3.0F)), 5,
        Particle.DUST.builder().data(DustOptions(Color.fromRGB(125, 1, 11), 3.0F))
    )

    fun flameSquares(ctx: AttackContext<WardenController.Phase>): BossAction {
        val circleCount = 4 - ctx.cycle
        val circles = getCorners(ctx.boss.getLowerCenter().toVector(), 8.5).take(circleCount).map {
            ShapeLayer(Polygon(getCorners(it, 3.0), ctx.boss.bottomY), ShapeLayer.Operation.SUBTRACT)
        }.toTypedArray()

        return MultiAction(ctx.boss, listOf(
            PlaneAttack(
                ctx.boss,
                NegativeShape(
                    ctx.boss.bottomY,
                    ShapeLayer(Polygon(getCorners(ctx.boss.getLowerCenter().toVector(), 15.0), ctx.boss.bottomY), ShapeLayer.Operation.ADD),
                    *circles
                ),
                ctx.boss.bottomY,
                linear(60 to 40, ctx),
                above = true,
                floorSettings,
                SoundSettings(Sound.BLOCK_BASALT_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_ANVIL_PLACE),
                HitEffects(
                    damage(linear(65.0 to 90.0, ctx), CustomDamageType.BURNING_NO_CD),
                    attribute(Attribute.MOVEMENT_SPEED, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, linear(40 to 80, ctx)),
                    attribute(Attribute.JUMP_STRENGTH, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, linear(40 to 80, ctx))
                )
            ),
            object : BossAction(ctx.boss) {
                override val category: ActionCategory = ActionCategory.SECONDARY
                override fun tick() {
                    if (timer == linear(60 to 40, ctx)) (ctx.boss as WardenInstance).stun()
                    timer++
                }
            }
        ))
    }

    fun safeCircles(ctx: AttackContext<WardenController.Phase>): BossAction {
        val attackProvider = {
            val circles = mutableListOf<ShapeLayer>()
            repeat(4) {
                val angle = Random.nextDouble() * 2 * Math.PI
                val radius = Random.nextDouble() * 12.0 + 3.0
                val offset = Vector(radius * cos(angle), 0.0, radius * sin(angle))
                circles.add(ShapeLayer(Circle(ctx.boss.bottomY, ctx.boss.getLowerCenter().toVector().add(offset),
                    linear(3.0 to 2.0, ctx)), ShapeLayer.Operation.SUBTRACT))
            }
            PlaneAttack(
                ctx.boss,
                NegativeShape(
                    ctx.boss.bottomY,
                    ShapeLayer(Polygon(getCorners(ctx.boss.getLowerCenter().toVector(), 16.0), ctx.boss.bottomY), ShapeLayer.Operation.ADD),
                    *circles.toTypedArray(),
                ),
                ctx.boss.bottomY,
                linear(80 to 50, ctx),
                above = true,
                floorSettings,
                SoundSettings(Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_LAVA_EXTINGUISH),
                HitEffects(
                    damage(linear(45.0 to 60.0, ctx), CustomDamageType.BURNING_NO_CD),
                    CustomKnockbackApply(0.0, 2.0, 0.0)
                )
            )
        }

        return RepeatAction(ctx.boss, attackProvider, linear(3 to 4, ctx), linear(18 to 12, ctx))
    }

    fun flameLasers(ctx: AttackContext<WardenController.Phase>): BossAction {
        val attackProvider = {
            MultiAction(
                ctx.boss,
                getCorners(ctx.boss.getLowerCenter().add(0.0, 2.0, 0.0).toVector(), 8.5).map {
                    ParticleLineAttack(
                        ctx.boss,
                        it.toLocation(ctx.boss.boss.world),
                        it.toLocation(ctx.boss.boss.world).add(it.toLocation(ctx.boss.boss.world).getNearbyPlayers(30.0)
                            .filter { player -> player in ctx.boss.currentPlayers }
                            .minBy { player -> it.clone().subtract(player.location.toVector()).length() }!!
                            .getUpperCenter().toVector()
                            .subtract(it)
                            .normalize().multiply(30.0).toLocation(ctx.boss.boss.world)),
                        linear(40 to 20, ctx),
                        0,
                        particleSettings,
                        SoundSettings(Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT),
                        HitEffects(damage(linear(40.0 to 65.0, ctx), CustomDamageType.PROJECTILE_NO_CD))
                    )
                }
            )
        }

        return RepeatAction(ctx.boss, attackProvider, linear(4 to 9, ctx), linear(15 to 8, ctx))
    }

    fun sonicBoom(ctx: AttackContext<WardenController.Phase>): BossAction {
        val attackProvider = {
            val center = ctx.boss.getCenter().add(0.0, 1.0, 0.0).toVector()
            MultiAction(
                ctx.boss,
                ctx.boss.currentPlayers.map { player ->
                    ParticleCylinderAttack(
                        ctx.boss,
                        ctx.boss.getCenter().add(0.0, 1.0, 0.0),
                        center.clone().add(player.getUpperCenter().subtract(center).toVector().normalize().multiply(30.0)).toLocation(ctx.boss.boss.world),
                        linear(1.4 to 1.8, ctx),
                        linear(60 to 30, ctx),
                        0,
                        particleSettings,
                        SoundSettings(Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_WARDEN_SONIC_BOOM),
                        HitEffects(damage(linear(10.0 to 18.0, ctx), CustomDamageType.ALL_BYPASS_NO_CD),
                            CustomKnockbackApply(1.5, 1.0, 1.5))
                    )
                }
            )
        }

        return RepeatAction(ctx.boss, attackProvider, linear(3 to 4, ctx), linear(15 to 8, ctx))
    }

    fun centerPusher(ctx: AttackContext<WardenController.Phase>): BossAction {
        return RepeatingPlaneAttack(
            ctx.boss,
            Circle(ctx.boss.bottomY, ctx.boss.getLowerCenter().toVector(), 4.5),
            ctx.boss.bottomY,
            0,
            100000,
            10,
            true,
            particleSettings,
            SoundSettings(Sound.BLOCK_NOTE_BLOCK_HARP, 0.0F, 0.0F, 1, Sound.BLOCK_NOTE_BLOCK_HARP, volume = 0.0F),
            HitEffects(
                damage(linear(30.0 to 40.0, ctx), CustomDamageType.BURNING_NO_CD),
                CustomKnockbackApply(1.5, 1.0, 1.5)
            ),
            ActionCategory.SECONDARY,
            cellsize = 0.1
        )
    }

    private val spawningProvider = MobProvider(EncounterStyle.GRUNT_ONLY,
        WardenZombie,
        WardenSkeleton,
        MobEntry(WardenCreature, 0.5)
    )
    
    fun mobWave(ctx: AttackContext<WardenController.Phase>): BossAction {
        val delay = when (ctx.phase) {
            WardenController.Phase.Enraged -> 600
            else -> {10}
        }
        return RepeatAction(ctx.boss,
            {
                SummonMobsAction(ctx.boss,
                    spawningProvider,
                    (linear(2 to 4, ctx) * ctx.boss.playerCount.toDouble().pow(0.7)).roundToInt(),
                    linear(60 to 40, ctx),
                    particleSettings,
                    ctx,
                    SoundSettings(Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 0.5F, 0.5F, 1, Sound.ITEM_TRIDENT_RETURN)
                )
            }, linear(3 to 3, ctx),
            separation = delay
        )
    }

    fun summonWardenMini(ctx: AttackContext<WardenController.Phase>): BossAction {
        return object : BossAction(ctx.boss) {
            override val category: ActionCategory = ActionCategory.SECONDARY
            private val timeline = ActionTimeline()
            override fun start() {
                val possLocs = mutableListOf<Location>()
                for ((first, second) in arrayOf(Pair(8.0, 8.0), Pair(8.0, -8.0), Pair(-8.0, 8.0), Pair(-8.0, -8.0))) {
                    val loc = ctx.boss.getLowerCenter().add(first, 1.5, second)
                    if (loc.block.type == Material.SOUL_FIRE) possLocs.add(loc)
                    //Bukkit.getLogger().info(loc.toString())
                }
                val loc = possLocs.random()

                timeline.at(40) {
                    for (x in -1..1) for (z in -1..1) {
                        loc.clone().add(x.toDouble(), 0.0, z.toDouble()).block.type = Material.AIR
                    }
                    loc.add(0.5, 0.0, 0.5)
                    CustomEffects.playSound(loc, Sound.ENTITY_WARDEN_EMERGE, 3.0F, 1.0F)
                    CustomEffects.particleCloud(
                        Particle.SONIC_BOOM.builder(),
                        loc.clone().add(0.0, 1.5, 0.0),
                        40,
                        2.0,
                        0.0
                    )

                    val warden = CustomItems.bossWorld.spawn(loc, Warden::class.java) {
                        it.getAttribute(Attribute.MAX_HEALTH)?.baseValue *= linear(0.5 to 0.8, ctx, false)
                        it.health *= linear(0.5 to 0.8, ctx, false)
                        it.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue *= linear(0.7 to 1.0, ctx, false)
                        it.getAttribute(Attribute.ATTACK_DAMAGE)?.baseValue *= linear(0.85 to 1.4, ctx, false)
                    }
                    EntityWrapperManager.getWrapperorNew(warden)
                        .addComponent(WardenMinibossComponent(ctx.boss as WardenInstance))
                    boss.addEntity(warden)
                    finish()
                }
            }

            override fun tick() {
                timeline.tick()
            }
        }
    }

}