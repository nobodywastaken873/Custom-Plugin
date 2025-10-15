package me.newburyminer.customItems.items.customs.weapons.magic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.entities.bosses.BossListeners
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Container
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.util.Vector
import java.util.*

class PewmaticHorn: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PEW_MATIC_HORN

    private val material = Material.POPPED_CHORUS_FRUIT
    private val color = arrayOf(179, 57, 75)
    private val name = text("Pew-matic Horn", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (!ctx.itemType.isHand()) return
                if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
                if (!item.offCooldown(e.player)) return
                usedMap[e.player.uniqueId] = true

            }

        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(
            6 to {player -> pewmaticHornTick(player)},
            4 to {player -> pewmaticHornShoot(player)}
        )

    private val counterMap = mutableMapOf<UUID, Int>()
    private val usedMap = mutableMapOf<UUID, Boolean>()
    private fun pewmaticHornTick(player: Player) {
        val uuid = player.uniqueId
        if (usedMap[uuid] == true) {
            counterMap[uuid] = (counterMap[uuid] ?: 0) + 1
            val pewMaticHornCount = counterMap[uuid] ?: return
            CustomEffects.playSound(
                player.location,
                Sound.BLOCK_AZALEA_PLACE,
                1F,
                (0.15 * (if (pewMaticHornCount > 12) 13 else pewMaticHornCount)).toFloat()
            )
        } else {
            if ((counterMap[uuid] ?: 0) != 0) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
                val totalTime = counterMap[uuid] ?: return
                if (totalTime > 12) {
                    player.setCooldown(CustomItem.PEW_MATIC_HORN, (0.3 * 4 * totalTime * 10).toInt() / 10.0)
                }
            }
            counterMap[uuid] = 0
        }
        usedMap[uuid] = false
    }
    private fun pewmaticHornShoot(player: Player) {
        if ((counterMap[player.uniqueId] ?: 0) > 12) {
            val facing = player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
            val possProj: Array<Pair<EntityType, Double>> =
                arrayOf(
                    Pair(EntityType.ARROW, 1.0),
                    Pair(EntityType.SPECTRAL_ARROW, 1.0),
                    Pair(EntityType.SPLASH_POTION, 1.0),
                    Pair(EntityType.FALLING_BLOCK, 1.0),
                    Pair(EntityType.WIND_CHARGE, 1.0),
                    Pair(EntityType.FIREBALL, 1.0),
                    Pair(EntityType.TNT, 1.0),
                    Pair(EntityType.FIREWORK_ROCKET, 1.0),
                    Pair(EntityType.DRAGON_FIREBALL, 1.0),
                    Pair(EntityType.SMALL_FIREBALL, 1.0),
                    Pair(EntityType.BEE, 1.0),
                    Pair(EntityType.COD, 1.0),
                    Pair(EntityType.EGG, 1.0),
                    Pair(EntityType.SNOWBALL, 1.0),
                    Pair(EntityType.EXPERIENCE_BOTTLE, 1.0),
                    Pair(EntityType.PUFFERFISH, 1.0),
                    Pair(EntityType.SHULKER_BULLET, 1.0),
                    Pair(EntityType.SILVERFISH, 1.0),
                    Pair(EntityType.WITHER_SKULL, 1.0)
                )
            val type = possProj.random().first
            var entity: Entity? = null
            if (type == EntityType.ARROW) {
                entity = player.world.spawnEntity(startingLocation, type) as Arrow
                entity.basePotionType = PotionType.entries.random()
            } else if (type == EntityType.SPLASH_POTION) {
                entity = player.world.spawnEntity(startingLocation, type) as ThrownPotion
                val newMeta = entity.potionMeta
                newMeta.basePotionType = PotionType.entries.random()
                entity.potionMeta = newMeta
            } else if (type == EntityType.FALLING_BLOCK) {
                entity = player.world.spawnEntity(startingLocation, type) as FallingBlock
                entity.blockData = arrayOf(
                    Material.SAND.createBlockData(),
                    Material.POINTED_DRIPSTONE.createBlockData(),
                    Material.GRAVEL.createBlockData(),
                    Material.CYAN_CONCRETE.createBlockData(),
                    Material.DAMAGED_ANVIL.createBlockData()
                ).random()
            } else if (type == EntityType.FIREBALL) {
                entity = player.world.spawnEntity(startingLocation, type) as Fireball
                entity.yield = (Math.random() * 5).toFloat()
            } else if (type == EntityType.FIREWORK_ROCKET) {
                entity = player.world.spawnEntity(startingLocation, type) as Firework
                val newMeta = entity.fireworkMeta
                newMeta.power = (Math.random() * 6).toInt()
            } else {
                entity = player.world.spawnEntity(startingLocation, type)
            }
            entity.velocity = facing.multiply(40)
        }
    }

}
