package me.newburyminer.customItems.items.customs.weapons.magic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.HeldActivation
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType
import org.bukkit.util.Vector

class PewmaticHorn: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PEW_MATIC_HORN

    private val material = Material.POPPED_CHORUS_FRUIT
    private val color = arrayOf(179, 57, 75)
    private val name = text("Pew-matic Horn", color)
    private val lore = mutableListOf(
        text("Hold right click for 6 seconds to begin shooting random projectiles.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
            .animation(ItemUseAnimation.TRIDENT)
            .consumeSeconds(32000.0F)
            .hasConsumeParticles(false)
            .build()
        )
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR)
        },
        {e ->
            CustomEffects.playSound(e.player.location, Sound.BLOCK_AZALEA_PLACE, 0.7F, 0.3F)
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(
            4 to {player -> pewmaticHornTick(player)},
        )

    private fun pewmaticHornTick(player: Player) {
        if (!player.activeItem.isItem(custom)) return
        if (player.activeItemUsedTime < 120) {
            CustomEffects.playSound(player.location, Sound.BLOCK_AZALEA_PLACE, 0.7F, 0.3F)
            return
        }


        CustomEffects.playSound(player.location, Sound.BLOCK_AZALEA_PLACE, 0.7F, 1.3F)

        pewmaticHornShoot(player)
    }
    private fun pewmaticHornShoot(player: Player) {
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
        var entity: Entity?
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