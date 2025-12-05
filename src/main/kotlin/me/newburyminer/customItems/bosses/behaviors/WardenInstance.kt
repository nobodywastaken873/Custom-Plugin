package me.newburyminer.customItems.bosses.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getHitboxCorners
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.randomToInt
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setAttr
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.bosses.CustomBossType
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.bosses.WardenBossComponent
import me.newburyminer.customItems.entity.components.bosses.WardenMinibossComponent
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.RandomSelector
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.damage.CenterKnockback
import me.newburyminer.customItems.helpers.damage.ConstantKnockback
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.helpers.shapes.Circle
import me.newburyminer.customItems.helpers.shapes.NegativePolygon
import me.newburyminer.customItems.helpers.shapes.Shape
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.structures.CustomLootTable
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Warden
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTables
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class WardenInstance(players: MutableList<Player>): BossInstance(players, CustomBossType.WARDEN) {

    override val loot: CustomLootTable = CustomLootTable(RandomSelector(
        Pair(ItemStack(Material.AIR), 7),
        Pair(ItemStack(Material.AIR), 1)
    ), 2..2)
    override fun giveLoot(lootPlayers: List<Player>) {
        super.giveLoot(lootPlayers)

        val newTable = CustomLootTable(RandomSelector(
            Pair(ItemRegistry.get(CustomItem.FRAGMENT_OF_SOUND), 7),
            Pair(ItemRegistry.get(CustomItem.WARDEN_HEART), 1)
        ), 2..2)

        lootPlayers.forEach { player ->
            val rolls = newTable.roll()
            var currentPity = player.getTag<Int>("withoutheartcount") ?: 0

            if (rolls.any { it.getCustom() == CustomItem.WARDEN_HEART })
                currentPity = 0
            else
                currentPity++

            if (currentPity == 4) {
                currentPity = 0
                player.addItemorDrop(ItemRegistry.get(CustomItem.WARDEN_HEART))
            }

            rolls.forEach {
                player.addItemorDrop(it)
            }

            player.sendMessage(Utils.text("Current pity count: ${currentPity}", Utils.GRAY))
            player.setTag("withoutheartcount", currentPity)

        }

        lootPlayers.forEach {
            val lootV2 = mutableListOf<ItemStack>()
            for (i in 0..20) {
                val lootContext = LootContext.Builder(it.location).build()
                val chestLoot = LootTables.ANCIENT_CITY.lootTable.populateLoot(null, lootContext)
                lootV2.addAll(chestLoot)
            }
            for (item in lootV2) {
                it.addItemorDrop(item)
            }
        }
    }
    override val bossCenter: Location = Location(CustomItems.bossWorld, -58.5, 203.0, -15.5)

    init {
        boss = spawnBoss()
        EntityWrapperManager.getWrapperorNew(boss).addComponent(WardenBossComponent(this))
    }

    override fun setupBossbar() {
        bossBar.color = BarColor.BLUE
        bossBar.setTitle("The Warden")
        bossBar.progress = 1.0
    }
    override fun spawnBoss(): LivingEntity {
        val warden = getCenter().world.spawn(bossCenter, Warden::class.java) {
            it.setAttr(Attribute.MAX_HEALTH, 250.0)
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

    private var phase: Int = 0
    fun getCurrentPhase(): Int = phase

    private var stunCounter: Int = 0
    private var isStunned = false
    fun getCurrentStunCounter(): Int = stunCounter
    fun stun() {
        boss.isInvulnerable = false
        attackDelay = -1
        isStunned = true
        stunCounter++
        for (player in players) player.sendMessage(Utils.text("The boss is stunned!", Utils.GRAY))
    }

    private var attackDelay = -1
    private var attackCount = 0
    private var mobDelay = -1
    private var hasUsedMobWave = false

    fun startPhase1() {
        CustomEffects.playSound(getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 0.8F)
        phase = 1
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        attackDelay = 0
        attackCount = 0
        mobDelay = 0
        boss.isInvulnerable = true
    }
    fun startPhase2() {
        CustomEffects.playSound(getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 1.0F)
        phase = 2
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        attackDelay = 10
        mobDelay = -1
    }
    fun startPhase3() {
        CustomEffects.playSound(getCenter(), Sound.ENTITY_WARDEN_ROAR, 3.0F, 1.2F)
        phase = 3
        for (player in boss.location.getNearbyPlayers(6.0)) {
            player.velocity = player.location.subtract(boss.location).toVector().normalize().add(Vector(0.0, 0.2, 0.0)).multiply(3.0)
        }
        attackDelay = -1
        mobDelay = 0
        boss.setAI(true)
    }
    private fun wardenSpawnMobs() {
        val subphase = when (hpPercent) {
            in 0.6..0.7 -> 3
            in 0.7..0.8 -> 2
            in 0.8..0.9 -> 1
            in 0.9..1.0 -> 0
            in 1.0..10.0 -> 0
            else -> 3
        }

        val loc = getValidMobSpawn()

        val random1 = Math.random()

        if (random1 < 0.5) {
            val zombie = loc.world.spawn(loc, Zombie::class.java)
            zombie.setAttr(Attribute.MAX_HEALTH, arrayOf(25.0, 28.0, 31.0, 34.0)[subphase])
            zombie.setAttr(Attribute.ATTACK_DAMAGE, arrayOf(11.0, 14.0, 17.0, 20.0)[subphase])
            zombie.health = arrayOf(25.0, 28.0, 31.0, 34.0)[subphase]
            zombie.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.26, 0.28, 0.30, 0.32)[subphase])
            zombie.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
            activeEntities.add(zombie)
        }

        else {
            val skeleton = loc.world.spawn(loc, Skeleton::class.java)
            skeleton.setAttr(Attribute.MAX_HEALTH, arrayOf(25.0, 28.0, 31.0, 34.0)[subphase])
            skeleton.health = arrayOf(25.0, 28.0, 31.0, 34.0)[subphase]
            skeleton.setAttr(Attribute.MOVEMENT_SPEED, arrayOf(0.28, 0.30, 0.32, 0.34)[subphase])
            skeleton.equipment.setItemInMainHand(ItemStack(Material.BOW).ench("PW${subphase+2}"))
            skeleton.equipment.itemInMainHandDropChance = 0.0F
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, true, false))
            activeEntities.add(skeleton)
        }

    }
    private fun possWardenSpawnLoc(): Location {
        val possLocs = mutableListOf<Location>()
        for (offset in arrayOf(Pair(8.0, 8.0), Pair(8.0, -8.0), Pair(-8.0, 8.0), Pair(-8.0, -8.0))) {
            val loc = getLowerCenter().add(offset.first, 1.5, offset.second)
            if (loc.block.type == Material.SOUL_FIRE) possLocs.add(loc)
            //Bukkit.getLogger().info(loc.toString())
        }
        return possLocs.random()
    }
    private fun wardenStartAttack() {
        val subphase = when (hpPercent) {
            in 0.0..0.7 -> 3
            in 0.7..0.8 -> 2
            in 0.8..0.9 -> 1
            in 0.9..1.0 -> 0
            else -> 0
        }
        val random = Math.random()

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
                attackCount++
                //predictive sonic boom attack
                if (random < 0.33 && attackCount < 6) {
                    //Bukkit.getLogger().info("sonic boom")
                    attackDelay =  arrayOf(15, 13, 12, 10)[subphase]

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(7.0, 8.0, 9.0, 11.0)[subphase],
                        CustomDamageType.ALL_BYPASS,
                        damager = boss,
                        knockback = CenterKnockback(getCenter(), 3.0, Vector(0.0, 0.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_WARDEN_SONIC_BOOM
                    )

                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                playReapeatingSound(soundSettings, arrayOf(70, 60, 50, 40)[subphase])

                                for (player in players) {

                                    val newEnd = player.location.add(0.0, 1.4, 0.0)
                                        .subtract(getCenter().add(0.0, 1.0, 0.0)).toVector()
                                        .normalize()
                                        .multiply(25)

                                    linearRoundAttack(
                                        getCenter().add(0.0, 1.0, 0.0),
                                        getCenter().add(newEnd),
                                        1.5,
                                        particleSettings,
                                        damageSettings,
                                        delay = arrayOf(70, 60, 50, 40)[subphase]
                                    )
                                }

                                i++
                                if (i == 3) this.cancel()

                            }
                        }.runTaskTimer(
                            CustomItems.plugin,
                            arrayOf(30, 20, 30, 20)[subphase].toLong(),
                            arrayOf(90, 80, 70, 60)[subphase].toLong()
                        ).taskId
                    )
                }
                //laser from each flame to nearest player
                else if (random < 0.66 && attackCount < 6) {
                    //Bukkit.getLogger().info("flame line")
                    attackDelay = arrayOf(17, 15, 16, 15)[subphase]
                    val center = getLowerCenter()
                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(42.0, 46.0, 50.0, 54.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT
                    )

                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                playReapeatingSound(soundSettings, arrayOf(70, 60, 40, 30)[subphase])

                                for (x in arrayOf(8.5, -8.5)) for (z in arrayOf(8.5, -8.5)) {
                                    val loc = center.clone().add(x, 2.0, z)
                                    var currentClosest: Player = players.first()
                                    var closestDist = 50.0
                                    for (player in players) {
                                        if (player.location.subtract(loc).length() < closestDist) {
                                            closestDist = player.location.subtract(loc).length()
                                            currentClosest = player
                                        }
                                    }

                                    val newEnd =
                                        currentClosest.location.add(0.0, 1.0, 0.0).subtract(loc).toVector().normalize()
                                            .multiply(33)

                                    lineAttack(
                                        loc,
                                        loc.clone().add(newEnd),
                                        particleSettings,
                                        damageSettings,
                                        delay = arrayOf(70, 60, 40, 30)[subphase]
                                    )

                                }

                                i++
                                if (i == arrayOf(4, 4, 5, 7)[subphase]) this.cancel()
                            }
                        }.runTaskTimer(
                            CustomItems.plugin,
                            arrayOf(10, 10, 20, 10)[subphase].toLong(),
                            arrayOf(80, 70, 60, 40)[subphase].toLong()
                        ).taskId
                    )
                }
                //random circle attack
                else if (attackCount < 6 && attackCount <= 3) {
                    //Bukkit.getLogger().info("random circle")
                    attackDelay =  arrayOf(18, 18, 15, 15)[subphase]

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(45.0, 52.0, 56.0, 60.0)[subphase], CustomDamageType.HOT_FLOOR,
                        damager = boss, knockback = ConstantKnockback(Vector(0.0, 1.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_BAMBOO_WOOD_BREAK, 0.5F, 1.5F, 10, Sound.BLOCK_LAVA_EXTINGUISH
                    )

                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                playReapeatingSound(
                                    soundSettings,
                                    arrayOf(100, 100, 80, 60)[subphase]
                                )

                                val center = getLowerCenter()

                                val circles = mutableListOf<Circle>()
                                for (j in mutableListOf(Pair(1, 1), Pair(-1, 1), Pair(1, -1), Pair(-1, -1))) {
                                    circles.add(
                                        Circle(
                                            arrayOf(3.0, 2.5, 2.5, 2.5)[subphase],
                                            center.clone().add(
                                                Location(
                                                    center.world,
                                                    Utils.randomRange(0.0, 15.0) * j.first,
                                                    0.0,
                                                    Utils.randomRange(0.0, 15.0) * j.second
                                                )
                                            )
                                        )
                                    )
                                }

                                var l = 0
                                tasks.add(object : BukkitRunnable() {
                                    override fun run() {

                                        for (safeArea in circles) {
                                            val safeCenter = safeArea.center.clone()
                                            CustomEffects.particleLine(
                                                particleSettings.particle,
                                                safeCenter,
                                                safeCenter.clone().add(0.0, 19.0, 0.0),
                                                50
                                            )
                                        }
                                        l++
                                        if (l == arrayOf(100, 100, 80, 60)[subphase] / 5) this.cancel()

                                    }
                                }.runTaskTimer(CustomItems.plugin, 0L, 5L).taskId)

                                val polygon = NegativePolygon(
                                    201.0,
                                    listOf(
                                        center.clone().add(15.0, 0.0, 15.0), center.clone().add(-15.0, 0.0, 15.0),
                                        center.clone().add(15.0, 0.0, -15.0), center.clone().add(-15.0, 0.0, -15.0)
                                    ),
                                    circles
                                )
                                planeAttack(
                                    polygon,
                                    201.0,
                                    floorSettings,
                                    0.15,
                                    damageSettings,
                                    delay = arrayOf(100, 100, 80, 60)[subphase]
                                )

                                i++
                                if (i == arrayOf(3, 3, 3, 4)[subphase]) this.cancel()
                            }
                        }.runTaskTimer(
                            CustomItems.plugin,
                            arrayOf(1, 1, 1, 1)[subphase].toLong(),
                            arrayOf(110, 110, 90, 70)[subphase].toLong()
                        ).taskId
                    )
                }
                //square around flames, stun after
                else {
                    attackDelay = -1
                    //Bukkit.getLogger().info("final attack")
                    val center = getLowerCenter()
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
                        listOf(
                            center.clone().add(15.0, 0.0, 15.0), center.clone().add(-15.0, 0.0, 15.0),
                            center.clone().add(15.0, 0.0, -15.0), center.clone().add(-15.0, 0.0, -15.0)
                        ),
                        negative
                    )

                    val damageSettings = DamageSettings(
                        arrayOf(64.0, 66.0, 68.0, 70.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_BASALT_BREAK, 0.5F, 1.5F, 20, Sound.BLOCK_ANVIL_PLACE
                    )

                    var l = 0
                    tasks.add(object : BukkitRunnable() {
                        override fun run() {

                            for (safeArea in negative) {
                                val safeCenter = safeArea.center.clone()
                                CustomEffects.particleLine(
                                    particleSettings.particle,
                                    safeCenter,
                                    safeCenter.clone().add(0.0, 19.0, 0.0),
                                    50
                                )
                            }

                            l++
                            if (l == 100 / 5) {
                                this.cancel()
                            }
                        }
                    }.runTaskTimer(CustomItems.plugin, 20L, 5L).taskId)

                    tasks.add(object : BukkitRunnable() {
                        override fun run() {

                            playReapeatingSound(soundSettings, 100)
                            planeAttack(
                                shape,
                                201.0,
                                floorSettings,
                                0.15,
                                damageSettings,
                                delay = 100
                            )

                        }
                    }.runTaskLater(CustomItems.plugin, 20L).taskId)

                    tasks.add(object : BukkitRunnable() {
                        override fun run() {
                            stun()
                        }
                    }.runTaskLater(CustomItems.plugin, 120L).taskId)
                }
            }

            2 -> {
                //predictive sonic boom attack
                if (random < 0.2 && !hasUsedMobWave) {
                    hasUsedMobWave = true
                    attackDelay = 25
                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                CustomEffects.playSound(getCenter(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 2.0F, 0.8F)

                                for (i in 0..(playerCount * 7).coerceAtMost(18)) {
                                    wardenSpawnMobs()
                                }

                            }
                        }.runTaskLater(
                            CustomItems.plugin,
                            10
                        ).taskId
                    )
                }
                //laser from each flame to nearest player
                else if (random < 0.6) {
                    //Bukkit.getLogger().info("flame line")
                    attackDelay = arrayOf(17, 15, 16, 15)[subphase] + 8
                    val center = getLowerCenter()
                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(42.0, 46.0, 50.0, 54.0)[subphase], CustomDamageType.DEFAULT,
                        damager = boss
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AZALEA_BREAK, 0.6F, 1.4F, 10, Sound.ITEM_TRIDENT_HIT
                    )

                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                playReapeatingSound(soundSettings, arrayOf(70, 60, 40, 30)[subphase])

                                for (x in arrayOf(8.5, -8.5)) for (z in arrayOf(8.5, -8.5)) {
                                    val loc = center.clone().add(x, 2.0, z)
                                    var currentClosest: Player = players.first()
                                    var closestDist = 50.0
                                    for (player in players) {
                                        if (player.location.subtract(loc).length() < closestDist) {
                                            closestDist = player.location.subtract(loc).length()
                                            currentClosest = player
                                        }
                                    }

                                    val newEnd =
                                        currentClosest.location.add(0.0, 1.0, 0.0).subtract(loc).toVector().normalize()
                                            .multiply(33)

                                    lineAttack(
                                        loc,
                                        loc.clone().add(newEnd),
                                        particleSettings,
                                        damageSettings,
                                        delay = arrayOf(70, 60, 40, 30)[subphase]
                                    )

                                }

                                i++
                                if (i == arrayOf(4, 4, 5, 7)[subphase]) this.cancel()
                            }
                        }.runTaskTimer(
                            CustomItems.plugin,
                            arrayOf(10, 10, 20, 10)[subphase].toLong(),
                            arrayOf(80, 70, 60, 40)[subphase].toLong()
                        ).taskId
                    )
                }
                // Mob wave attack
                else {

                    //Bukkit.getLogger().info("sonic boom")
                    attackDelay =  arrayOf(15, 13, 12, 10)[subphase] + 8

                    var i = 0

                    val damageSettings = DamageSettings(
                        arrayOf(6.0, 7.0, 8.0, 10.0)[subphase],
                        CustomDamageType.ALL_BYPASS,
                        damager = boss,
                        knockback = CenterKnockback(getCenter(), 3.0, Vector(0.0, 0.5, 0.0))
                    )

                    val soundSettings = SoundSettings(
                        Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 1.5F, 10, Sound.ENTITY_WARDEN_SONIC_BOOM
                    )

                    tasks.add(
                        object : BukkitRunnable() {
                            override fun run() {

                                playReapeatingSound(soundSettings, arrayOf(70, 60, 50, 40)[subphase])

                                for (player in players) {

                                    val newEnd = player.location.add(0.0, 1.4, 0.0)
                                        .subtract(getCenter().add(0.0, 1.0, 0.0)).toVector()
                                        .normalize().multiply(25)

                                    linearRoundAttack(
                                        getCenter().add(0.0, 1.0, 0.0),
                                        getCenter().add(newEnd),
                                        1.5,
                                        particleSettings,
                                        damageSettings,
                                        delay = arrayOf(70, 60, 50, 40)[subphase]
                                    )
                                }

                                i++
                                if (i == 3) this.cancel()

                            }
                        }.runTaskTimer(
                            CustomItems.plugin,
                            arrayOf(30, 20, 30, 20)[subphase].toLong(),
                            arrayOf(90, 80, 70, 60)[subphase].toLong()
                        ).taskId
                    )
                }
            }

            3 -> {

            }
        }
    }

    override fun tick() {
        super.tick()

        if (Bukkit.getCurrentTick() % 20 == 0) {

            if (mobDelay > 0) mobDelay--
            else if (mobDelay != -1) {
                wardenSpawnMobs()
                mobDelay = (15 / playerCount.toDouble().pow(0.6)).toInt()
            }

            if (attackDelay > 0) attackDelay--
            else if (attackDelay != -1) wardenStartAttack()

            if (underStunThresholdAndStunned()) {
                isStunned = false
                attackDelay = if (phase == 2) 10 else 0
                attackCount = 0
                hasUsedMobWave = false
                boss.isInvulnerable = true

                if (phase == 2) {
                    spawnWardenMini()
                }

            }
        }

        if (Bukkit.getCurrentTick() % 10 == 0 && !isStunned && phase != 0) {

            val subphase = when (hpPercent) {
                in 0.6..0.7 -> 3
                in 0.7..0.8 -> 2
                in 0.8..0.9 -> 1
                in 0.9..1.0 -> 0
                else -> 0
            }

            val damageSettings = DamageSettings(
                arrayOf(28.0, 30.0, 32.0, 35.0)[subphase], CustomDamageType.DEFAULT,
                damager = boss, CenterKnockback(getCenter(), 2.5)
            )

            planeAttack(
                Circle(5.0, getLowerCenter()), getLowerCenter().y,
                ParticleSettings(
                    Particle.DUST.builder().data(DustOptions(Color.fromRGB(102, 226, 232), 1.0F)), 5,
                    Particle.DUST.builder().data(DustOptions(Color.fromRGB(50, 117, 120), 1.0F))
                ),
                0.15, damageSettings, delay = 10
            )
        }
    }

    fun spawnWardenMini() {
        val reference = this
        tasks.add(object : BukkitRunnable() { override fun run() {
            val loc = possWardenSpawnLoc()
            for (x in -1..1) for (z in -1..1) {
                loc.clone().add(x.toDouble(), 0.0, z.toDouble()).block.type = Material.AIR
            }
            loc.add(0.5, 0.0, 0.5)
            CustomEffects.playSound(loc, Sound.ENTITY_WARDEN_EMERGE, 3.0F, 1.0F)
            CustomEffects.particleCloud(Particle.SONIC_BOOM.builder(), loc.clone().add(0.0, 1.5, 0.0), 40, 2.0, 0.0)

            val subPhase = stunCounter - 2
            val warden = CustomItems.bossWorld.spawn(loc, Warden::class.java) {
                it.getAttribute(Attribute.MAX_HEALTH)!!.baseValue *= 0.4 + (0.05 * subPhase)
                it.health *= 0.4 + (0.05 * subPhase)
                it.getAttribute(Attribute.MOVEMENT_SPEED)!!.baseValue *= 0.7 + (0.05 * subPhase)
                it.getAttribute(Attribute.ATTACK_DAMAGE)!!.baseValue *= 0.85 + (0.1 * subPhase)
            }
            EntityWrapperManager.getWrapperorNew(warden).addComponent(WardenMinibossComponent(reference))
            activeEntities.add(warden)

        }}.runTaskLater(CustomItems.plugin, 40L).taskId)
    }
    fun underStunThresholdAndStunned(): Boolean {
        return isStunned &&
                hpPercent < 1.0 - stunCounter * 0.1 &&
                phase < 3
    }

    private fun playReapeatingSound(soundSettings: SoundSettings, delay: Int) {
        var s = 0
        val period = delay / soundSettings.steps

        tasks.add(object : BukkitRunnable() { override fun run() {
            s++

            if (s == soundSettings.steps) {
                CustomEffects.playSound(getCenter(), soundSettings.postSound, 3.0F, 1.0F, random = false)
            } else {
                CustomEffects.playSound(getCenter(), soundSettings.preSound, 3.0F, soundSettings.getPitch(s), random = false)
            }

            if (s == soundSettings.steps) this.cancel()
        }}.runTaskTimer(CustomItems.plugin, 1L, period.toLong()).taskId)
    }
    private fun lineAttack(start: Location, end: Location, particleSettings: ParticleSettings, damage: DamageSettings, delay: Int = 0, duration: Int = 0) {

        val direction = end.clone().subtract(start)
        val length = direction.length()
        val unit = direction.toVector().normalize().multiply(0.1)
        val newLoc = start.clone()

        var k = delay / particleSettings.preParticleSeparation

        tasks.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            CustomEffects.particleLine(particleSettings.preParticle, start, end, (length * 10).toInt())

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)


        if (duration == 0) {
            tasks.add(object : BukkitRunnable() { override fun run() {

                val toDamage = mutableSetOf<Player>()

                for (i in 0..(length/0.1).toInt()) {
                    for (player in newLoc.getNearbyEntitiesByType(Player::class.java, 3.0)) {
                        if (player.boundingBox.containsLoc(newLoc, player.world)) {
                            //println("player added")
                            toDamage.add(player)
                        }
                    }

                    for (player in toDamage) {
                        player.applyDamage(damage)
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
            tasks.add(object : BukkitRunnable() { override fun run() {
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
    private fun arcAttack(origin: Location, radius: Double, totalDegrees: Double, direction: Vector, particleSettings: ParticleSettings, damage: DamageSettings, possRandomOffset: Double = 0.0, delay: Int = 0) {


        var k = delay / particleSettings.preParticleSeparation

        tasks.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            CustomEffects.rotatedArc(particleSettings.preParticle, origin, radius, totalDegrees, (Math.PI * radius.pow(2) * (totalDegrees/360.0) * 50).toInt(), direction, possRandomOffset)

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)

        tasks.add(object : BukkitRunnable() { override fun run() {

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
    private fun planeAttack(shape: Shape, yLevel: Double, particleSettings: ParticleSettings, concentration: Double, damage: DamageSettings, above: Boolean = true, delay: Int = 0) {
        if (shape is NegativePolygon) {
            //Bukkit.getLogger().info(shape.area.toString())
            //Bukkit.getLogger().info(shape.boundingArea.toString())
        }
        var k = delay / particleSettings.preParticleSeparation

        tasks.add(object : BukkitRunnable() { override fun run() {
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

        tasks.add(object : BukkitRunnable() { override fun run() {
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
    private fun linearRoundAttack(start: Location, end: Location, radius: Double, particleSettings: ParticleSettings, damage: DamageSettings, delay: Int = 0, duration: Int = 0) {

        val direction = end.subtract(start)
        val length = direction.length()
        val unit = direction.toVector().normalize().multiply(0.1)

        var k = delay / particleSettings.preParticleSeparation

        tasks.add(object : BukkitRunnable() { override fun run() {
            if (k == 0) this.cancel()

            val newLoc = start.clone()

            for (i in 0..(length/0.2).toInt()) {
                CustomEffects.rotatedParticleCircle(particleSettings.preParticle, newLoc.clone(), radius, (0.8 * radius).toInt(), unit)
                newLoc.add(unit)
            }

            k--
        }}.runTaskTimer(CustomItems.plugin, 0L, particleSettings.preParticleSeparation.toLong()).taskId)


        if (duration == 0) {
            tasks.add(object : BukkitRunnable() { override fun run() {

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
            tasks.add(object : BukkitRunnable() { override fun run() {

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

}