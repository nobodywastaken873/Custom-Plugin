package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.villager.VillagerTradeComponent
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class DullRefreshingEmerald: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DULL_REFRESHING_EMERALD

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(134, 202, 227)
    private val name = text("Dull Refreshing Emerald", color)
    private val lore = Utils.loreBlockToList(
        text("Right click on a villager to refresh its trades, with a 5s cooldown. This will also reset any built up price increases.", Utils.GRAY),
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
            e.player.offCooldown(custom)
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

            tradingComponent.refreshTrades(wrapper)

            e.player.setCooldown(custom, 5.0)
        })
    }

}