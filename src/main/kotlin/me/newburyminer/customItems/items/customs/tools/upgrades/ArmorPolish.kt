package me.newburyminer.customItems.items.customs.tools.upgrades

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.DefaultEntityComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.meta.Damageable

class ArmorPolish: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ARMOR_POLISH

    private val material = Material.ARMADILLO_SCUTE
    private val color = arrayOf(213, 234, 237)
    private val name = text("Armor Polish", color)
    private val lore = Utils.loreBlockToList(
        text("Can be applied to any armor piece to add one level of the Creature Tactics enchant, " +
                "which adds 4% damage resistance against Arid Lands mobs, with a maximum level of 2. " +
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
                .getTagValues(ItemTypeTagKeys.ENCHANTABLE_ARMOR)
                    .contains(toUpgrade.type.asItemType())
                ) return@register

            if ((toUpgrade.enchantments[CustomEnchantments.CREATURE_TACTICS] ?: 0) >= 2) return@register
            e.isCancelled = true
            upgrade.amount -= 1

            //val newMeta = toUpgrade.itemMeta as Damageable
            //if (newMeta.hasMaxDamage()) toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, newMeta.maxDamage + 100)
            //else toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, toUpgrade.type.maxDurability + 100)
            toUpgrade.addUnsafeEnchantment(CustomEnchantments.CREATURE_TACTICS, (toUpgrade.enchantments[CustomEnchantments.CREATURE_TACTICS] ?: 0) + 1)

            CustomEffects.playSound(e.player.location, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.4F)
        })

        register(EntityDamageEvent::class, { e ->
            e.entity is Player &&
            (e.entity as Player).equipment.armorContents.any {
                it != null && it.type != Material.AIR &&
                it.enchantments.keys.contains(CustomEnchantments.CREATURE_TACTICS)
            }
        },
        {e ->
            val player = e.entity as Player
            val hitter = e.damageSource.directEntity ?: e.damageSource.causingEntity ?: return@register
            if (EntityWrapperManager.getWrapper(hitter.uniqueId)?.hasComponent(DefaultEntityComponent::class) == false) return@register

            val count = player.equipment.armorContents.map {
                if (it?.type != Material.AIR) {
                    it?.getEnchantmentLevel(CustomEnchantments.CREATURE_TACTICS) ?: 0
                } else {
                    0
                }
            }.sum()

            e.damage *= (1 - 0.04 * count)
        })
    }

}