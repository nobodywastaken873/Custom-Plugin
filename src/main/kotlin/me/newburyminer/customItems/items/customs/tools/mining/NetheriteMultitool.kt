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
import org.bukkit.block.Container
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

class NetheriteMultitool: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_MULTITOOL

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(89, 14, 7)
    private val name = text("Netherite Multitool", color)
    private val lore = Utils.loreBlockToList(
        text("Right click while sneaking to cycle through a netherite pickaxe, axe, shovel, and hoe.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .setTag("tool", 0)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                if (!player.offCooldown(CustomItem.NETHERITE_MULTITOOL)) return
                if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
                // Hoe block because it doesnt fucking work
                else if (item.type == Material.NETHERITE_HOE && e.action == Action.RIGHT_CLICK_BLOCK &&
                    Tag.DIRT.isTagged(e.clickedBlock!!.type) && !player.isSneaking &&
                    player.world.getBlockAt(e.clickedBlock!!.location.add(0.0, 1.0, 0.0)).type == Material.AIR) {
                    e.clickedBlock!!.type = Material.FARMLAND
                }
                if (!player.isSneaking) return
                val toolNum = item.getTag<Int>("tool")!!
                val newMat = when (toolNum) {
                    0 -> Material.NETHERITE_PICKAXE
                    1 -> Material.NETHERITE_AXE
                    2 -> Material.NETHERITE_SHOVEL
                    3 -> Material.NETHERITE_HOE
                    else -> Material.AIR
                }
                item.type = newMat
                item.setTag("tool", if (toolNum == 3) 0 else toolNum + 1)
                player.setCooldown(CustomItem.NETHERITE_MULTITOOL, 0.5)
            }

        }

    }

}