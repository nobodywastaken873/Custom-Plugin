package me.newburyminer.customItems.items.customs.food

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

class MysticalGreenApple: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MYSTICAL_GREEN_APPLE

    private val material = Material.APPLE
    private val color = arrayOf(35, 212, 0)
    private val name = text("Mystical Green Apple", color)
    private val lore = Utils.loreBlockToList(text("Increases the amount of your experience gain from all sources by 15% up to 4 times.", Utils.GRAY))

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .food(20, 20F, true)
        .build()

    private val experienceTag = "experiencekept"
    init {
        register(PlayerItemConsumeEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            if ((e.player.getTag<Int>(experienceTag) ?: 0) == 4) {
                e.isCancelled = true
                e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
                return@register
            }
            e.player.setTag(experienceTag, (e.player.getTag<Int>(experienceTag) ?: 0) + 1)
            CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
        })

        register(PlayerPickupExperienceEvent::class, {e ->
            (e.player.getTag<Int>(experienceTag) ?: 0) != 0
        },
        {e ->
            val multiplier = e.player.getTag<Int>(experienceTag) ?: 0
            e.experienceOrb.experience = (e.experienceOrb.experience * (1 + multiplier * 0.15)).toInt()
        })
    }

}