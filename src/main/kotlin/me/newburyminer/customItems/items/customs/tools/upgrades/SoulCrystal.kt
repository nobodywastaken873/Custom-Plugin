package me.newburyminer.customItems.items.customs.tools.upgrades

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class SoulCrystal: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SOUL_CRYSTAL

    private val material = Material.NETHER_STAR
    private val color = arrayOf(189, 154, 219)
    private val name = text("Soul Crystal", color)
    private val lore = Utils.loreBlockToList(
        text("This item adds the Soulbound enchantment to items, allowing them to be kept on death. " +
                "Sneak and swap hands with this in your offhand and the item you want to apply it to in your mainhand to apply.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerSwapHandItemsEvent -> {
                if (ctx.itemType != EventItemType.OFFHAND) return
                if (!e.player.isSneaking) return
                val upgrade = ctx.item ?: return
                val toSoulbind = e.player.inventory.itemInMainHand
                if (e.player.inventory.itemInMainHand.type == Material.AIR) return
                if (CustomEnchantments.SOULBOUND in toSoulbind.enchantments.keys) return
                e.isCancelled = true
                upgrade.amount -= 1
                toSoulbind.addUnsafeEnchantment(CustomEnchantments.SOULBOUND, 1)
                CustomEffects.playSound(e.player.location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 1.1F)
            }

        }

    }

}