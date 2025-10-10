package me.newburyminer.customItems.items.customs.weapons.magic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.convertVillagerLevel
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.lore
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
import me.newburyminer.customItems.helpers.CustomDamageType
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
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

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
                    e.player.setTag("evokerstaffused", true)
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

}
