package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.OvermaxVillagerComponent
import me.newburyminer.customItems.entity.components.VillagerTradeComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
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
        text("Right click on a villager to reroll its trades. All trades from all levels will be rerolled. This will not reset trades if they are locked out.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {

                if (!ctx.itemType.isHand()) return
                if (e.rightClicked !is Villager) return
                if (EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)
                        ?.hasComponent(NonPickuppableComponent::class) == true) return
                if (EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)
                        ?.hasComponent(OvermaxVillagerComponent::class) == true) return

                val villager = e.rightClicked as Villager
                val wrapper = EntityWrapperManager.getWrapperorNew(villager)

                val tradingComponent =
                    if (wrapper.hasComponent(VillagerTradeComponent::class))
                        wrapper.getComponents(VillagerTradeComponent::class).firstOrNull() as VillagerTradeComponent
                    else {
                        val newComponent = VillagerTradeComponent()
                        wrapper.addComponent(newComponent)
                        newComponent
                    }


                tradingComponent.rerollTrades(wrapper)

            }

        }

    }

}