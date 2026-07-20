package me.newburyminer.customItems.items.customs.weapons.magic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.HeldActivation
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EvokerFangs
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class FangedStaff: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FANGED_STAFF

    private val material = Material.QUARTZ
    private val color = arrayOf(112, 121, 128)
    private val name = text("Fanged Staff", color)
    private val lore = Utils.loreBlockToList(
        text("Left click to summon fangs, 0.5 second cooldown. Hold right click for 4 seconds to gain an aura that damages anything within it.", Utils.GRAY)
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
            e.item.isItem(custom)
        },
        {e ->
            val wand = e.item ?: return@register
            if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) && wand.offCooldown(e.player, "Vexing")) {
                CustomEffects.playSound(e.player.location, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1F, 0.3F)
            }

            else if ((e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) && wand.offCooldown(e.player, "Fangs")) {
                val result =
                    e.player.location.world.rayTraceBlocks(e.player.eyeLocation, e.player.location.direction, 80.0,
                        FluidCollisionMode.NEVER, true)?.hitPosition?.toLocation(e.player.world) ?:
                    e.player.eyeLocation.add(e.player.location.direction.normalize().multiply(80))

                for (x in -1..1) { for (z in -1..1) {
                    val fangs = result.world.spawn(result.clone().add(x.toDouble(), 0.0, z.toDouble()), EvokerFangs::class.java)
                    fangs.owner = e.player
                } }
                wand.setCooldown(e.player, 0.5, "Fangs")
                CustomEffects.playSound(e.player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.7F)
            }
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(4 to {player -> updateCounter(player)})

    private fun updateCounter(player: Player) {
        if (!player.activeItem.isItem(custom)) return
        if (!player.offCooldown(custom, "Vexing")) {
            player.clearActiveItem()
        }

        if (player.activeItemUsedTime < 80) return

        player.setCooldown(custom, 45.0, "Vexing")
        CustomEffects.playSound(player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.9F)
        EffectManager.applyEffect(player, CustomEffectType.FANG_STAFF_VEXING, 10 * 20)
        player.clearActiveItem()
    }

}