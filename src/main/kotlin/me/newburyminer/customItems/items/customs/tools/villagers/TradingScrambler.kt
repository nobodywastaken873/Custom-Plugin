package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.villager.OvermaxVillagerComponent
import me.newburyminer.customItems.entity.components.villager.VillagerTradeComponent
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class TradingScrambler: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRADING_SCRAMBLER

    private val material = Material.LIME_DYE
    private val color = arrayOf(132, 207, 168)
    private val name = text("Trading Scrambler", color)
    private val lore = Utils.loreBlockToList(
        text("Right click on a villager to reroll its trades. All trades from all levels will be rerolled. This will not reset trades if they are locked out.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractAtEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.rightClicked is Villager &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(NonPickuppableComponent::class) != true &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(OvermaxVillagerComponent::class) != true
        },
        {e ->
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
        })
    }

}