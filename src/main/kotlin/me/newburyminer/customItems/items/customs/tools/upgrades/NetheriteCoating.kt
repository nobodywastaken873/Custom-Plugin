package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class NetheriteCoating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_COATING

    private val material = Material.NETHERITE_SCRAP
    private val color = arrayOf(74, 63, 56)
    private val name = text("Netherite Coating", color)
    private val lore = Utils.loreBlockToList(
        text("This item applies the Fireproof enchantment to any item, which prevents it from being burned in fire or lava. " +
                "You may apply Blast Resistant to an item with Fireproof, but it will overwrite the bonus. " +
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
                val toUpgrade = e.player.inventory.itemInMainHand
                if (e.player.inventory.itemInMainHand.type == Material.AIR) return
                if (CustomEnchantments.FIREPROOF in toUpgrade.enchantments.keys) return
                e.isCancelled = true
                upgrade.amount -= 1
                toUpgrade.resist(DamageTypeTagKeys.IS_FIRE)
                toUpgrade.addUnsafeEnchantment(CustomEnchantments.FIREPROOF, 1)
                //lore
                CustomEffects.playSound(e.player.location, Sound.BLOCK_SMITHING_TABLE_USE, 1.0F, 1.1F)
            }

        }

    }

}