package me.newburyminer.customItems.entities.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.helpers.RandomSelector
import me.newburyminer.customItems.structures.CustomLootTable
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class CustomBoss {
    WARDEN,
    GUARDIAN,
    WITHER,
    PIGLIN,
    HUSK,

    ;

    val id: Int
        get() {
            return this.ordinal
        }

    private lateinit var center: Location
    lateinit var loot: CustomLootTable

    var tick: Int = 0

    val boss: LivingEntity?
        get() {
            for (entity in this.center.getNearbyEntitiesByType(LivingEntity::class.java, 25.0, 25.0, 25.0)) {
                if (entity.getTag<Int>("bossid") == this.id) return entity
            }
            return null
        }

    val bossBar: BossBar
        get() {
            val key = NamespacedKey(CustomItems.plugin, this.name.lowercase())
            if (Bukkit.getServer().getBossBar(key) == null) {
                return Bukkit.getServer().createBossBar(key, "", BarColor.RED, BarStyle.SEGMENTED_6)
            }
            return Bukkit.getServer().getBossBar(key)!!
        }

    fun getYOffset(): Double {
        return 201.0
    }

    fun getLowerCenter(): Location {
        val center = getCenter()
        center.y = getYOffset()
        return center
    }

    val hpPercent: Double
        get() {
            return this.boss!!.health / (this.boss as LivingEntity).getAttribute(Attribute.MAX_HEALTH)!!.baseValue
        }

    val players: List<Player>
        get() {
            val list = getCenter().getNearbyPlayers(25.0).toMutableList()
            list.removeIf { it.health == 0.0 }
            return list
        }

    fun getCenter(): Location {
        return Location(center.world, center.x, center.y, center.z)
    }

    fun isAlive(): Boolean {
        return this.center.block.type == Material.AIR
    }

    companion object {
        fun init() {
            WARDEN.center = Location(CustomItems.bossWorld, -58.5, 203.0, -15.5)
            GUARDIAN.center = Location(CustomItems.bossWorld, 64.5, 201.0, 59.5)
            WITHER.center = Location(CustomItems.bossWorld,  -58.5, 206.0, 59.5)
            PIGLIN.center = Location(CustomItems.bossWorld, 10.5, 202.0, 59.5)
            HUSK.center = Location(CustomItems.bossWorld, 42.5, 201.0, -5.5)

            WARDEN.loot = CustomLootTable(
                RandomSelector(
                    Pair(ItemStack(Material.AIR), 1)
                ), 0..5
            )
        }
    }
}