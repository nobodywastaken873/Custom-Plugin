package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class Treecapitator: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TREECAPITATOR

    private val material = Material.GOLDEN_AXE
    private val color = arrayOf(117, 32, 8)
    private val name = text("Treecapitator", color)
    private val lore = Utils.loreBlockToList(
        text("Mines up to 200 logs or leaves of the same type connected to the block you break.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .unb()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockBreakEvent -> {
                //for (item in e.block.drops) e.player.sendMessage(item.type.toString() + item.amount.toString())
                val axe = ctx.item ?: return
                val material = e.block.type
                if (!Tag.LOGS.isTagged(material) && !Tag.LEAVES.isTagged(material)) return
                val drops: MutableList<ItemStack> = mutableListOf()
                var total = 1
                val checked = mutableListOf(e.block.location.clone())
                val toContinue = mutableListOf(e.block.location.clone())
                while (toContinue.isNotEmpty() && total <= 200) {
                    val currentLoc = toContinue[0]
                    for (loc in getAround(currentLoc)) {
                        if (loc in checked) continue
                        if (e.block.world.getBlockAt(loc).type == material) {
                            for (drop in e.block.world.getBlockAt(loc).getDrops(axe, e.player)) drops.add(drop)
                            e.block.world.getBlockAt(loc).type = Material.AIR
                            if (total < 5) CustomEffects.playSound(loc, e.block.blockData.soundGroup.breakSound, 1.0F, e.block.blockData.soundGroup.pitch)
                            total++
                            checked.add(loc)
                            toContinue.add(loc)
                        } else {
                            checked.add(loc)
                        }
                    }
                    toContinue.removeFirst()
                }
                if (axe.itemMeta.hasEnchant(CustomEnchantments.AUTOSMELT)) {
                    for (drop in drops) {
                        drop.smelt()
                    }
                }
                for (drop in drops) {
                    e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
                }
            }

        }

    }

    private fun getAround(loc: Location): MutableList<Location> {
        val locs = mutableListOf<Location>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    locs.add(loc.clone().add(Vector(x, y, z)))
                }
            }
        }
        return locs
    }

}