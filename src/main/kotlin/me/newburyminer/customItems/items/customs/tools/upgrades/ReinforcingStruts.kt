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
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class ReinforcingStruts: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REINFORCING_STRUTS

    private val material = Material.IRON_CHAIN
    private val color = arrayOf(154, 161, 158)
    private val name = text("Reinforcing Struts", color)
    private val lore = Utils.loreBlockToList(
        text("This item adds 100 durability to any item, up to 5 times. " +
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
            e.player.inventory.itemInMainHand.type != Material.AIR &&
            e.player.inventory.itemInMainHand.itemMeta is Damageable
        },
        {e ->
            val upgrade = e.player.inventory.itemInOffHand
            val toUpgrade = e.player.inventory.itemInMainHand
            if ((toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) >= 5) return@register
            e.isCancelled = true
            upgrade.amount -= 1

            val newMeta = toUpgrade.itemMeta as Damageable
            if (newMeta.hasMaxDamage()) toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, newMeta.maxDamage + 100)
            else toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, toUpgrade.type.maxDurability + 100)
            toUpgrade.addUnsafeEnchantment(CustomEnchantments.REINFORCED, (toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) + 1)

            CustomEffects.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 1.0F, 1.1F)
        })
    }

}