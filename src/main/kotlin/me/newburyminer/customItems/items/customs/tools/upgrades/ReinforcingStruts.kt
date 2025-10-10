package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
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
import me.newburyminer.customItems.Utils.Companion.resist
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
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class ReinforcingStruts: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REINFORCING_STRUTS

    private val material = Material.CHAIN
    private val color = arrayOf(154, 161, 158)
    private val name = text("Reinforcing Struts", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (ctx.itemType != EventItemType.OFFHAND) return
                val upgrade = ctx.item ?: return
                val toUpgrade = e.player.inventory.itemInMainHand
                if (e.player.inventory.itemInMainHand.type == Material.AIR) return
                if (toUpgrade.itemMeta !is Damageable) return
                if ((toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) >= 5) return
                e.isCancelled = true
                upgrade.amount -= 1
                val newMeta = toUpgrade.itemMeta as Damageable
                if (newMeta.hasMaxDamage()) toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, newMeta.maxDamage + 200)
                else toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, toUpgrade.type.maxDurability + 200)
                toUpgrade.addUnsafeEnchantment(CustomEnchantments.REINFORCED, (toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) + 1)
                CustomEffects.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 1.0F, 1.1F)
            }

        }

    }

}
