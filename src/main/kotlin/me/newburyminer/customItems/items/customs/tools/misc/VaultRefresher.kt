package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Vault
import org.bukkit.entity.EntityType
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class VaultRefresher: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.VAULT_REFRESHER

    private val material = Material.IRON_NUGGET
    private val color = arrayOf(190, 202, 237)
    private val name = text("Vault Refresher", color)
    private val lore = Utils.loreBlockToList(
        text("Right click a vault to reset its usage for all players, consuming this item.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            e.action == Action.RIGHT_CLICK_BLOCK
        },
        {e ->
            e.isCancelled = true
            val block = e.clickedBlock ?: return@register

            val state = block.state as? Vault ?: return@register
            state.rewardedPlayers.forEach {
                state.removeRewardedPlayer(it)
            }
            state.update()

            e.item?.amount -= 1

            CustomEffects.playSound(block.location, Sound.BLOCK_VAULT_INSERT_ITEM, 1F, 1.2F)
            e.player.setCooldown(custom, 0.5)
        })
    }

}