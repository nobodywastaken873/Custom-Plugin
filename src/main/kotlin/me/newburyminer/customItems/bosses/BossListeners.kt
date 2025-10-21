package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.decrementTag
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.getHitboxCorners
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.incrementTag
import me.newburyminer.customItems.Utils.Companion.randomToInt
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setAttr
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.CustomEntity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.damage.CenterKnockback
import me.newburyminer.customItems.helpers.damage.ConstantKnockback
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.helpers.shapes.Circle
import me.newburyminer.customItems.helpers.shapes.NegativePolygon
import me.newburyminer.customItems.helpers.shapes.Shape
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTables
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class BossListeners: Listener, Runnable {

    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        e.player.health = 0.0
    }

    @EventHandler fun onArrowLand(e: ProjectileHitEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        if (e.entity.type != EntityType.ARROW) return
        if (e.hitBlock != null) {
            e.entity.remove()
        } else if (e.hitEntity != null) {
            if (e.hitEntity !is Player && e.entity.shooter !is Player) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler fun onSculkSpread(e: BlockSpreadEvent) {
        if (e.block.location.world == CustomItems.bossWorld) e.isCancelled = true
    }

    @EventHandler fun onPlayerThrowPearl(e: ProjectileLaunchEvent) {
        if (e.entity.type != EntityType.ENDER_PEARL) return
        if (e.entity.world != CustomItems.bossWorld) return
        if (e.entity.shooter !is Player) return
        if ((e.entity.shooter as Player).location.block.isPassable) return
        e.isCancelled = true
    }

    /*@EventHandler fun onPlayerInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.item == null) return
        if (e.player.isBeingTracked()) return
        var summon: ItemStack? = null
        for (custom in arrayOf(
                CustomItem.WARDEN_SPAWNER, CustomItem.WITHER_SPAWNER, CustomItem.DESERT_SPAWNER,
                CustomItem.BASTION_SPAWNER, CustomItem.MONUMENT_SPAWNER
            )) if (e.item!!.isItem(custom)) {summon = e.item!!; break}
        if (summon == null) return

        val boss = when (summon.getCustom()!!) {
            CustomItem.WARDEN_SPAWNER -> CustomBoss.WARDEN
            CustomItem.WITHER_SPAWNER -> CustomBoss.WITHER
            CustomItem.DESERT_SPAWNER -> CustomBoss.HUSK
            CustomItem.BASTION_SPAWNER -> CustomBoss.PIGLIN
            CustomItem.MONUMENT_SPAWNER -> CustomBoss.GUARDIAN
            else -> return
        }

        if (boss.isAlive()) {
            e.player.sendMessage(Utils.text("This boss is already alive. Please try again later.", Utils.FAILED_COLOR))
            return
        }

        summon.amount -= 1
        for (player in e.player.location.getNearbyPlayers(20.0)) {
            player.teleport(boss.getCenter())
            player.gameMode = GameMode.ADVENTURE
            player.sendMessage(Utils.text("Hit the boss to begin.", Utils.GRAY))
        }

        when (boss) {
            CustomBoss.WARDEN -> wardenSummon()
            CustomBoss.WITHER -> witherSummon()
            CustomBoss.HUSK -> {}
            CustomBoss.PIGLIN -> {}
            CustomBoss.GUARDIAN -> {}
        }

    }*/

    @EventHandler fun onEntityTakeDamage(e: EntityDamageEvent) {
        miniWardenDamage(e)
        miniWardenTakeDamage(e)
        bossDamage(e)
    }
    private fun miniWardenDamage(e: EntityDamageEvent) {
        if (e.entity.world != CustomItems.bossWorld) return
        if (e.damageSource.directEntity !is Warden) return
        if (e.damageSource.directEntity!!.getTag<Int>("id") != CustomEntity.BOSS_SPAWNED_WARDEN.id) return
        if (e.damageSource.damageType != DamageType.SONIC_BOOM) return
        e.damage *= 0.4
    }
    private fun miniWardenTakeDamage(e: EntityDamageEvent) {
        if (e.entity.world != CustomItems.bossWorld) return
        if (e.entity.getTag<Int>("id") != CustomEntity.BOSS_SPAWNED_WARDEN.id) return
        if (e.damageSource.damageType != DamageType.MACE_SMASH) return
        e.damage *= 0.2
    }
    private fun bossDamage(e: EntityDamageEvent) {
        if (e.entity.getTag<Int>("bossid") == null) return
        val customBoss = CustomBoss.entries[e.entity.getTag<Int>("bossid")!!]
        if (e.damageSource.damageType == DamageType.MACE_SMASH) e.damage *= 0.1
        e.damage *= (1.0 / (8.0 + 8.0 * customBoss.players.size))
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            val progress = customBoss.hpPercent
            customBoss.bossBar!!.progress = progress

            if (customBoss == CustomBoss.WARDEN) {
                if (customBoss.boss!!.getTag<Int>("phase")!! == 2 && progress <= 0.2) {
                    wardenStartPhase3()
                } else if (customBoss.boss!!.getTag<Int>("phase")!! == 1 && progress <= 0.6) {
                    wardenStartPhase2()
                } else if (customBoss.boss!!.getTag<Int>("phase")!! == 0 && progress < 1.0) {
                    wardenStartPhase1()
                }
            }
        })
    }

    @EventHandler fun onEntityDeath(e: EntityDeathEvent) {
        bossDeath(e)
        normalDeath(e)
        miniWardenDeath(e)
    }
    private fun bossDeath(e: EntityDeathEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        if (e.entity.getTag<Int>("bossid") == null) return
        val customBoss = CustomBoss.entries[e.entity.getTag<Int>("bossid")!!]
        for (player in customBoss.players) {
            player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
            // give loot
            for (item in customBoss.loot.roll()) {
                player.addItemorDrop(item)
            }
            if (customBoss == CustomBoss.WARDEN) {
                val lootV2 = mutableListOf<ItemStack>()
                for (i in 0..30) {
                    val lootContext = LootContext.Builder(player.location).build()
                    val chestLoot = LootTables.ANCIENT_CITY.lootTable.populateLoot(null, lootContext)
                    lootV2.addAll(chestLoot)
                }
                for (item in lootV2) {
                    player.addItemorDrop(item)
                }
            }
            //remove bars
            for (boss in CustomBoss.entries) {
                boss.bossBar.removePlayer(player)
            }
            player.gameMode = GameMode.SURVIVAL
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 0.4F)
        }
        endBosses(customBoss)
    }
    private fun normalDeath(e: EntityDeathEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        e.drops.clear()
    }
    private fun miniWardenDeath(e: EntityDeathEvent) {
        if (e.entity.location.world != CustomItems.bossWorld) return
        if (e.entity.getTag<Int>("id") != CustomEntity.BOSS_SPAWNED_WARDEN.id) return
        CustomBoss.WARDEN.boss!!.incrementTag("stun")
        CustomBoss.WARDEN.boss!!.setTag("stunned", true)
        CustomBoss.WARDEN.boss!!.setTag("delay1", -1)
        CustomBoss.WARDEN.boss!!.isInvulnerable = false
        for (player in CustomBoss.WARDEN.players) player.sendMessage(Utils.text("The boss is stunned!", Utils.GRAY))
        cancelTasks(CustomBoss.WARDEN)
    }

    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.player.world != CustomItems.bossWorld) return
        for (bar in e.player.activeBossBars()) {
            bar.removeViewer(e.player)
        }
        for (boss in CustomBoss.entries) {
            boss.bossBar.removePlayer(e.player)
        }
        e.player.gameMode = GameMode.SURVIVAL
    }

    fun witherSummon() {
        val wardenBoss = CustomItems.bossWorld.spawn(CustomBoss.WITHER.getCenter(), Wither::class.java)
        wardenBoss.setAttr(Attribute.MAX_HEALTH, 375.0)
        wardenBoss.health = 375.0
        wardenBoss.setAI(false)
        wardenBoss.setTag("bossid", CustomBoss.WITHER.id)
        wardenBoss.setTag("tick", Bukkit.getServer().currentTick)

        wardenBoss.setTag("delay1", 0)
        wardenBoss.setTag("delay2", 0)
        wardenBoss.setTag("mobdelay", 0)

        wardenBoss.setTag("currentattack", 1)
        wardenBoss.setTag("phase", 0)

        CustomBoss.WITHER.getCenter().block.type = Material.AIR
        CustomBoss.WITHER.bossBar.color = BarColor.RED
        CustomBoss.WITHER.bossBar.setTitle("The Wither")
        CustomBoss.WITHER.bossBar.progress = 1.0

        for (player in wardenBoss.location.getNearbyPlayers(20.0)) {
            CustomBoss.WITHER.bossBar.addPlayer(player)
        }
    }

    fun wardenSummon() {
        val wardenBoss = CustomItems.bossWorld.spawn(CustomBoss.WARDEN.getCenter(), Warden::class.java)
        wardenBoss.setAttr(Attribute.MAX_HEALTH, 250.0)
        wardenBoss.health = 250.0
        wardenBoss.setAI(false)
        wardenBoss.setTag("bossid", CustomBoss.WARDEN.id)
        wardenBoss.setTag("tick", Bukkit.getServer().currentTick)

        wardenBoss.setTag("delay1", 0)
        wardenBoss.setTag("delay2", 0)
        wardenBoss.setTag("mobdelay", 0)

        wardenBoss.setTag("currentattack", 1)
        wardenBoss.setTag("phase", 0)

        CustomBoss.WARDEN.getCenter().block.type = Material.AIR
        CustomBoss.WARDEN.bossBar.color = BarColor.BLUE
        CustomBoss.WARDEN.bossBar.setTitle("The Warden")
        CustomBoss.WARDEN.bossBar.progress = 1.0

        for (player in wardenBoss.location.getNearbyPlayers(20.0)) {
            CustomBoss.WARDEN.bossBar.addPlayer(player)
        }
    }
    private fun wardenStartPhase1() {
        CustomEffects.playSound(CustomBoss.WARDEN.getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 0.8F)
        val boss = CustomBoss.WARDEN.boss!!
        boss.setTag("phase", 1)
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        boss.setTag("delay1", 0)
        boss.setTag("delay2", 0)
        boss.setTag("mobdelay", 0)
        boss.isInvulnerable = true
        wardenStartAttack()
    }
    private fun wardenStartPhase2() {
        val boss = CustomBoss.WARDEN.boss!!
        boss.setTag("phase", 2)
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        CustomEffects.playSound(CustomBoss.WARDEN.getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 1.0F)
        boss.setTag("delay1", 10)
        boss.setTag("mobdelay", 0)
    }
    private fun possWardenSpawnLoc(): Location {
        val possLocs = mutableListOf<Location>()
        for (offset in arrayOf(Pair(8.0, 8.0), Pair(8.0, -8.0), Pair(-8.0, 8.0), Pair(-8.0, -8.0))) {
            val loc = CustomBoss.WARDEN.getLowerCenter().add(offset.first, 1.5, offset.second)
            if (loc.block.type == Material.SOUL_FIRE) possLocs.add(loc)
            //Bukkit.getLogger().info(loc.toString())
        }
        return possLocs.random()
    }
    private fun wardenStartPhase3() {
        CustomEffects.playSound(CustomBoss.WARDEN.getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 1.2F)
        val boss = CustomBoss.WARDEN.boss!!
        boss.setTag("phase", 3)
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        boss.setTag("delay1", 0)
        boss.setTag("mobdelay", 0)
        boss.setAI(true)
        //wardenStartAttack()
    }
    private fun wardenSpawnMobs() {
        val subphase = when (CustomBoss.WARDEN.hpPercent) {
            in 0.6..0.7 -> 3
            in 0.7..0.8 -> 2
            in 0.8..0.9 -> 1
            in 0.9..1.0 -> 0
            in 1.0..10.0 -> 0
            else -> 3
        }

        val loc = getValidSpawn(CustomBoss.WARDEN, 15.0)

        val random = Math.random()

        if (random < 0.3) {
            val zombie = loc.world.spawn(loc, Zombie::class.java)
            zombie.setAttr(Attribute.MAX_HEALTH, arrayOf(30.0, 33.0, 36.0, 39.0)[subphase])
            zombie.setAttr(Attribute.ATTACK_DAMAGE, arrayOf(10.0, 13.0, 16.0, 20.0)[subphase])
            zombie.health = arrayOf(30.0, 33.0, 36.0, 39.0)[subphase]
            zombie.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.28, 0.30, 0.32, 0.34)[subphase])
            zombie.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
        } else if (random < 0.6) {
            val skeleton = loc.world.spawn(loc, Skeleton::class.java)
            skeleton.setAttr(Attribute.MAX_HEALTH, arrayOf(30.0, 35.0, 40.0, 45.0)[subphase])
            skeleton.health = arrayOf(30.0, 35.0, 40.0, 45.0)[subphase]
            skeleton.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.28, 0.30, 0.32, 0.34)[subphase])
            skeleton.equipment.setItemInMainHand(ItemStack(Material.BOW).ench("PW${subphase+2}"))
            skeleton.equipment.itemInMainHandDropChance = 0.0F
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
        } else if (random < 0.8) {
            val zombie = loc.world.spawn(loc, Zombie::class.java)
            zombie.setAttr(Attribute.MAX_HEALTH, arrayOf(30.0, 33.0, 36.0, 39.0)[subphase])
            zombie.setAttr(Attribute.ATTACK_DAMAGE, arrayOf(8.0, 11.0, 14.0, 18.0)[subphase])
            zombie.health = arrayOf(30.0, 33.0, 36.0, 39.0)[subphase]
            zombie.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.28, 0.30, 0.32, 0.34)[subphase])
            zombie.equipment.setItemInMainHand(ItemStack(Material.IRON_SWORD).ench("KB${((subphase+1)/2)}"))
            zombie.equipment.itemInMainHandDropChance = 0.0F
            zombie.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
        } else {
            val skeleton = loc.world.spawn(loc, Skeleton::class.java)
            skeleton.setAttr(Attribute.MAX_HEALTH, arrayOf(30.0, 35.0, 40.0, 45.0)[subphase])
            skeleton.health = arrayOf(30.0, 35.0, 40.0, 45.0)[subphase]
            skeleton.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.28, 0.30, 0.32, 0.34)[subphase])
            skeleton.equipment.setItemInMainHand(ItemStack(Material.BOW).ench("PW${subphase+2}", "PU${((subphase+1)/2)}"))
            skeleton.equipment.itemInMainHandDropChance = 0.0F
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
        }

    }
    private fun wardenStartAttack() {
        val boss = CustomBoss.WARDEN.boss!!
        val phase = boss.getTag<Int>("phase")
        val random = Math.random()

        val subphase = when (CustomBoss.WARDEN.hpPercent) {
            in 0.0..0.7 -> 3
            in 0.7..0.8 -> 2
            in 0.8..0.9 -> 1
            in 0.9..1.0 -> 0
            else -> 0
        }

        val particleSettings = ParticleSettings(
            Particle.DUST.builder().data(DustOptions(Color.fromRGB(102, 226, 232), 1.0F)), 5,
            Particle.DUST.builder().data(DustOptions(Color.fromRGB(50, 117, 120), 1.0F))
        )
        val floorSettings = ParticleSettings(
            Particle.DUST.builder().data(DustOptions(Color.fromRGB(247, 2, 2), 3.0F)), 5,
            Particle.DUST.builder().data(DustOptions(Color.fromRGB(125, 1, 11), 3.0F))
        )

        when (phase) {
            1 -> {
                val currentCount = boss.getTag<Int>("delay2")!!
                boss.setTag("delay2", currentCount + 1)
                //predictive sonic boom attack
                if (random < 0.3 && currentCount < 6) {
                    //Bukkit.getLogger().info("sonic boom")
                    boss.setTag("delay1", arrayOf(15, 13, 12, 10)[subphase])

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(6.0, 7.0, 8.0, 10.0)[subphase], CustomDamageType.ALL_BYPASS,
                        damager = boss, knockback = CenterKnockback(CustomBoss.WARDEN.getCenter(), 3.0, Vector(0.0, 0.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_WARDEN_SONIC_BOOM
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(70, 60, 50, 40)[subphase])

                        for (player in CustomBoss.WARDEN.players) {

                            val newEnd = player.location.add(0.0, 1.4, 0.0).subtract(CustomBoss.WARDEN.getCenter().add(0.0, 1.0, 0.0)).toVector().normalize().multiply(25)

                            linearRoundAttack(CustomBoss.WARDEN.getCenter().add(0.0, 1.0, 0.0), CustomBoss.WARDEN.getCenter().add(newEnd), 1.5,
                                particleSettings, damageSettings, CustomBoss.WARDEN, delay = arrayOf(70, 60, 50, 40)[subphase]
                            )
                        }

                        i++
                        if (i == 3) this.cancel()

                    }}.runTaskTimer(CustomItems.plugin, arrayOf(30, 20, 30, 20)[subphase].toLong(), arrayOf(90, 80, 70, 60)[subphase].toLong()).taskId)
                }
                //random circle attack
                else if (random < 0.6 && currentCount < 6) {
                    //Bukkit.getLogger().info("random circle")
                    boss.setTag("delay1", arrayOf(18, 18, 15, 15)[subphase])

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(45.0, 52.0, 56.0, 60.0)[subphase], CustomDamageType.HOT_FLOOR,
                        damager = boss, knockback = ConstantKnockback(Vector(0.0, 1.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_LAVA_EXTINGUISH
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(100, 100, 80, 60)[subphase])

                        val center = CustomBoss.WARDEN.getLowerCenter()

                        val circles = mutableListOf<Circle>()
                        for (j in mutableListOf(Pair(1, 1), Pair(-1, 1), Pair(1, -1), Pair(-1, -1))) {
                            circles.add(Circle(arrayOf(3.0, 2.5, 2.5, 2.5)[subphase],
                                center.clone().add(Location(center.world, Utils.randomRange(0.0, 15.0)*j.first, 0.0, Utils.randomRange(0.0, 15.0)*j.second))))
                        }

                        var l = 0
                        futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                            for (safeArea in circles) {
                                val safeCenter = safeArea.center.clone()
                                CustomEffects.particleLine(particleSettings.particle, safeCenter, safeCenter.clone().add(0.0, 19.0, 0.0), 50)
                            }
                            l++
                            if (l == arrayOf(100, 100, 80, 60)[subphase]/5) this.cancel()

                        }}.runTaskTimer(CustomItems.plugin, 0L, 5L).taskId)

                        val polygon = NegativePolygon(201.0,
                            listOf(center.clone().add(15.0, 0.0, 15.0), center.clone().add(-15.0, 0.0, 15.0),
                                   center.clone().add(15.0, 0.0, -15.0), center.clone().add(-15.0, 0.0, -15.0)),
                            circles
                        )
                        planeAttack(polygon, 201.0, floorSettings, 0.4, damageSettings, CustomBoss.WARDEN, delay = arrayOf(100, 100, 80, 60)[subphase])

                        i++
                        if (i == arrayOf(3, 3, 3, 4)[subphase]) this.cancel()
                    }}.runTaskTimer(CustomItems.plugin, arrayOf(1, 1, 1, 1)[subphase].toLong(), arrayOf(110, 110, 90, 70)[subphase].toLong()).taskId)
                }
                //laser from each flame to nearest player
                else if (random < 0.8 && currentCount < 6) {
                    //Bukkit.getLogger().info("flame line")
                    boss.setTag("delay1", arrayOf(17, 15, 16, 15)[subphase])
                    val center = Location(CustomBoss.WARDEN.getCenter().world, CustomBoss.WARDEN.getCenter().x, CustomBoss.WARDEN.getYOffset(), CustomBoss.WARDEN.getCenter().z)
                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(42.0, 46.0, 50.0, 54.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(70, 60, 40, 30)[subphase])

                        for (x in arrayOf(8.5, -8.5)) for (z in arrayOf(8.5, -8.5)) {
                            val loc = center.clone().add(x, 2.0, z)
                            var currentClosest: Player = CustomBoss.WARDEN.players.first()
                            var closestDist = 50.0
                            for (player in CustomBoss.WARDEN.players) {
                                if (player.location.subtract(loc).length() < closestDist) {
                                    closestDist = player.location.subtract(loc).length()
                                    currentClosest = player
                                }
                            }

                            val newEnd = currentClosest.location.add(0.0, 1.0, 0.0).subtract(loc).toVector().normalize().multiply(33)

                            lineAttack(loc, loc.clone().add(newEnd), particleSettings, damageSettings, CustomBoss.WARDEN, delay = arrayOf(70, 60, 40, 30)[subphase])

                        }

                        i++
                        if (i == arrayOf(4, 4, 5, 7)[subphase]) this.cancel()
                    }}.runTaskTimer(CustomItems.plugin, arrayOf(10, 10, 20, 10)[subphase].toLong(), arrayOf(80, 70, 60, 40)[subphase].toLong()).taskId)
                }
                //square around flames, stun after
                else {
                    boss.setTag("delay1", -1)
                    //Bukkit.getLogger().info("final attack")
                    val center = Location(CustomBoss.WARDEN.getCenter().world, CustomBoss.WARDEN.getCenter().x, CustomBoss.WARDEN.getYOffset(), CustomBoss.WARDEN.getCenter().z)
                    val possOffset = mutableListOf(Pair(1, 1), Pair(-1, 1), Pair(1, -1), Pair(-1, -1))
                    val realOffsets = mutableListOf<Pair<Int, Int>>()
                    for (j in 0..<arrayOf(4, 3, 2, 1)[subphase]) {
                        val current = possOffset.random()
                        possOffset.remove(current)
                        realOffsets.add(current)
                    }
                    val negative = mutableListOf<Circle>()
                    for (offset in realOffsets) {
                        val currentCenter = center.clone().add(8.5 * offset.first, 0.0, 8.5 * offset.second)
                        negative.add(Circle(4.0, currentCenter))
                    }
                    val shape = NegativePolygon(
                        201.0,
                        listOf(center.clone().add(15.0, 0.0, 15.0), center.clone().add(-15.0, 0.0, 15.0),
                            center.clone().add(15.0, 0.0, -15.0), center.clone().add(-15.0, 0.0, -15.0)),
                        negative
                    )

                    val damageSettings = DamageSettings(
                        arrayOf(64.0, 66.0, 68.0, 70.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_BASALT_BREAK, 0.5F, 1.5F, 20, Sound.BLOCK_ANVIL_PLACE
                    )

                    boss.setTag("stun", subphase + 1)
                    boss.setTag("stunned", true)
                    boss.isInvulnerable = false

                    var l = 0
                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        for (safeArea in negative) {
                            val safeCenter = safeArea.center.clone()
                            CustomEffects.particleLine(particleSettings.particle, safeCenter, safeCenter.clone().add(0.0, 19.0, 0.0), 50)
                        }

                        l++
                        if (l == 100/5) {
                            for (player in CustomBoss.WARDEN.players) player.sendMessage(Utils.text("The boss is stunned!", Utils.GRAY))
                            this.cancel()
                        }
                    }}.runTaskTimer(CustomItems.plugin, 20L, 5L).taskId)

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, 100)
                        planeAttack(shape, 201.0, floorSettings, 0.4, damageSettings, CustomBoss.WARDEN, delay = 100)

                    }}.runTaskLater(CustomItems.plugin, 20L).taskId)
                }
            }
            2 -> {
                //predictive sonic boom attack
                if (random < 0.4) {
                    //Bukkit.getLogger().info("sonic boom")
                    boss.setTag("delay1", arrayOf(15, 13, 12, 10)[subphase] + 4)

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(6.0, 7.0, 8.0, 10.0)[subphase], CustomDamageType.ALL_BYPASS,
                        damager = boss, knockback = CenterKnockback(CustomBoss.WARDEN.getCenter(), 3.0, Vector(0.0, 0.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_WARDEN_SONIC_BOOM
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(70, 60, 50, 40)[subphase])

                        for (player in CustomBoss.WARDEN.players) {

                            val newEnd = player.location.add(0.0, 1.4, 0.0).subtract(CustomBoss.WARDEN.getCenter().add(0.0, 1.0, 0.0)).toVector().normalize().multiply(25)

                            linearRoundAttack(CustomBoss.WARDEN.getCenter().add(0.0, 1.0, 0.0), CustomBoss.WARDEN.getCenter().add(newEnd), 1.5,
                                particleSettings, damageSettings, CustomBoss.WARDEN, delay = arrayOf(70, 60, 50, 40)[subphase]
                            )
                        }

                        i++
                        if (i == 3) this.cancel()

                    }}.runTaskTimer(CustomItems.plugin, arrayOf(30, 20, 30, 20)[subphase].toLong(), arrayOf(90, 80, 70, 60)[subphase].toLong()).taskId)
                }
                //random circle attack
                else if (random < 0.6) {
                    //Bukkit.getLogger().info("random circle")
                    boss.setTag("delay1", arrayOf(18, 18, 15, 15)[subphase] + 4)

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(45.0, 52.0, 56.0, 60.0)[subphase], CustomDamageType.HOT_FLOOR,
                        damager = boss, knockback = ConstantKnockback(Vector(0.0, 1.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_LAVA_EXTINGUISH
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(100, 100, 80, 60)[subphase])

                        val center = CustomBoss.WARDEN.getLowerCenter()

                        val circles = mutableListOf<Circle>()
                        for (j in mutableListOf(Pair(1, 1), Pair(-1, 1), Pair(1, -1), Pair(-1, -1))) {
                            circles.add(Circle(arrayOf(3.0, 2.5, 2.5, 2.5)[subphase],
                                center.clone().add(Location(center.world, Utils.randomRange(0.0, 15.0)*j.first, 0.0, Utils.randomRange(0.0, 15.0)*j.second))))
                        }

                        var l = 0
                        futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                            for (safeArea in circles) {
                                val safeCenter = safeArea.center.clone()
                                CustomEffects.particleLine(particleSettings.particle, safeCenter, safeCenter.clone().add(0.0, 19.0, 0.0), 50)
                            }
                            l++
                            if (l == arrayOf(100, 100, 80, 60)[subphase]/5) this.cancel()

                        }}.runTaskTimer(CustomItems.plugin, 0L, 5L).taskId)

                        val polygon = NegativePolygon(201.0,
                            listOf(center.clone().add(15.0, 0.0, 15.0), center.clone().add(-15.0, 0.0, 15.0),
                                center.clone().add(15.0, 0.0, -15.0), center.clone().add(-15.0, 0.0, -15.0)),
                            circles
                        )
                        planeAttack(polygon, 201.0, floorSettings, 0.4, damageSettings, CustomBoss.WARDEN, delay = arrayOf(100, 100, 80, 60)[subphase])

                        i++
                        if (i == arrayOf(3, 3, 3, 4)[subphase]) this.cancel()
                    }}.runTaskTimer(CustomItems.plugin, arrayOf(1, 1, 1, 1)[subphase].toLong(), arrayOf(110, 110, 90, 70)[subphase].toLong()).taskId)
                }
                //laser from each flame to nearest player
                else  {
                    //Bukkit.getLogger().info("flame line")
                    boss.setTag("delay1", arrayOf(17, 15, 16, 15)[subphase] + 4)
                    val center = Location(CustomBoss.WARDEN.getCenter().world, CustomBoss.WARDEN.getCenter().x, CustomBoss.WARDEN.getYOffset(), CustomBoss.WARDEN.getCenter().z)
                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(42.0, 46.0, 50.0, 54.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT
                    )

                    futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {

                        playReapeatingSound(CustomBoss.WARDEN, soundSettings, arrayOf(70, 60, 40, 30)[subphase])

                        for (x in arrayOf(8.5, -8.5)) for (z in arrayOf(8.5, -8.5)) {
                            val loc = center.clone().add(x, 2.0, z)
                            var currentClosest: Player = CustomBoss.WARDEN.players.first()
                            var closestDist = 50.0
                            for (player in CustomBoss.WARDEN.players) {
                                if (player.location.subtract(loc).length() < closestDist) {
                                    closestDist = player.location.subtract(loc).length()
                                    currentClosest = player
                                }
                            }

                            val newEnd = currentClosest.location.add(0.0, 1.0, 0.0).subtract(loc).toVector().normalize().multiply(33)

                            lineAttack(loc, loc.clone().add(newEnd), particleSettings, damageSettings, CustomBoss.WARDEN, delay = arrayOf(70, 60, 40, 30)[subphase])

                        }

                        i++
                        if (i == arrayOf(4, 4, 5, 7)[subphase]) this.cancel()
                    }}.runTaskTimer(CustomItems.plugin, arrayOf(10, 10, 20, 10)[subphase].toLong(), arrayOf(80, 70, 60, 40)[subphase].toLong()).taskId)
                }
            }
            3 -> {

            }
        }
    }

    private fun endBosses(boss: CustomBoss? = null) {
        if (boss == null) {
            cancelTasks()
            for (customBoss in CustomBoss.entries) {
                customBoss.boss?.remove()
                customBoss.getCenter().block.type = Material.BEDROCK
                for (player in customBoss.getCenter().getNearbyPlayers(25.0)) {
                    player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
                }
                for (entity in customBoss.getCenter().getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (entity is LivingEntity && entity !is Player) entity.remove()
                }
                customBoss.bossBar.removeAll()

                if (customBoss == CustomBoss.WARDEN) {
                    for (offset in arrayOf(Pair(1.0, 1.0), Pair(1.0, -1.0), Pair(-1.0, 1.0), Pair(-1.0, -1.0))) {
                        for (x in 8..9) for (z in 8..9) {
                            customBoss.getLowerCenter().add(x * offset.first, 1.0, z * offset.second).block.type = Material.SOUL_FIRE
                        }
                    }
                }
            }
            for (player in CustomItems.bossWorld.players) {
                player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
            }
        } else {
            cancelTasks(boss)
            for (player in boss.getCenter().getNearbyPlayers(25.0)) {
                player.teleport(player.respawnLocation ?: Bukkit.getWorlds()[0].spawnLocation)
            }
            boss.boss?.remove()
            boss.getCenter().block.type = Material.BEDROCK
            for (entity in boss.getCenter().getNearbyEntities(20.0, 20.0, 20.0)) {
                if (entity is LivingEntity && entity !is Player) entity.remove()
            }
            boss.bossBar.removeAll()
            if (boss == CustomBoss.WARDEN) {
                for (offset in arrayOf(Pair(1.0, 1.0), Pair(1.0, -1.0), Pair(-1.0, 1.0), Pair(-1.0, -1.0))) {
                    for (x in 8..9) for (z in 8..9) {
                        boss.getLowerCenter().add(x * offset.first, 1.0, z * offset.second).block.type = Material.SOUL_FIRE
                    }
                }
            }
        }
    }

    private fun lineAttack(start: Location, end: Location, particleSettings: ParticleSettings, damage: DamageSettings, boss: CustomBoss, delay: Int = 0, duration: Int = 0) {

        val direction = end.clone().subtract(start)
        val length = direction.length()
        val unit = direction.toVector().normalize().multiply(0.1)
        val newLoc = start.clone()

        var k = delay / particleSettings.preParticleSeparation

        futures[FutureType.PARTICLE]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            CustomEffects.particleLine(particleSettings.preParticle, start, end, (length * 10).toInt())

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)


        if (duration == 0) {
            futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {

                val toDamage = mutableSetOf<UUID>()

                for (i in 0..(length/0.1).toInt()) {
                    for (player in newLoc.getNearbyEntitiesByType(Player::class.java, 3.0)) {
                        if (player.boundingBox.containsLoc(newLoc, player.world)) {
                            toDamage.add(player.uniqueId)
                        }
                    }

                    for (uuid in toDamage) {
                        Bukkit.getServer().getPlayer(uuid)?.applyDamage(damage)
                    }

                    if (i%4 == 0) {
                        CustomEffects.particle(particleSettings.particle, newLoc, 1)
                    }
                    newLoc.add(unit)
                }
            }}.runTaskLater(CustomItems.plugin, delay.toLong()).taskId)
        } else if (duration > 0) {
            val incPerTick = length/0.1 / duration
            val incPerDuration: Double
            var singleDuration = 1
            if (incPerTick < 1) {
                val ticksPerInc = 1 / incPerTick
                val tickInc = ticksPerInc.round(0).toInt()
                singleDuration = tickInc
                incPerDuration = incPerTick * tickInc
            } else {
                incPerDuration = incPerTick
            }

            var i = 0
            futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
                for (j in 0..<incPerDuration.randomToInt()) {
                    for (player in newLoc.getNearbyEntitiesByType(Player::class.java, 3.0)) {
                        var hitPlayer = false
                        if (player.boundingBox.containsLoc(newLoc, player.world)) {
                            hitPlayer = true
                        }
                        if (hitPlayer) {
                            player.applyDamage(damage)
                        }
                    }

                    if (i%4 == 0) {
                        CustomEffects.particle(particleSettings.particle, newLoc, 1)
                    }
                    newLoc.add(unit)

                    i++
                    if (i >= (length/0.1).toInt()) this.cancel()
                }
            }}.runTaskTimer(CustomItems.plugin, delay.toLong(), singleDuration.toLong()).taskId)

        }
    }
    private fun arcAttack(origin: Location, radius: Double, totalDegrees: Double, direction: Vector, particleSettings: ParticleSettings, damage: DamageSettings, boss: CustomBoss, possRandomOffset: Double = 0.0, delay: Int = 0) {


        var k = delay / particleSettings.preParticleSeparation

        futures[FutureType.PARTICLE]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            CustomEffects.rotatedArc(particleSettings.preParticle, origin, radius, totalDegrees, (Math.PI * radius.pow(2) * (totalDegrees/360.0) * 50).toInt(), direction, possRandomOffset)

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)

        futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {

            val toDamage = mutableSetOf<UUID>()

            for (i in 0..totalDegrees.toInt()) {
                val currentDegree = -totalDegrees / 2 + i
                val currentRad = Math.toRadians(currentDegree)
                val vect = Vector(cos(currentRad), 0.0, sin(currentRad)).rotateToAxis(direction)
                val unit = vect.normalize().multiply(0.1)

                val currentLoc = origin.clone()
                for (j in 0..(radius * 10).toInt()) {
                    currentLoc.add(unit)
                    for (player in currentLoc.getNearbyEntitiesByType(Player::class.java, 2.0)) {
                        if (player.boundingBox.containsLoc(currentLoc, player.world)) {
                            toDamage.add(player.uniqueId)
                        }
                    }
                }
            }

            for (player in toDamage) {
                Bukkit.getPlayer(player)?.applyDamage(damage)
            }

            CustomEffects.rotatedArc(particleSettings.particle, origin, radius, totalDegrees, (Math.PI * radius.pow(2) * (totalDegrees/360.0) * 50).toInt(), direction, possRandomOffset)
        }}.runTaskLater(CustomItems.plugin, delay.toLong()).taskId)
    }
    private fun planeAttack(shape: Shape, yLevel: Double, particleSettings: ParticleSettings, concentration: Double, damage: DamageSettings, boss: CustomBoss, above: Boolean = true, delay: Int = 0) {
        if (shape is NegativePolygon) {
            //Bukkit.getLogger().info(shape.area.toString())
            //Bukkit.getLogger().info(shape.boundingArea.toString())
        }
        var k = delay / particleSettings.preParticleSeparation

        futures[FutureType.PARTICLE]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            //var count = 0

            for (i in 0..(concentration * shape.area).toInt()) {
                CustomEffects.particle(particleSettings.preParticle, shape.randomPoint(), 1)
                //count++
                //Bukkit.getLogger().info("Total count is: $count")
            }
            for (point in shape.linePoints(2 * sqrt(concentration))) {
                CustomEffects.particle(particleSettings.preParticle, point, 1)
            }

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)

        futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
            for (player in shape.center.getNearbyEntitiesByType(Player::class.java, shape.xRadius + 2.0, 20.0, shape.zRadius + 2.0)) {
                var doDamage = false
                for (corner in player.getHitboxCorners(true)) {
                    if (!shape.contains(corner)) continue

                    if ((!above && player.y == yLevel) || (above && player.y >= yLevel)) {
                        doDamage = true
                    }
                }
                if (doDamage) player.applyDamage(damage)
            }

            for (i in 0..(concentration * shape.area).toInt()) {
                CustomEffects.particle(particleSettings.particle, shape.randomPoint(), 1)
            }
            for (point in shape.linePoints(2 * sqrt(concentration))) {
                CustomEffects.particle(particleSettings.particle, point, 1)
            }

        }}.runTaskLater(CustomItems.plugin, delay.toLong()).taskId)
    }
    private fun linearRoundAttack(start: Location, end: Location, radius: Double, particleSettings: ParticleSettings, damage: DamageSettings, boss: CustomBoss, delay: Int = 0, duration: Int = 0) {

        val direction = end.subtract(start)
        val length = direction.length()
        val unit = direction.toVector().normalize().multiply(0.1)

        var k = delay / particleSettings.preParticleSeparation

        futures[FutureType.PARTICLE]!![boss]!!.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            val newLoc = start.clone()

            for (i in 0..(length/0.1).toInt()) {
                CustomEffects.rotatedParticleCircle(particleSettings.preParticle, newLoc, radius, (3.1 * radius).toInt(), unit)
                newLoc.add(unit)
            }

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)


        if (duration == 0) {
            futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {

                val newLoc = start.clone()

                for (i in 0..(length/0.1).toInt()) {
                    for (player in newLoc.getNearbyEntitiesByType(Player::class.java, radius + 3)) {
                        var hitPlayer = false
                        for (corner in player.getHitboxCorners()) {
                            if (corner.subtract(newLoc).length() < radius) {
                                hitPlayer = true
                                break
                            }
                        }
                        if (hitPlayer) {
                            player.applyDamage(damage)
                        }
                    }

                    CustomEffects.rotatedParticleCircle(particleSettings.particle, newLoc, radius, (3.1 * radius).toInt(), unit)
                    newLoc.add(unit)
                }
            }}.runTaskLater(CustomItems.plugin, delay.toLong()).taskId)
        } else if (duration > 0) {
            val incPerTick = length/0.1 / duration
            var incPerDuration = 0.0
            var singleDuration = 1
            if (incPerTick < 1) {
                val ticksPerInc = 1 / incPerTick
                val tickInc = ticksPerInc.round(0).toInt()
                singleDuration = tickInc
                incPerDuration = incPerTick * tickInc
            } else {
                incPerDuration = incPerTick
            }

            var i = 0
            futures[FutureType.ATTACK]!![boss]!!.add(object : BukkitRunnable() { override fun run() {

                val newLoc = start.clone()

                for (j in 0..<incPerDuration.randomToInt()) {

                    for (player in newLoc.getNearbyEntitiesByType(Player::class.java, radius + 3)) {
                        var hitPlayer = false
                        for (corner in player.getHitboxCorners()) {
                            if (corner.subtract(newLoc).length() < radius) {
                                hitPlayer = true
                                break
                            }
                        }
                        if (hitPlayer) {
                            player.applyDamage(damage)
                        }
                    }

                    CustomEffects.rotatedParticleCircle(particleSettings.particle, newLoc, radius, (3.1 * radius).toInt(), unit)
                    newLoc.add(unit)

                    i++
                    if (i >= (length/0.1).toInt()) this.cancel()
                }
            }}.runTaskTimer(CustomItems.plugin, delay.toLong(), singleDuration.toLong()).taskId)

        }
    }

    private fun getValidSpawn(boss: CustomBoss, radius: Double): Location {
        val center = boss.getLowerCenter()
        var possOffset = Vector(Utils.randomRange(-radius, radius), 0.0, Utils.randomRange(-radius, radius))
        while (center.clone().add(possOffset).block.type != Material.AIR ||
               center.clone().add(possOffset).add(0.0, 1.0, 0.0).block.type != Material.AIR ||
               center.clone().add(possOffset).getNearbyPlayers(5.0).isNotEmpty()
        ) {
            possOffset = Vector(Utils.randomRange(-radius, radius), 0.0, Utils.randomRange(-radius, radius))
        }
        return center.clone().add(possOffset)
    }
    private fun cancelTasks(boss: CustomBoss? = null, type: FutureType? = null) {
        if (type == null && boss == null) {
            for (map in futures.values) for (list in map.values) { for (task in list) {
                    Bukkit.getScheduler().cancelTask(task)
                }
                list.clear()
            }
        } else if (type == null) {
            for (map in futures.values) {
                for (task in map[boss]!!) {
                    Bukkit.getScheduler().cancelTask(task)
                }
                map[boss]!!.clear()
            }
        } else {
            for (task in futures[type]!![boss]!!) {
                Bukkit.getScheduler().cancelTask(task)
            }
            futures[type]!![boss]!!.clear()
        }
    }
    private fun playReapeatingSound(boss: CustomBoss, soundSettings: SoundSettings, delay: Int) {
        var s = 0
        val period = delay / soundSettings.steps

        futures[FutureType.SOUND]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {
            s++

            if (s == soundSettings.steps) {
                CustomEffects.playSound(boss.getCenter(), soundSettings.postSound, 3.0F, 1.0F, random = false)
            } else {
                CustomEffects.playSound(boss.getCenter(), soundSettings.preSound, 3.0F, soundSettings.getPitch(s), random = false)
            }

            if (s == soundSettings.steps) this.cancel()
        }}.runTaskTimer(CustomItems.plugin, 1L, period.toLong()).taskId)
    }

    private val futures = mutableMapOf(
        Pair(FutureType.ATTACK, mutableMapOf(
            Pair(CustomBoss.WARDEN, mutableListOf<Int>()),
            Pair(CustomBoss.WITHER, mutableListOf()),
            Pair(CustomBoss.HUSK, mutableListOf()),
            Pair(CustomBoss.GUARDIAN, mutableListOf()),
            Pair(CustomBoss.PIGLIN, mutableListOf()),
        )),
        Pair(FutureType.PARTICLE, mutableMapOf(
            Pair(CustomBoss.WARDEN, mutableListOf()),
            Pair(CustomBoss.WITHER, mutableListOf()),
            Pair(CustomBoss.HUSK, mutableListOf()),
            Pair(CustomBoss.GUARDIAN, mutableListOf()),
            Pair(CustomBoss.PIGLIN, mutableListOf()),
        )),
        Pair(FutureType.SOUND, mutableMapOf(
            Pair(CustomBoss.WARDEN, mutableListOf()),
            Pair(CustomBoss.WITHER, mutableListOf()),
            Pair(CustomBoss.HUSK, mutableListOf()),
            Pair(CustomBoss.GUARDIAN, mutableListOf()),
            Pair(CustomBoss.PIGLIN, mutableListOf()),
        )),
    )

    enum class FutureType {
        ATTACK,
        PARTICLE,
        SOUND,
    }

    private var taskFuture: BukkitTask? = null
    private var counter: Int = 0
    override fun run() {
        taskFuture = Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
            counter = if (counter == 2400) 0 else counter + 1
            if (counter % 20 == 0) {
                //check if all players are dead
                for (boss in CustomBoss.entries) {
                    if (!boss.isAlive()) continue
                    var alive = false
                    for (player in boss.players) {
                        if (!player.isDead) {alive = true; break}
                    }
                    if (alive) continue

                    endBosses(boss)
                }

                //check if any players escaped arena
                for (player in CustomItems.bossWorld.players) {
                    var inBoss = false
                    for (boss in CustomBoss.entries) {
                        if (player in boss.players) {
                            inBoss = true
                        }
                    }
                    if (!inBoss && player.health != 0.0) {
                        player.health = 0.0
                    }
                }

                if (CustomBoss.WARDEN.isAlive()) {
                    val boss = CustomBoss.WARDEN.boss!!
                    boss.decrementTag("delay1")
                    boss.decrementTag("mobdelay")

                    if (boss.getTag<Int>("delay1") == 0) {
                        wardenStartAttack()
                    }
                    if (boss.getTag<Int>("mobdelay") == 0 && ((boss.getTag<Int>("phase") ?: 0) == 1 || (boss.getTag<Int>("phase") ?: 0) == 3)) {
                        wardenSpawnMobs()
                        boss.setTag("mobdelay", (15 / CustomBoss.WARDEN.players.size.toDouble().pow(0.6)).toInt())
                    }

                    if (boss.getTag<Boolean>("stunned") == true && (1.0 - (boss.getTag<Int>("stun") ?: 0) * 0.1) > CustomBoss.WARDEN.hpPercent && (boss.getTag<Int>("phase") ?: 0) < 3) {
                        boss.setTag("stunned", false)
                        boss.setTag("delay1", 0)
                        boss.setTag("delay2", 0)
                        boss.isInvulnerable = true

                        if (boss.getTag<Int>("phase") == 2) {
                            futures[FutureType.ATTACK]!![CustomBoss.WARDEN]!!.add(object : BukkitRunnable() { override fun run() {
                                val loc = possWardenSpawnLoc()
                                for (x in -1..1) for (z in -1..1) {
                                    loc.clone().add(x.toDouble(), 0.0, z.toDouble()).block.type = Material.AIR
                                }
                                loc.add(0.5, 0.0, 0.5)
                                CustomEffects.playSound(loc, Sound.ENTITY_WARDEN_EMERGE, 3.0F, 1.0F)
                                CustomEffects.particleCloud(Particle.SONIC_BOOM.builder(), loc.clone().add(0.0, 1.5, 0.0), 40, 2.0, 0.0)
                                val warden = CustomItems.bossWorld.spawn(loc, Warden::class.java)
                                //warden.getAttribute(Attribute.ATTACK_DAMAGE)!!.baseValue *= 1.1
                                val subPhase = boss.getTag<Int>("stun")!! - 2
                                warden.getAttribute(Attribute.MAX_HEALTH)!!.baseValue *= 0.4 + (0.08 * subPhase)
                                warden.health *= 0.4 + (0.08 * subPhase)
                                warden.getAttribute(Attribute.MOVEMENT_SPEED)!!.baseValue *= 0.7 + (0.08 * subPhase)
                                warden.setTag("id", CustomEntity.BOSS_SPAWNED_WARDEN.id)
                            }}.runTaskLater(CustomItems.plugin, 40L).taskId)
                        }
                    }

                }
            }

            if (counter % 10 == 0) {
                if (CustomBoss.WARDEN.isAlive()) {
                    val boss = CustomBoss.WARDEN.boss!!

                    val subphase = when (CustomBoss.WARDEN.hpPercent) {
                        in 0.6..0.7 -> 3
                        in 0.7..0.8 -> 2
                        in 0.8..0.9 -> 1
                        in 0.9..1.0 -> 0
                        else -> 0
                    }

                    val damageSettings = DamageSettings(
                        arrayOf(28.0, 30.0, 32.0, 35.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss, CenterKnockback(CustomBoss.WARDEN.getCenter(), 2.5)
                    )

                    if ((1.0 - (boss.getTag<Int>("stun") ?: 0) * 0.1) > CustomBoss.WARDEN.hpPercent) {
                        planeAttack(
                            Circle(5.0, CustomBoss.WARDEN.getLowerCenter()), CustomBoss.WARDEN.getLowerCenter().y,
                            ParticleSettings(
                                Particle.DUST.builder().data(DustOptions(Color.fromRGB(102, 226, 232), 1.0F)), 5,
                                Particle.DUST.builder().data(DustOptions(Color.fromRGB(50, 117, 120), 1.0F))
                            ),
                            0.8, damageSettings, CustomBoss.WARDEN, delay = 10
                        )
                    }
                }
            }


        }, 1, 1)
    }

    fun cancelAll() {
        endBosses()
        taskFuture!!.cancel()
    }
}