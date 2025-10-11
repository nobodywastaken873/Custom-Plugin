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
import org.bukkit.util.Vector

class LastPrism: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LAST_PRISM

    private val material = Material.COPPER_INGOT
    private val color = arrayOf(243, 219, 255)
    private val name = text("Last Prism", color)
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
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (!item.offCooldown(e.player, "Zap")) return
                    val facing = e.player.location.direction.normalize().clone().multiply(0.1)
                    val startingLocation = e.player.location.clone().add(Vector(0.0, 1.6, 0.0))
                    for (i in 0..800) {
                        CustomEffects.particle(Particle.END_ROD.builder(), startingLocation, 1)
                        if (i%5 == 0) {
                            for (entity in startingLocation.getNearbyEntities(3.0, 3.0, 3.0)) {
                                if (entity is LivingEntity && entity != e.player) {
                                    if (!entity.boundingBox.overlaps(
                                            startingLocation.clone().add(Vector(0.75, 0.75, 0.75)).toVector(),
                                            startingLocation.clone().add(Vector(-0.75, -0.75, -0.75)).toVector())) continue
                                    if (entity is Player) entity.setTag("lastprismzapdebuff", 20)
                                    else entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(e.player as Entity).withCausingEntity(e.player as Entity).build())
                                }
                            }
                        }
                        if (!startingLocation.add(facing).block.isPassable) {
                            break
                        }
                    }
                    item.setCooldown(e.player, 10.0, "Zap")
                    CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_HIT, 1F, 1.3F)
                } else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                    if (!item.offCooldown(e.player, "Beam")) return
                    e.player.setTag("lastprismused", true)
                }
            }

        }

    }

}
