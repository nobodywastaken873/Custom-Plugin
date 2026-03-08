package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.VillagerTradeComponent
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class RefreshingEmerald: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REFRESHING_EMERALD

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(16, 175, 232)
    private val name = text("Refreshing Emerald", color)
    private val lore = Utils.loreBlockToList(
        text("Right click on a villager to refresh its trades. This will also reset any built up price increases.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.rightClicked is Villager &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(NonPickuppableComponent::class) != true
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
        })
    }

}