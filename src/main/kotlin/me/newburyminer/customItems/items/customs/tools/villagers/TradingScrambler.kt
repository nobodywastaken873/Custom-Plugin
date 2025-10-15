package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack

class TradingScrambler: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRADING_SCRAMBLER

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(132, 207, 168)
    private val name = text("Trading Scrambler", color)
    private val lore = Utils.loreBlockToList(
        text("Right click on a villager to reroll its trades. All trades from all levels will be rerolled.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                if (e.rightClicked !is Villager) return
                if (e.rightClicked.getTag<Int>("id") != null) return
                val villager = e.rightClicked as Villager
                val maxLevel = villager.villagerLevel
                val profession = villager.profession
                val experience = villager.villagerExperience
                //villager.profession = Villager.Profession.NONE
                villager.villagerExperience = 0
                villager.villagerLevel = 1
                villager.recipes = mutableListOf()
                //villager.profession = profession
                for (i in 2..maxLevel) {
                    villager.addTrades(2)
                    villager.villagerLevel = i
                }
                villager.addTrades(2)
                villager.villagerExperience = experience
                CustomEffects.playSound(e.player.location, Sound.BLOCK_BAMBOO_BREAK, 1.0F, 1.0F)
            }

        }

    }

}
