package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.duraBroken
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
import me.newburyminer.customItems.Utils.Companion.tagTool
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
import org.bukkit.enchantments.Enchantment
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
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class NetheriteMattock: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_MATTOCK

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(54, 35, 64)
    private val name = text("Netherite Mattock", color)
    private val lore = Utils.loreBlockToList(
        text("Works as a axe, pickaxe, shovel, and hoe when breaking blocks.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .tagTool(Tag.MINEABLE_HOE, 9F)
        .tagTool(Tag.MINEABLE_AXE, 9F)
        .tagTool(Tag.MINEABLE_SHOVEL, 9F)
        .tagTool(Tag.MINEABLE_PICKAXE, 9F)
        .duraBroken(1)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockBreakEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val item = ctx.item ?: return
                val newMeta = item.itemMeta as Damageable
                if (Math.random() < (1.0 - 1.0 / (1 + (item.enchantments[Enchantment.UNBREAKING] ?: 0)))) return
                newMeta.damage += 1
                if (newMeta.damage == 2031) {item.amount = 0; CustomEffects.playSound(e.player.location, Sound.ENTITY_ITEM_BREAK, 1F, 1F)}
                item.itemMeta = newMeta
            }

        }

    }

}