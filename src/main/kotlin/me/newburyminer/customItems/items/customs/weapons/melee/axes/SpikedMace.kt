package me.newburyminer.customItems.items.customs.weapons.melee.axes

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Weapon
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class SpikedMace: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SPIKED_MACE

    private val material = Material.MACE
    private val color = arrayOf(84, 179, 214)
    private val name = Utils.text("Spiked Mace", color)
    private val lore = Utils.loreBlockToList(
        Utils.text(
            "Right click to imbue your next attack with this weapon with a 10s shield disable. 30s cooldown.",
            Utils.GRAY
        )
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(
            EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom)
        },
        {e ->
            val player = e.damager as? Player ?: return@register
            val item = player.inventory.itemInMainHand

            if (!item.hasData(DataComponentTypes.WEAPON)) return@register
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                item.resetData(DataComponentTypes.WEAPON)
            })
        })

        register(
            PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.player.setCooldown(custom, 30.0)
            e.player.playSound(e.player.location, Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.4f)

            e.player.swingHand(e.hand ?: return@register)
            e.item?.setData(
                DataComponentTypes.WEAPON, Weapon.weapon()
                .disableBlockingForSeconds(10.0F)
                .build()
            )
        })
    }

}