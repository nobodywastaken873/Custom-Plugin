package me.newburyminer.customItems.items.customs.food

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ShulkerFruit: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHULKER_FRUIT

    private val material = Material.CHORUS_FRUIT
    private val nameColor = arrayOf(157, 3, 252)
    private val name = text("Shulker Fruit", nameColor)
    private val lore = Utils.loreBlockToList(text("Consume to permanently be able to open shulker boxes in your inventory with right click.", Utils.GRAY))

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    private val tagConstant = "inventoryshulker"
    init {
        register(PlayerItemConsumeEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            if (e.player.getTag<Boolean>(tagConstant) == true) {
                e.isCancelled = true
                e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
                return@register
            }
            e.player.setTag(tagConstant, true)
            CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
        })
    }

    /*override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerItemConsumeEvent -> {
                if (e.player.getTag<Boolean>("inventoryshulker") == true) {
                    e.isCancelled = true
                    e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
                    return
                }
                e.player.setTag("inventoryshulker", true)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
            }

        }
    }*/

}