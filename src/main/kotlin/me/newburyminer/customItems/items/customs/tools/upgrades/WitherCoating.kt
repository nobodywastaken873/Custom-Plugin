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

class WitherCoating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WITHER_COATING

    private val material = Material.IRON_INGOT
    private val color = arrayOf(191, 242, 224)
    private val name = text("Wither Coating", color)
    private val lore = Utils.loreBlockToList(
        text("This item applies the Blast Resistant enchantment to any item, which prevents it from being blown up by explosions. " +
                "You may apply Fireproof to an item with Blast Resistant, but it will overwrite the bonus. " +
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
                if (CustomEnchantments.BLAST_RESISTANT in toUpgrade.enchantments.keys) return
                e.isCancelled = true
                upgrade.amount -= 1
                toUpgrade.resist(DamageTypeTagKeys.IS_EXPLOSION)
                toUpgrade.addUnsafeEnchantment(CustomEnchantments.BLAST_RESISTANT, 1)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_WITHER_AMBIENT, 1.0F, 1.1F)
            }

        }

    }

}