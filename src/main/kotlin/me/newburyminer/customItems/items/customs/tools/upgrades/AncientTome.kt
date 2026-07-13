package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class AncientTome: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ANCIENT_TOME

    private val material = Material.BOOK
    private val color = arrayOf(128, 95, 11)
    private val name = text("Ancient Tome", color)
    private val lore = Utils.loreBlockToList(
        text("This item can be applied to any item that has this enchant at one level below the level on this item. " +
                "Sneak and swap hands with this in your offhand and the item you want to apply it to in your mainhand to apply.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerSwapHandItemsEvent::class, { e ->
            slotMatches(e, EquipmentSlot.OFF_HAND, custom) &&
            e.player.isSneaking &&
            e.player.inventory.itemInMainHand.type != Material.AIR
        },
        {e ->
            val upgrade = e.player.inventory.itemInOffHand
            val toUpgrade = e.player.inventory.itemInMainHand

            val (enchant, newLevel) = upgrade.enchantments.toList().firstOrNull() ?: return@register
            if (toUpgrade.enchantments.none { it.key == enchant && it.value == newLevel - 1 }) return@register

            e.isCancelled = true
            upgrade.amount -= 1

            toUpgrade.removeEnchantment(enchant)
            toUpgrade.addUnsafeEnchantment(enchant, newLevel)

            CustomEffects.playSound(e.player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.1F)
        })
    }

}