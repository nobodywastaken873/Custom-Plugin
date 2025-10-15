package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class ReinforcingStruts: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REINFORCING_STRUTS

    private val material = Material.CHAIN
    private val color = arrayOf(154, 161, 158)
    private val name = text("Reinforcing Struts", color)
    private val lore = Utils.loreBlockToList(
        text("This item adds 200 durability to any item, up to 5 times. " +
                "Sneak and swap hands with this in your offhand and the item you want to apply it to in your mainhand to apply.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerSwapHandItemsEvent -> {
                if (ctx.itemType != EventItemType.OFFHAND) return
                if (!e.player.isSneaking) return
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
