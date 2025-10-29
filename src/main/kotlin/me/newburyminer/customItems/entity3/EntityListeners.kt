package me.newburyminer.customItems.entity3

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.getDifficultyIndex
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.RandomSelector
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.world.GenericGameEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class EntityListeners: Listener, Runnable {

    //fix: possibly figure out how to make breaching better along with preignition, figure out how to only do it when going into player
    //figure out health and armor values
    /*
    skelly todo:
    add explosive arrows for explosive skeleton
    add occasional explosive arrows for hyper explosive + explosion on impact
    occasional sniper shot from far away, maybe replace all shots/every other shot
    
     */
    @EventHandler fun onPotionChange(e: EntityPotionEffectEvent) {
        if (e.entity.world != CustomItems.aridWorld) return
        if (e.entity is Player) return
        if (e.cause != EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD && e.cause != EntityPotionEffectEvent.Cause.POTION_SPLASH) return
        e.isCancelled = true
    }

    @EventHandler fun onEntityDeath(e: EntityDeathEvent) {
        if (e.entity.world != CustomItems.aridWorld) return
        broodmotherDeath(e)
        caveBroodmotherDeath(e)
        //lavaCubeDeath(e)
    }
    private fun broodmotherDeath(e: EntityDeathEvent) {
        if (e.entity.type != EntityType.SPIDER) return
        if (e.entity.getTag<Int>("id") != CustomEntity.BROODMOTHER_SPIDER.id) return
        for (i in 0..3) {
            val spider = e.entity.location.world.spawn(e.entity.location, Spider::class.java, CreatureSpawnEvent.SpawnReason.DUPLICATION)
            spider.setTag("id", CustomEntity.CAVE_BROODMOTHER_SPIDER.id)
            spider.getAttribute(Attribute.SCALE)?.baseValue = 0.5
        }
    }
    private fun caveBroodmotherDeath(e: EntityDeathEvent) {
        if (e.entity.type != EntityType.SPIDER) return
        if (e.entity.getTag<Int>("id") != CustomEntity.CAVE_BROODMOTHER_SPIDER.id) return
        for (i in 0..3) {
            val spider = e.entity.location.world.spawn(e.entity.location, Spider::class.java, CreatureSpawnEvent.SpawnReason.DUPLICATION)
            spider.setTag("id", CustomEntity.BROODMOTHER_SPAWN.id)
            spider.getAttribute(Attribute.SCALE)?.baseValue = 0.25
        }
    }
    /*private fun lavaCubeDeath(e: EntityDeathEvent) {
        if (e.entity.type != EntityType.MAGMA_CUBE) return
        if (e.entity.getTag<Int>("id") != CustomEntity.LAVA_CUBE.id) return
        if (e.entity.location.block.type != Material.AIR) return
        e.entity.location.block.type = Material.LAVA
    }*/

    @EventHandler fun onArrowLand(e: ProjectileHitEvent) {
        if (e.entity.world != CustomItems.aridWorld) return
        explosiveSkeletonArrowLand(e)
        homingArrowHit(e)
        //elytraArrowHit(e)
        machineGunArrowHit(e)
        shieldBreakerArrowHit(e)
    }
    private fun explosiveSkeletonArrowLand(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.EXPLOSIVE_SKELETON_ARROW.id) return
        if (e.hitBlock == null && e.hitEntity !is Player) return
        val difficulty = e.entity.getTag<Double>("difficulty")!!
        e.entity.world.createExplosion(e.entity, e.entity.location, (difficulty.pow(0.45)).toFloat(), false)
        e.entity.remove()
    }
    private fun homingArrowHit(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.HOMING_SKELETON_ARROW.id) return
        if (e.hitEntity !is Player) return
        e.entity.remove()
    }
    /*private fun elytraArrowHit(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_SKELETON_ARROW.id) return
        if (e.hitEntity !is Player) return
        e.entity.remove()
    }*/
    private fun machineGunArrowHit(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.MACHINE_GUN_SKELETON_ARROW.id) return
        e.entity.remove()
    }
    private fun shieldBreakerArrowHit(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.SHIELD_BREAKER_ARROW.id) return
        if (e.hitEntity == null) {
            e.entity.remove()
            return
        }
        if (e.hitEntity!! is Player) {
            (e.hitEntity!! as Player).setCooldown(Material.SHIELD, 40)
            CustomEffects.playSound(e.hitEntity!!.location, Sound.ITEM_SHIELD_BREAK, 1F, 1F)
        }
        e.entity.remove()
    }

    @EventHandler fun onFuseIgnite(e: GenericGameEvent) {
        if (e.event != GameEvent.PRIME_FUSE) return
        if (e.entity == null) return
        if (e.entity!!.world != CustomItems.aridWorld) return
        if (e.entity!!.type != EntityType.CREEPER) return
        val mob = e.entity as Creeper
        if (mob.getTag<Int>("id") == CustomEntity.HOPPING_CREEPER.id) {
            val difficulty = mob.getTag<Double>("difficulty")
            Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                val toPlayer = mob.target?.location?.clone()?.subtract(mob.location) ?: Location(mob.world, 0.0, 0.0, 0.0)
                mob.velocity = toPlayer.toVector().multiply(0.21).add(Vector(0.0, 0.6, 0.0)).add(mob.target?.velocity ?: Vector(0, 0, 0))
            }, 10)
        } else if (mob.getTag<Int>("id") == CustomEntity.SHIELD_BREAKER_CREEPER.id) {
            Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                if (mob.target is Player) {
                    (mob.target as Player).setCooldown(Material.SHIELD, 30)
                    CustomEffects.playSound(mob.target!!.location, Sound.ITEM_SHIELD_BREAK, 1F, 1F)
                }
            }, 10)
        }
    }
    @EventHandler fun onEntityHit(e: EntityKnockbackByEntityEvent) {
        goatHit(e)
        launchingSlimeHit(e)
        launchingCubeHit(e)
    }
    private fun goatHit(e: EntityKnockbackByEntityEvent) {
        if (e.hitBy.type != EntityType.GOAT) return
        e.knockback = e.knockback.add(Vector(0.0, 5.0, 0.0))
        e.knockback = e.knockback.multiply(10)
    }
    private fun launchingSlimeHit(e: EntityKnockbackByEntityEvent) {
        if (e.hitBy.type != EntityType.SLIME) return
        if (e.hitBy.getTag<Int>("id") != CustomEntity.LAUNCHING_SLIME.id) return
        e.knockback = e.knockback.add(Vector(0.0, 0.4, 0.0))
        e.knockback = e.knockback.multiply(1.5)
    }
    private fun launchingCubeHit(e: EntityKnockbackByEntityEvent) {
        if (e.hitBy.type != EntityType.MAGMA_CUBE) return
        if (e.hitBy.getTag<Int>("id") != CustomEntity.LAUNCHING_CUBE.id) return
        e.knockback = e.knockback.add(Vector(0.0, 0.4, 0.0))
        e.knockback = e.knockback.multiply(1.5)
    }

    @EventHandler fun onArrowShoot(e: ProjectileLaunchEvent) {
        if (e.entity.world != CustomItems.aridWorld) return
        homingSkeletonShoot(e)
        //elytraBreakerSkeletonShoot(e)
        //sniperSkeletonShoot(e)
        explosiveSkeletonShoot(e)
        machineGunSkeletonShoot(e)
        swarmerSkeletonShoot(e)
        shieldBreakerSkeletonShoot(e)
        witchShoot(e)
        sniperWitchShoot(e)
        bioweaponWitch(e)
    }
    private fun homingSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.HOMING_SKELETON.id) return
        e.entity.setTag("id", CustomEntity.HOMING_SKELETON_ARROW.id)
        var closest: Player? = null
        for (entity in e.entity.getNearbyEntities(100.0, 100.0, 100.0)) {
            if (entity !is Player) continue
            if (entity.location.subtract(e.entity.location).length() > (closest?.location?.subtract(e.entity.location)?.length() ?: 1000.0)) continue
            closest = entity
        }
        if (closest == null) return
        e.entity.setTag("target", closest.uniqueId)
    }
    /*private fun elytraBreakerSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_SKELETON.id) return
        e.entity.setTag("id", CustomEntity.ELYTRA_BREAKER_SKELETON_ARROW.id)
        e.entity.setTag("difficulty", (e.entity.shooter!! as Skeleton).getTag<Double>("difficulty"))
        var closest: Player? = null
        for (entity in e.entity.getNearbyEntities(100.0, 100.0, 100.0)) {
            if (entity !is Player) continue
            if (entity.location.subtract(e.entity.location).length() < (closest?.location?.subtract(e.entity.location)?.length() ?: 1000.0)
                || entity.isGliding) {
                closest = entity
            }
        }
        if (closest == null) return
        e.entity.setTag("target", closest.uniqueId)
    }*/
    /*private fun sniperSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.SNIPER_SKELETON.id) return
        val skeleton = e.entity.shooter as Skeleton
        e.entity.setTag("id", CustomEntity.SNIPER_SKELETON_ARROW.id)
        e.entity.setTag("difficulty", skeleton.getTag<Double>("difficulty"))
        if (!skeleton.hasLineOfSight(skeleton.target ?: e.entity)) return
        if ((skeleton.getTag<Int>("shots") ?: 0) < 2) {
            e.isCancelled = true
            e.entity.remove()
            skeleton.setTag("shots", (skeleton.getTag<Int>("shots") ?: 0) + 1)
        } else {
            skeleton.setTag("shots", 0)
            e.entity.velocity = skeleton.target!!.location.add(Vector(0.0, 0.75, 0.0)).add(skeleton.target!!.velocity).subtract(e.entity.location).toVector()
        }
    }*/
    private fun explosiveSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.EXPLOSIVE_SKELETON.id) return
        val skeleton = e.entity.shooter as Skeleton
        e.entity.setTag("id", CustomEntity.EXPLOSIVE_SKELETON_ARROW.id)
        e.entity.setTag("difficulty", skeleton.getTag<Double>("difficulty"))
        if (skeleton.getTag<Boolean>("trialspawned") == true) e.entity.setTag("trialspawned", true)
        if (!skeleton.hasLineOfSight(skeleton.target ?: e.entity)) return
        if ((skeleton.getTag<Int>("shots") ?: 0) < 2) {
            e.isCancelled = true
            e.entity.remove()
            skeleton.setTag("shots", (skeleton.getTag<Int>("shots") ?: 0) + 1)
        } else {
            skeleton.setTag("shots", 0)
        }
    }
    private fun machineGunSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.MACHINE_GUN_SKELETON.id) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("count") == 2) return
        val skeleton = e.entity.shooter as Skeleton
        e.entity.setTag("difficulty", (e.entity.shooter as Skeleton).getTag<Double>("difficulty"))
        if (!skeleton.hasLineOfSight(skeleton.target ?: e.entity)) return
        e.isCancelled = true
    }
    private fun swarmerSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.SWARMER_SKELETON.id) return
        e.entity.setTag("id", CustomEntity.SWARMER_SKELETON_ARROW.id)
    }
    private fun shieldBreakerSkeletonShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Skeleton) return
        if ((e.entity.shooter as Skeleton).getTag<Int>("id") != CustomEntity.SHIELD_BREAKER_SKELETON.id) return
        e.entity.setTag("id", CustomEntity.SHIELD_BREAKER_ARROW.id)
    }
    private fun witchShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Witch) return
        val difficulty = (e.entity.shooter!! as Witch).getTag<Double>("difficulty") ?: 0.0

        val possEffects = RandomSelector(
            Pair(PotionEffectType.SLOWNESS, 40), Pair(PotionEffectType.MINING_FATIGUE, 15), Pair(PotionEffectType.INSTANT_DAMAGE, 40),
            Pair(PotionEffectType.BLINDNESS, 10), Pair(PotionEffectType.NAUSEA, 30), Pair(PotionEffectType.WEAKNESS, 25),
            Pair(PotionEffectType.POISON, 40), Pair(PotionEffectType.WITHER, 10), Pair(PotionEffectType.SLOW_FALLING, 15),
            Pair(PotionEffectType.LEVITATION, 10),
        )

        val newMeta = (e.entity as SplashPotion).potionMeta
        newMeta.basePotionType = null
        newMeta.clearCustomEffects()
        newMeta.addCustomEffect(PotionEffect(possEffects.next(), (400 * (1 + difficulty/120)).toInt(), (difficulty/150).toInt()), true)
        newMeta.color = Color.fromRGB((Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt())
    }
    private fun sniperWitchShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Witch) return
        if ((e.entity.shooter as Witch).getTag<Int>("id") != CustomEntity.SNIPER_WITCH.id) return
        val witch = e.entity.shooter as Witch
        if (!witch.hasLineOfSight(witch.target ?: e.entity)) return
        if ((witch.getTag<Int>("shots") ?: 0) < 2) {
            e.isCancelled = true
            e.entity.remove()
            witch.setTag("shots", (witch.getTag<Int>("shots") ?: 0) + 1)
        } else {
            witch.setTag("shots", 0)
            e.entity.velocity = witch.target!!.location.add(Vector(0.0, 0.75, 0.0)).add(witch.target!!.velocity).subtract(e.entity.location).toVector()
        }
    }
    private fun bioweaponWitch(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Witch) return
        if ((e.entity.shooter as Witch).getTag<Int>("id") != CustomEntity.BIOWEAPON_WITCH.id) return
        val witch = e.entity.shooter as Witch
        val original = e.entity as SplashPotion
        e.isCancelled = true
        val potion = e.entity.location.world.spawn(e.entity.location, LingeringPotion::class.java)
        val potionMeta = potion.potionMeta
        potionMeta.clearCustomEffects()
        for (effect in original.effects) {
            potionMeta.addCustomEffect(effect, true)
        }
        potion.potionMeta = potionMeta
        potion.velocity = original.velocity
    }

    @EventHandler fun onCreeperExplode(e: EntityExplodeEvent) {
        if (e.entity.location.world != CustomItems.aridWorld) return
        checkSkeletonExplode(e)
        if (e.entity.type != EntityType.CREEPER) return
        val difficulty = e.entity.getTag<Double>("difficulty") ?: 0.0
        (e.entity as Creeper).activePotionEffects.forEach { x -> (e.entity as Creeper).removePotionEffect(x.type)}
        if (e.entity.getTag<Boolean>("trialspawned") == true) e.blockList().clear()

        //add negative lingering effects to explosion
        if (Math.random() < sqrt(difficulty)/10) {
            val effects =
                RandomSelector(Pair(PotionEffectType.HUNGER, 5), Pair(PotionEffectType.NAUSEA, 5), Pair(PotionEffectType.BLINDNESS, 2),
                    Pair(PotionEffectType.DARKNESS, 1), Pair(PotionEffectType.LEVITATION, 2), Pair(PotionEffectType.MINING_FATIGUE, 5),
                    Pair(PotionEffectType.POISON, 7), Pair(PotionEffectType.WEAKNESS, 3), Pair(PotionEffectType.WITHER, 4),)

            val effect = PotionEffect(effects.next(), 200, difficulty.pow(0.25).toInt().coerceIn(1, 4))
            (e.entity as Creeper).addPotionEffect(effect)
        }

        //arrowbomb creeper
        if (e.entity.getTag<Int>("id") == CustomEntity.ARROWBOMB_CREEPER.id) {
            for (i in 1..(difficulty.pow(0.8).toInt())) {
                val arrow = e.entity.world.spawnArrow(e.entity.location, Vector(Utils.randomRange(-1.0, 1.0), Math.random(), Utils.randomRange(-1.0, 1.0)).normalize(), 1F, 1F)
                arrow.shooter = e.entity as Creeper
            }
            //multiply one of x or z or x and z by -1 to get all directions
        }

        //firebomb creeper
        else if (e.entity.getTag<Int>("id") == CustomEntity.FIREBOMB_CREEPER.id) {
            if (e.entity.getTag<Boolean>("explosionCompleted") == true) return
            e.isCancelled = true
            e.entity.setTag("explosionCompleted", true)
            e.location.world.createExplosion(
                e.entity, e.entity.location.clone().add(Vector(0.0, 0.0, 0.05)), (e.entity as Creeper).explosionRadius.toFloat(),
                true, e.entity.getTag<Boolean>("trialspawned") ?: false
            )
            Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                val radius = (e.entity as Creeper).explosionRadius
                for (x in -radius..radius) {
                    for (y in -radius..radius) {
                        for (z in -radius..radius) {
                            if (e.location.clone().add(Vector(x, y, z)).block.type == Material.FIRE) {
                                if (Math.random() < 0.35) {
                                    e.location.clone().add(Vector(x, y, z)).block.type = Material.LAVA
                                }
                            }
                        }
                    }
                }
            },1)
        }

        //firework creeper
        else if (e.entity.getTag<Int>("id") == CustomEntity.FIREWORK_CREEPER.id) {
            for (i in 1..(difficulty.pow(0.5).toInt())) {
                val firework = e.entity.world.spawn(e.entity.location, Firework::class.java)
                firework.isShotAtAngle
                firework.velocity = Vector(Utils.randomRange(-1.0, 1.0), Math.random(), Utils.randomRange(-1.0, 1.0)).normalize().multiply(0.5)
                firework.ticksToDetonate = 3
                val newMeta = firework.fireworkMeta
                newMeta.power = (difficulty / 2).toInt()
                newMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).build())
                firework.fireworkMeta = newMeta
                firework.shooter = e.entity as Creeper
            }
        }

        //tnthead creeper
        else if (e.entity.getTag<Int>("id") == CustomEntity.TNTHEAD_CREEPER.id) {
            for (entity in e.entity.passengers) {
                if (entity.type == EntityType.TNT) {
                    (entity as TNTPrimed).fuseTicks = 30
                }
            }
        }
    }
    private fun checkSkeletonExplode(e: EntityExplodeEvent) {
        if (e.entity.type != EntityType.ARROW) return
        if (e.entity.getTag<Boolean>("trialspawned") != true) return
        e.blockList().clear()
    }

    @EventHandler fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.entity.world != CustomItems.aridWorld) return
        creeperExplode(e)
        //elytraBreakerSkeletonHit(e)
        sniperSkeletonHit(e)
        machineGunSkeletonHit(e)
        swarmerSkeletonHit(e)
        infectiousZombieHit(e)
        swarmerZombieHit(e)
        spiderHit(e)
        weaverSpiderHit(e)
        swarmerSlimeHit(e)
        swarmerCubeHit(e)
        replicatingSlimeTakeDamage(e)
    }
    private fun creeperExplode(e: EntityDamageByEntityEvent) {
        if ((e.damager.type != EntityType.CREEPER && e.damager.type != EntityType.ARROW) || (e.damageSource.causingEntity is Player || e.damageSource.directEntity is Player)) return
        if (e.entity !is Player) e.isCancelled = true
        if (e.entity.type != EntityType.CREEPER) return
        if (e.damager.getTag<Boolean>("chain") != true) return
        if (e.entity.getTag<Int>("id") == CustomEntity.NUCLEAR_CREEPER.id) return
        if (!(e.entity as Creeper).isIgnited) {
            (e.entity as Creeper).fuseTicks = (20 * Math.random()).toInt()
            (e.entity as Creeper).ignite()
        }
        (e.entity as Creeper).setTag("chain", true)
    }
    /*private fun elytraBreakerSkeletonHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_SKELETON_ARROW.id) return
        val difficulty = e.damager.getTag<Double>("difficulty")!!
        (e.entity as Player).isGliding = false
        //seconds
        EffectManager.applyEffect((e.entity as Player), CustomEffectType.ELYTRA_DISABLED, 20 * (10 + difficulty.pow(0.55)).toInt())
        //e.entity.setTag("elytradisabled", (10 + difficulty.pow(0.55)).toInt())
        CustomEffects.playSound(e.entity.location, Sound.ITEM_SHIELD_BREAK, 1F, 1F)
    }*/
    private fun sniperSkeletonHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SNIPER_SKELETON_ARROW.id) return
        val difficulty = e.damager.getTag<Double>("difficulty")!!
        e.damage = 25.0 + difficulty.pow(0.67)
    }
    private fun machineGunSkeletonHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.MACHINE_GUN_SKELETON_ARROW.id) return
        val difficulty = e.damager.getTag<Double>("difficulty")!!
        e.damage = 3.0 + difficulty.pow(0.4)
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun swarmerSkeletonHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SWARMER_SKELETON_ARROW.id) return
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun infectiousZombieHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Zombie) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.INFECTIOUS_ZOMBIE.id) return
        val difficulty = e.damager.getTag<Double>("difficulty")!!
        val possEffects = RandomSelector(
            Pair(PotionEffectType.HUNGER, 20),
            Pair(PotionEffectType.POISON, 20),
            Pair(PotionEffectType.WITHER, 10),
            Pair(PotionEffectType.BLINDNESS, 10),
            Pair(PotionEffectType.NAUSEA, 20),
            Pair(PotionEffectType.MINING_FATIGUE, 5)
        )

        (e.entity as Player).addPotionEffect(
            PotionEffect(
                possEffects.next(),
                200 + difficulty.pow(1.2).toInt(),
                1 + difficulty.toInt()/110,
            )
        )
    }
    private fun swarmerZombieHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Zombie) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SWARMER_ZOMBIE.id) return
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun spiderHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Spider) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SWARMER_SPIDER.id &&
            e.damager.getTag<Int>("id") != CustomEntity.BROODMOTHER_SPAWN.id &&
            e.damager.getTag<Int>("id") != CustomEntity.CAVE_BROODMOTHER_SPIDER.id) return
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun weaverSpiderHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Spider) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.WEAVER_SPIDER.id) return
        if (e.damager.getTag<Int>("maincooldown") != 0) return
        if (e.entity.location.block.type != Material.AIR) return
        e.entity.location.block.type = Material.COBWEB
        val difficulty = e.damager.getTag<Double>("difficulty")!!
        e.damager.setTag("maincooldown", 15 - difficulty.toInt()/15)
    }
    private fun swarmerSlimeHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Slime) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SWARMER_SLIME.id) return
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun swarmerCubeHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is MagmaCube) return
        if (e.entity !is Player) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SWARMER_CUBE.id) return
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).noDamageTicks = 0
        })
    }
    private fun replicatingSlimeTakeDamage(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (e.entity !is Slime) return
        if (e.entity.getTag<Int>("id") != CustomEntity.REPLICATING_SLIME.id) return
        if ((e.entity as Slime).health < e.damage) return
        val newSlime = e.entity.world.spawn(e.entity.location, Slime::class.java, CreatureSpawnEvent.SpawnReason.BUILD_WITHER)
        CustomEntity.convert(newSlime, CustomEntity.REPLICATING_SLIME)
        newSlime.size = 1
    }

    @EventHandler fun onMobSpawn(e: CreatureSpawnEvent) {
        //scale up more for trial spawned mobs
        if (e.entity.world != CustomItems.aridWorld) return
        if (Math.random() < 0.25) {e.isCancelled = true; return}
        if (e.entity is Mob) {
            (e.entity as Mob).target = e.entity.location.getNearbyPlayers(100.0).firstOrNull()
        }
        var difficulty = e.location.world.getDifficultyIndex(e.location)
        var spawnType = CustomSpawnType.NATURAL
        if (e.entity.isSilent && !e.entity.hasAI()) {
            // manipulate difficulty here
            difficulty *= 2.0
            spawnType = CustomSpawnType.OMINOUS_SPAWNER
            e.entity.isSilent = false
            e.entity.setAI(true)
        } else if (!e.entity.hasAI()) {
            // manipulate difficulty here
            difficulty *= 1.5
            spawnType = CustomSpawnType.NORMAL_SPAWNER
            e.entity.setAI(true)
        }

        creeperSpawn(e, difficulty, spawnType)
        skeletonSpawn(e, difficulty, spawnType)
        zombieSpawn(e, difficulty, spawnType)
        spiderSpawn(e, difficulty, spawnType)
        slimeSpawn(e, difficulty, spawnType)
        magmaCubeSpawn(e, difficulty, spawnType)
        witchSpawn(e, difficulty, spawnType)
    }
    private fun creeperSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.CREEPER) return
        val mob = e.entity as Creeper
        if (spawnType == CustomSpawnType.OMINOUS_SPAWNER || spawnType == CustomSpawnType.NORMAL_SPAWNER) {
            mob.setTag("trialspawned", true)
        }
        mob.setTag("difficulty", difficulty)
        val explosionRadius = (difficulty.pow(0.4) * (Math.random() / 10 * 3 + 0.7)).coerceAtLeast(3.0).toInt()
        val fuseTicks = (30 - difficulty/10 * Utils.randomRange(0.65, 1.0)).coerceAtLeast(15.0).toInt()
        val health = 20 + (difficulty.pow(0.6) * Utils.randomRange(0.8, 1.2))
        mob.maxFuseTicks = fuseTicks
        mob.explosionRadius = explosionRadius
        mob.getAttribute(Attribute.MAX_HEALTH)!!.baseValue = health
        mob.health = health
        val weights = RandomSelector(
            Pair(CustomEntity.LEAPING_CREEPER, 20), Pair(CustomEntity.FIREBOMB_CREEPER, 10), Pair(CustomEntity.BREACHING_CREEPER, 20),
            Pair(CustomEntity.FIREWORK_CREEPER, 10), Pair(CustomEntity.ARROWBOMB_CREEPER, 20), Pair(CustomEntity.CHAIN_REACTION_CREEPER, 15),
            Pair(CustomEntity.TNTHEAD_CREEPER, 5), Pair(CustomEntity.SHIELD_BREAKER_CREEPER, 15), Pair(CustomEntity.NUCLEAR_CREEPER, 5),
            Pair(CustomEntity.PREIGNITION_CREEPER, 15), Pair(CustomEntity.HOPPING_CREEPER, 40), Pair(CustomEntity.MINI_BREACHING_CREEPER, 25),
            Pair(CustomEntity.BABY_CREEPER, 40),
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            val type = weights.next()
            CustomEntity.convert(mob, type)
        }
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.25 * Utils.randomRange(1.0, 1.2) * (1+difficulty/300)).coerceAtMost(0.4)

        // onexplosion for negative effects, fire/lava (for lava search for fire in radius and replace some with lava)
        e.entity.setTag("maincooldown", 5)
    }
    private fun skeletonSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.SKELETON) return
        val mob = e.entity as Skeleton
        mob.setTag("difficulty", difficulty)
        if (spawnType == CustomSpawnType.OMINOUS_SPAWNER || spawnType == CustomSpawnType.NORMAL_SPAWNER) {
            mob.setTag("trialspawned", true)
        }
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.25 * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(0.4)
        val weights = RandomSelector(
            Pair(CustomEntity.HOMING_SKELETON, 40), Pair(CustomEntity.EXPLOSIVE_SKELETON, 10), Pair(CustomEntity.ELYTRA_BREAKER_SKELETON, 25),
            Pair(CustomEntity.SWORDSMAN_SKELETON, 60), Pair(CustomEntity.SNIPER_SKELETON, 10), Pair(CustomEntity.ENERGY_SHIELD_SKELETON, 10),
            Pair(CustomEntity.MACHINE_GUN_SKELETON, 10), Pair(CustomEntity.SHIELD_BREAKER_SKELETON, 20), Pair(CustomEntity.BABY_SKELETON, 40),
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            val type = weights.next()
            CustomEntity.convert(mob, type)
        }
        addGear(mob, difficulty)

        e.entity.setTag("maincooldown", 5)
    }
    private fun zombieSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.ZOMBIE) return
        val mob = e.entity as Zombie
        mob.setTag("difficulty", difficulty)
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.25 * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(0.4)
        val weights = RandomSelector(
            Pair(CustomEntity.JUMPING_ZOMBIE, 30), Pair(CustomEntity.INFECTIOUS_ZOMBIE, 30), Pair(CustomEntity.SHADOW_ASSASSIN_ZOMBIE, 30),
            Pair(CustomEntity.TANK_ZOMBIE, 15), Pair(CustomEntity.SWARMER_ZOMBIE, 45),
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            val type = weights.next()
            CustomEntity.convert(mob, type)
        }
        addGear(mob, difficulty)

        e.entity.setTag("maincooldown", 5)
    }
    private fun spiderSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.SPIDER) return
        val mob = e.entity as Spider
        mob.setTag("difficulty", difficulty)
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.3 * Utils.randomRange(1.0, 1.2) * (1+difficulty/300)).coerceAtMost(0.48)
        val health = 16 + (difficulty.pow(0.6) * Utils.randomRange(0.8, 1.2))
        mob.getAttribute(Attribute.MAX_HEALTH)!!.baseValue = health
        mob.health = health
        mob.getAttribute(Attribute.ATTACK_DAMAGE)!!.baseValue = difficulty.pow(0.55).coerceAtLeast(3.0)
        if (Utils.randomPercent(difficulty/8)) {
            val effects = RandomSelector(
                Pair(PotionEffectType.SPEED, 20), Pair(PotionEffectType.STRENGTH, 10),
                Pair(PotionEffectType.INVISIBILITY, 10), Pair(PotionEffectType.REGENERATION, 10),
            )
            mob.addPotionEffect(PotionEffect(effects.next(), PotionEffect.INFINITE_DURATION, (difficulty/100).toInt()))
        }
        val weights = RandomSelector(
            Pair(CustomEntity.BROODMOTHER_SPIDER, 10), Pair(CustomEntity.CAVE_BROODMOTHER_SPIDER, 40), Pair(CustomEntity.LEAPING_SPIDER, 40),
            Pair(CustomEntity.SWARMER_SPIDER, 50), Pair(CustomEntity.TARANTULA_SPIDER, 25), Pair(CustomEntity.WEAVER_SPIDER, 25),
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) ) && e.spawnReason != CreatureSpawnEvent.SpawnReason.DUPLICATION) {
            val type = weights.next()
            CustomEntity.convert(mob, type)
        }

        e.entity.setTag("maincooldown", 5)
    }
    private fun slimeSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.SLIME) return
        if (e.spawnReason == CreatureSpawnEvent.SpawnReason.BUILD_WITHER) return
        val mob = e.entity as Slime
        if (Math.random() < 0.5 && e.spawnReason != CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
            e.entity.location.world.spawn(e.entity.location, MagmaCube::class.java, e.spawnReason)
            e.isCancelled = true
            return
        }
        mob.setTag("difficulty", difficulty)

        if (e.spawnReason != CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) mob.size = (1..3).random()
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = ((0.3 + 0.1 * mob.size) * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(2.0)
        mob.getAttribute(Attribute.ATTACK_DAMAGE)?.baseValue = ((3 * (mob.size - 1)) + difficulty.pow(0.52))
        val health = (4.0.pow(mob.size - 1) + mob.size * difficulty.pow(0.35))
        mob.getAttribute(Attribute.MAX_HEALTH)?.baseValue = health
        mob.health = health

        val weights = RandomSelector(
            Pair(CustomEntity.SWARMER_SLIME, 40), Pair(CustomEntity.LEAPING_SLIME, 25), Pair(CustomEntity.LAUNCHING_SLIME, 20)
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            if (mob.size != 1) {
                val type = weights.next()
                CustomEntity.convert(mob, type)
            } else {
                CustomEntity.convert(mob, CustomEntity.REPLICATING_SLIME)
            }
        }

        e.entity.setTag("maincooldown", 5)
    }
    private fun magmaCubeSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.MAGMA_CUBE) return
        val mob = e.entity as MagmaCube
        mob.setTag("difficulty", difficulty)
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.25 * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(2.25)

        if (e.spawnReason != CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) mob.size = (1..3).random()
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = ((0.3 + 0.1 * mob.size) * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(2.0)
        mob.getAttribute(Attribute.ATTACK_DAMAGE)?.baseValue = ((3 * (mob.size)) + difficulty.pow(0.52))
        val health = (4.0.pow(mob.size - 1) + mob.size * difficulty.pow(0.35))
        mob.getAttribute(Attribute.MAX_HEALTH)?.baseValue = health
        mob.health = health

        val largeWeights = RandomSelector(
            Pair(CustomEntity.SWARMER_CUBE, 40), Pair(CustomEntity.LEAPING_CUBE, 20), Pair(CustomEntity.LAUNCHING_CUBE, 25)
        )
        val smallWeights = RandomSelector(
            Pair(CustomEntity.SWARMER_CUBE, 40), Pair(CustomEntity.LEAPING_CUBE, 20), Pair(CustomEntity.LAUNCHING_CUBE, 25), Pair(CustomEntity.LAVA_CUBE, 80)
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            if (mob.size != 1) {
                val type = largeWeights.next()
                CustomEntity.convert(mob, type)
            } else {
                val type = smallWeights.next()
                CustomEntity.convert(mob, type)
            }
        }

        e.entity.setTag("maincooldown", 5)
    }
    private fun witchSpawn(e: CreatureSpawnEvent, difficulty: Double, spawnType: CustomSpawnType) {
        if (e.entity.type != EntityType.WITCH) return
        val mob = e.entity as Witch
        mob.setTag("difficulty", difficulty)
        val weights = RandomSelector(
            Pair(CustomEntity.ENDER_WITCH, 20), Pair(CustomEntity.CLERIC_WITCH, 10), Pair(CustomEntity.BIOWEAPON_WITCH, 20),
            Pair(CustomEntity.SNIPER_WITCH, 10), Pair(CustomEntity.COLONIEL_WITCH, 10),
        )
        if (Utils.randomPercent( difficulty.pow(0.86).coerceAtMost(100.0) )) {
            val type = weights.next()
            CustomEntity.convert(mob, type)
        }
        mob.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (0.25 * Utils.randomRange(1.0, 1.2) * (1+difficulty/200)).coerceAtMost(0.4)
        val health = 26 + (difficulty.pow(0.61) * Utils.randomRange(0.8, 1.2))
        mob.getAttribute(Attribute.MAX_HEALTH)!!.baseValue = health
        mob.health = health

        // onexplosion for negative effects, fire/lava (for lava search for fire in radius and replace some with lava)
        e.entity.setTag("maincooldown", 5)
    }

    private fun addGear(mob: Mob, difficulty: Double) {
        val gearIndexes = listOf(
            difficulty * Utils.randomRange(0.8, 1.2), difficulty * Utils.randomRange(0.8, 1.2),
            difficulty * Utils.randomRange(0.8, 1.2), difficulty * Utils.randomRange(0.8, 1.2),
            difficulty * Utils.randomRange(0.8, 1.2),
        )

        mob.equipment.helmet = getEquipment(gearIndexes[0], EquipmentSlot.HEAD)
        mob.equipment.chestplate = getEquipment(gearIndexes[1], EquipmentSlot.CHEST)
        mob.equipment.leggings = getEquipment(gearIndexes[2], EquipmentSlot.LEGS)
        mob.equipment.boots = getEquipment(gearIndexes[3], EquipmentSlot.FEET)

        if (mob.equipment.helmet.type != Material.AIR) mob.equipment.helmet = mob.equipment.helmet.enchantWithLevels((17.0/200.0 * difficulty).toInt(), true, Random())
        if (mob.equipment.chestplate.type != Material.AIR) mob.equipment.chestplate = mob.equipment.chestplate.enchantWithLevels((17.0/200.0 * difficulty).toInt(), true, Random())
        if (mob.equipment.leggings.type != Material.AIR) mob.equipment.leggings = mob.equipment.leggings.enchantWithLevels((17.0/200.0 * difficulty).toInt(), true, Random())
        if (mob.equipment.boots.type != Material.AIR) mob.equipment.boots = mob.equipment.boots.enchantWithLevels((17.0/200.0 * difficulty).toInt(), true, Random())

        if (mob.type == EntityType.ZOMBIE || mob.getTag<Int>("id") == CustomEntity.SWORDSMAN_SKELETON.id) {
            val item = getEquipment(gearIndexes[4], EquipmentSlot.HAND)
            if (item.type != Material.AIR) item.ench("SH" + (difficulty/28).toInt().toString())
            mob.equipment.setItemInMainHand(item)
        } else {
            val item = ItemStack(Material.BOW)
            item.ench("PW" + ((difficulty/28).toInt()).toString())
            if (Math.random() < difficulty.pow(0.85)/100) item.ench("FL1")
            if (Math.random() < difficulty.pow(0.85)/100) item.ench("PU" + (difficulty/60).toInt().toString())
            mob.equipment.setItemInMainHand(item)
        }

        mob.equipment.helmetDropChance = (1/(0.003*gearIndexes[0].pow(2.0))).toFloat().coerceAtMost(1.0F)
        mob.equipment.chestplateDropChance = (1/(0.003*gearIndexes[1].pow(2.0))).toFloat().coerceAtMost(1.0F)
        mob.equipment.leggingsDropChance = (1/(0.003*gearIndexes[2].pow(2.0))).toFloat().coerceAtMost(1.0F)
        mob.equipment.bootsDropChance = (1/(0.003*gearIndexes[3].pow(2.0))).toFloat().coerceAtMost(1.0F)
        mob.equipment.itemInMainHandDropChance = (1/(0.003*gearIndexes[4].pow(2.0))).toFloat().coerceAtMost(1.0F)


    }
    private fun getEquipment(difficulty: Double, equipmentSlot: EquipmentSlot): ItemStack {
        return when(difficulty) {
            in 0.0..28.5 -> ItemStack(Material.AIR)
            in 28.5..57.1 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.LEATHER_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.LEATHER_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.LEATHER_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.LEATHER_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.WOODEN_SWORD), 1), Pair(ItemStack(Material.WOODEN_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            in 57.1..85.7 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.GOLDEN_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.GOLDEN_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.GOLDEN_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.GOLDEN_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.GOLDEN_SWORD), 1), Pair(ItemStack(Material.GOLDEN_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            in 85.7..114.3 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.CHAINMAIL_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.CHAINMAIL_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.CHAINMAIL_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.CHAINMAIL_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.STONE_SWORD), 1), Pair(ItemStack(Material.STONE_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            in 114.3..142.9 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.IRON_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.IRON_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.IRON_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.IRON_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.IRON_SWORD), 1), Pair(ItemStack(Material.IRON_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            in 142.9..171.4 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.DIAMOND_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.DIAMOND_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.DIAMOND_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.DIAMOND_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.DIAMOND_SWORD), 1), Pair(ItemStack(Material.DIAMOND_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            in 171.4..300.0 -> when(equipmentSlot) {
                EquipmentSlot.HEAD -> ItemStack(Material.NETHERITE_HELMET)
                EquipmentSlot.CHEST -> ItemStack(Material.NETHERITE_CHESTPLATE)
                EquipmentSlot.LEGS -> ItemStack(Material.NETHERITE_LEGGINGS)
                EquipmentSlot.FEET -> ItemStack(Material.NETHERITE_BOOTS)
                EquipmentSlot.HAND -> RandomSelector(Pair(ItemStack(Material.NETHERITE_SWORD), 1), Pair(ItemStack(Material.NETHERITE_AXE), 1)).next()
                else -> ItemStack(Material.AIR)
            }
            else -> ItemStack(Material.AIR)
        }
    }

    private var taskFuture: BukkitTask? = null
    private var counter: Int = 0
    override fun run() {
        taskFuture = Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
            counter = if (counter == 2400) 0 else counter + 1
            if (counter % 20 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    if (player.world != CustomItems.aridWorld) continue
                    val aggroRange = 60.0
                    for (mob in player.getNearbyEntities(aggroRange, aggroRange, aggroRange)) {
                        if (mob.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
                        mob.setTag("tick", Bukkit.getServer().currentTick)
                        if (mob !is Mob) continue
                        val difficulty = mob.getTag<Double>("difficulty") ?: 0.0
                        //set target to player so constant aggro
                        if (mob.target !is Player) {
                            mob.target = player
                        }
                        //remove 1 from each cd
                        if (mob.getTag<Int>("id") != null) {
                            val currentCd = mob.getTag<Int>("maincooldown") ?: 0
                            mob.setTag("maincooldown", if (currentCd == 0) 0 else currentCd - 1)
                        }
                        leapingCreeperActivate(mob, difficulty)
                        preigniteCreeperActivate(mob, player, difficulty)
                        jumpingZombieActivate(mob, difficulty)
                        shadowAssassinZombieActivate(mob, difficulty)
                        leapingSpiderActivate(mob, difficulty)
                        leapingSlimeActivate(mob, difficulty)
                        leapingCubeActivate(mob, difficulty)
                        enderWitchActivate(mob, difficulty)
                        colonielWitchActivate(mob, difficulty)
                        clericWitchActivate(mob, difficulty)
                    }
                }
            }
            if (counter % 20 == 10) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    if (player.world != CustomItems.aridWorld) continue
                    val aggroRange = 60.0
                    for (mob in player.getNearbyEntities(aggroRange, aggroRange, aggroRange)) {
                        if (mob.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
                        mob.setTag("tick", Bukkit.getServer().currentTick)
                        if (mob !is Mob) continue
                        breachingCreeperTrigger(mob, player)
                        miniBreachingCreeperTrigger(mob)
                    }
                }
            }
            if (counter % 5 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    if (player.world != CustomItems.aridWorld) continue
                    val aggroRange = 60.0
                    for (mob in player.getNearbyEntities(aggroRange, aggroRange, aggroRange)) {
                        if (mob.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
                        mob.setTag("tick", Bukkit.getServer().currentTick)
                        if (mob !is Mob) continue
                        energyShieldSkeletonTrigger(mob, player)
                    }
                }
            }
            //every tick
            for (player in Bukkit.getServer().onlinePlayers) {
                if (player.world != CustomItems.aridWorld) continue
                for (entity in player.getNearbyEntities(100.0, 100.0, 100.0)) {
                    if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
                    entity.setTag("tick", Bukkit.getServer().currentTick)
                    homingSkeletonArrowUpdate(entity)
                    //elytraBreakerSkeletonArrowUpdate(entity)
                    machineGunSkeletonTrigger(entity)
                }
            }
            //if ((counter % 100))
        }, 1, 1)
    }

    private fun clericWitchActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.CLERIC_WITCH.id)  return
        if (mob.getTag<Int>("maincooldown") != 0) return

        for (entity in mob.getNearbyEntities(20.0, 20.0, 20.0)) {
            if (entity !is Mob) continue
            if (entity.isDead) return
            if (entity.health == 0.0) return
            entity.health = (entity.health + (10 + difficulty.pow(0.45))).coerceAtMost(entity.getAttribute(Attribute.MAX_HEALTH)!!.baseValue)
        }
        mob.setTag("maincooldown", 10)
    }
    private fun colonielWitchActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.COLONIEL_WITCH.id)  return
        if (mob.getTag<Int>("maincooldown") != 0) return

        for (entity in mob.getNearbyEntities(20.0, 20.0, 20.0)) {
            if (entity is Player) continue
            if (entity !is Mob) continue
            if (Math.random() < difficulty.pow(0.85)/100) entity.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 200, 1))
            if (Math.random() < difficulty.pow(0.85)/100) entity.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 1))
            if (Math.random() < difficulty.pow(0.85)/100) entity.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 200, 1))
            if (Math.random() < difficulty.pow(0.85)/100) entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1))
        }
        mob.setTag("maincooldown", 10)
    }
    private fun enderWitchActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.ENDER_WITCH.id)  return
        if (mob.getTag<Int>("maincooldown") != 0) return
        if (mob.target == null) return

        mob.teleport(mob.target!!.location.add(mob.target!!.location.direction.normalize().multiply(-1).toLocation(mob.target!!.world)))
        mob.setTag("maincooldown", 15)
    }
    private fun leapingSlimeActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.LEAPING_SLIME.id) return
        if (mob.getTag<Int>("maincooldown") != 0) return
        if (mob.target == null) return
        if (!mob.hasLineOfSight(mob.target!!)) return

        mob.velocity =
            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                .add(Vector(0.0, 0.35, 0.0))
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }
    private fun leapingCubeActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.LEAPING_CUBE.id) return
        if (mob.getTag<Int>("maincooldown") != 0) return
        if (mob.target == null) return
        if (!mob.hasLineOfSight(mob.target!!)) return

        mob.velocity =
            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                .add(Vector(0.0, 0.35, 0.0))
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }
    private fun leapingSpiderActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.LEAPING_SPIDER.id) return
        if (mob.getTag<Int>("maincooldown") != 0) return
        if (mob.target == null) return
        if (!mob.hasLineOfSight(mob.target!!)) return

        mob.velocity =
            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                .add(Vector(0.0, 0.35, 0.0))
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }
    private fun shadowAssassinZombieActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.SHADOW_ASSASSIN_ZOMBIE.id)  return
        if (mob.getTag<Int>("maincooldown") != 0) return
        if (mob.target == null) return

        mob.teleport(mob.target!!.location.add(mob.target!!.location.direction.normalize().multiply(-1).toLocation(mob.target!!.world)))
        mob.setTag("maincooldown", 15)
    }
    private fun jumpingZombieActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.JUMPING_ZOMBIE.id || mob.getTag<Int>("maincooldown") != 0 || mob.target == null || !mob.hasLineOfSight(mob.target!!)) return

        mob.velocity =
            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                .add(Vector(0.0, 0.35, 0.0))
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }
    private fun machineGunSkeletonTrigger(entity: Entity) {
        if (entity.type != EntityType.SKELETON) return
        if (entity.getTag<Int>("id") != CustomEntity.MACHINE_GUN_SKELETON.id) return
        val skeleton = entity as Skeleton
        if (skeleton.target == null) return
        entity.setTag("count", (entity.getTag<Int>("count") ?: 0) + 1)
        if ((entity.getTag<Int>("count") ?: 0) < 2) return
        if (skeleton.target!!.location.subtract(skeleton.location).length() > 10) return

        val locDiff = skeleton.target!!.location.subtract(skeleton.location).toVector().multiply(0.119).add(Vector(Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05)))
        val locDiffHoriz = Vector(locDiff.x, 0.0, locDiff.y)
        val vect = Vector(locDiff.x, locDiffHoriz.length() * 0.2 + locDiff.y, locDiff.z)

        val arrow = skeleton.launchProjectile(Arrow::class.java, vect)
        arrow.setTag("id", CustomEntity.MACHINE_GUN_SKELETON_ARROW.id)
        arrow.setTag("difficulty", entity.getTag<Double>("difficulty"))

        entity.setTag("count", 0)
    }
    private fun energyShieldSkeletonTrigger(mob: Mob, player: Player) {
        if (mob.type != EntityType.SKELETON) return
        if (mob.getTag<Int>("id") != CustomEntity.ENERGY_SHIELD_SKELETON.id) return
        if (player.location.subtract(mob.location).length() >= 10.0) return
        player.velocity = player.velocity.add(player.location.subtract(mob.location).toVector().normalize().multiply(0.6))
    }
    /*private fun elytraBreakerSkeletonArrowUpdate(entity: Entity) {
        if (entity.type != EntityType.ARROW || (entity as Arrow).isInBlock) return
        if (entity.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_SKELETON_ARROW.id) return
        val target = Bukkit.getEntity(entity.getTag<UUID>("target")!!)
        val currentVelocity = entity.velocity.length()
        if (target == null) return
        val newDirection = target.location.subtract(entity.location).toVector().add(Vector(0.0, 0.5, 0.0))
        entity.velocity = newDirection.normalize().multiply((target.location.subtract(entity.location).length() / 8).coerceAtLeast(8.0))
    }*/
    private fun homingSkeletonArrowUpdate(entity: Entity) {
        if (entity.type != EntityType.ARROW || (entity as Arrow).isInBlock) return
        if (entity.getTag<Int>("id") != CustomEntity.HOMING_SKELETON_ARROW.id) return
        val target = Bukkit.getEntity(entity.getTag<UUID>("target")!!)
        val currentVelocity = entity.velocity.length() * 1.05
        if (target == null) return
        val newDirection = entity.velocity.add(target.location.subtract(entity.location).toVector().add(Vector(0.0, 0.5, 0.0)).normalize().multiply(50))
        entity.velocity = newDirection.normalize().multiply(currentVelocity)
    }
    private fun miniBreachingCreeperTrigger(mob: Mob) {
        if (mob.getTag<Int>("id") != CustomEntity.MINI_BREACHING_CREEPER.id || mob.target == null || mob.hasLineOfSight(mob.target!!)) return
        /*mob.velocity =
                            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                                .add(Vector(0.0, 0.35, 0.0))
                        mob.setTag("maincooldown", 10)*/
        val prevLoc = mob.getTag<Location>("prevloc")
        if (prevLoc != null && prevLoc.subtract(mob.location).length() < 0.1) {
            (mob as Creeper).ignite()
        } else {
            mob.setTag("prevloc", mob.location)
        }
    }
    private fun breachingCreeperTrigger(mob: Mob, player: Player) {
        if (mob.getTag<Int>("id") != CustomEntity.BREACHING_CREEPER.id || mob.target == null || mob.hasLineOfSight(mob.target!!)) return
        /*mob.velocity =
                            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                                .add(Vector(0.0, 0.35, 0.0))
                        mob.setTag("maincooldown", 10)*/
        val prevLoc = mob.getTag<Location>("prevloc")
        if (prevLoc != null && prevLoc.subtract(mob.location).length() < 0.1) {
            (mob as Creeper).ignite()
            if (Math.random() < 0.25) {
                mob.velocity =
                    player.location.subtract(mob.location).toVector().normalize().multiply(Math.random() * 10)
            }
        } else {
            mob.setTag("prevloc", mob.location)
        }
    }
    private fun leapingCreeperActivate(mob: Mob, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.LEAPING_CREEPER.id || mob.getTag<Int>("maincooldown") != 0 || mob.target == null || !mob.hasLineOfSight(mob.target!!)) return

        mob.velocity =
            mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15))
                .add(Vector(0.0, 0.35, 0.0))
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }
    private fun preigniteCreeperActivate(mob: Mob, player: Player, difficulty: Double) {
        if (mob.getTag<Int>("id") != CustomEntity.PREIGNITION_CREEPER.id || mob.getTag<Int>("maincooldown") != 0 ||
            mob.target == null || !mob.hasLineOfSight(mob.target!!) || player.location.clone().subtract(player.location).length() >= 20) return

        mob.velocity = mob.velocity.add(mob.target!!.location.subtract(mob.location).toVector().multiply(0.15)).add(Vector(0.0, 0.35, 0.0))
        (mob as Creeper).ignite()
        mob.setTag("maincooldown", (20 - difficulty/15).toInt())
    }

    fun cancel() {
        taskFuture!!.cancel()
    }
}