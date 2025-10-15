package me.newburyminer.customItems.items.customs.tools.upgrades

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
import org.bukkit.Tag
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class FieryShard: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FIERY_SHARD

    private val material = Material.MAGMA_CREAM
    private val color = arrayOf(255, 117, 54)
    private val name = text("Fiery Shard", color)
    private val lore = Utils.loreBlockToList(
        text("This item applies the Autosmelt enchantment to tools, which smelts mined blocks. " +
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
                val smelt = ctx.item ?: return
                val smeltable = e.player.inventory.itemInMainHand
                if (!Tag.ITEMS_PICKAXES.isTagged(smeltable.type) && !Tag.ITEMS_AXES.isTagged(smeltable.type) && !Tag.ITEMS_SHOVELS.isTagged(smeltable.type)) return
                if (CustomEnchantments.AUTOSMELT in smeltable.enchantments.keys) return
                e.isCancelled = true
                smelt.amount -= 1
                smeltable.addUnsafeEnchantment(CustomEnchantments.AUTOSMELT, 1)
                CustomEffects.playSound(e.player.location, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1.0F, 1.1F)
            }

        }

    }

}