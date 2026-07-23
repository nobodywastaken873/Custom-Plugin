package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
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

class RefinedRefreshingEmerald: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REFINED_REFRESHING_EMERALD

    private val material = Material.TURTLE_SCUTE
    private val color = arrayOf(5, 174, 235)
    private val name = text("Refined Refreshing Emerald", color)
    private val lore = Utils.loreBlockToList(
        text("While holding this item, you will be able to trade infinitely with villagers without closing the menu.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractAtEntityEvent::class, { e ->
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

            tradingComponent.maxMaxUses(wrapper)
        })
    }

}