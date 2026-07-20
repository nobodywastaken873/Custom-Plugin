package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
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
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.meta.Damageable

class SilverCoating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SILVER_COATING

    private val material = Material.IRON_INGOT
    private val color = arrayOf(213, 234, 237)
    private val name = text("Silver Coating", color)
    private val lore = Utils.loreBlockToList(
        text("Can be applied to any melee weapon to add one level of the Mob Slayer enchant, " +
                "which adds 10% damage against Arid Lands mobs, with a maximum level of 8. " +
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

            if (!RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM)
                .getTagValues(ItemTypeTagKeys.ENCHANTABLE_MELEE_WEAPON)
                    .contains(toUpgrade.type.asItemType())
                ) return@register

            if ((toUpgrade.enchantments[CustomEnchantments.MOB_SLAYER] ?: 0) >= 8) return@register
            e.isCancelled = true
            upgrade.amount -= 1

            //val newMeta = toUpgrade.itemMeta as Damageable
            //if (newMeta.hasMaxDamage()) toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, newMeta.maxDamage + 100)
            //else toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, toUpgrade.type.maxDurability + 100)
            toUpgrade.addUnsafeEnchantment(CustomEnchantments.MOB_SLAYER, (toUpgrade.enchantments[CustomEnchantments.MOB_SLAYER] ?: 0) + 1)

            CustomEffects.playSound(e.player.location, Sound.ITEM_SPEAR_HIT, 1.0F, 0.6F)
        })
    }

}