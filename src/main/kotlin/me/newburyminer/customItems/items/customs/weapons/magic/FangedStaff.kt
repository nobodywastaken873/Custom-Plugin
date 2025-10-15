package me.newburyminer.customItems.items.customs.weapons.magic

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EvokerFangs
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class FangedStaff: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FANGED_STAFF

    private val material = Material.QUARTZ
    private val color = arrayOf(112, 121, 128)
    private val name = text("Fanged Staff", color)
    private val lore = Utils.loreBlockToList(
        text("Left click to summon fangs, 0.5 second cooldown. Hold right click for 4 seconds to gain an aura that damages anything within it.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) && e.item!!.offCooldown(e.player, "Vexing")) {
                    usedMap[e.player.uniqueId] = true
                } else if ((e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) && e.item!!.offCooldown(e.player, "Fangs")) {
                    val facing = e.player.location.direction.normalize().clone().multiply(0.1)
                    val startingLocation = e.player.location.clone().add(Vector(0.0, 1.6, 0.0))
                    for (i in 0..800) {
                        if (!startingLocation.add(facing).block.isPassable) {
                            break
                        }
                    }
                    for (x in -1..1) {
                        for (z in -1..1) {
                            val fangs = startingLocation.world.spawn(startingLocation.clone().add(x.toDouble(), 0.0, z.toDouble()), EvokerFangs::class.java)
                            fangs.owner = e.player
                        }
                    }
                    e.item!!.setCooldown(e.player, 0.5, "Fangs")
                    CustomEffects.playSound(e.player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.7F)
                }
            }

        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(6 to {player -> updateCounter(player)})

    private val counterMap = mutableMapOf<UUID , Int>()
    private val usedMap = mutableMapOf<UUID , Boolean>()
    private fun updateCounter(player: Player) {
        val uuid = player.uniqueId
        if (counterMap[uuid] == 13) {
            player.setCooldown(CustomItem.FANGED_STAFF, 55.0, "Vexing")
            counterMap[uuid] = 0
            usedMap[uuid] = false
            CustomEffects.playSound(player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 20F, 0.9F)
            EffectManager.applyEffect(player, CustomEffectType.FANG_STAFF_VEXING, 10 * 20)
        }
        if (usedMap[uuid] == true) {
            counterMap[uuid] = (counterMap[uuid] ?: 0) + 1
            CustomEffects.playSound(player.location, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1F, 1.2F)
        } else {
            if ((counterMap[uuid] ?: 0) != 0) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
            }
            counterMap[uuid] = 0
        }
        usedMap[uuid] = false
    }

}
