package me.newburyminer.customItems.items.customs.food

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.food
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

class MysticalGreenApple: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MYSTICAL_GREEN_APPLE

    private val material = Material.APPLE
    private val color = arrayOf(35, 212, 0)
    private val name = text("Mystical Green Apple", color)
    private val lore = Utils.loreBlockToList(text("Increases the amount of your total experience that you keep on death by 25%. You can consume a maximum of 4.", Utils.GRAY))

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .food(20, 20F, true)

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerItemConsumeEvent -> {
                if ((e.player.getTag<Int>("experiencekept") ?: 0) == 4) {
                    e.isCancelled = true
                    e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
                    return
                }
                e.player.setTag("experiencekept", (e.player.getTag<Int>("experiencekept") ?: 0) + 1)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
            }

        }
    }



}