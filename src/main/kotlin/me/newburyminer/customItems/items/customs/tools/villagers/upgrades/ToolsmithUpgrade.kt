package me.newburyminer.customItems.items.customs.tools.villagers.upgrades

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.villager.OvermaxVillagerComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.items.behaviors.VillagerUpgrade
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ToolsmithUpgrade: CustomItemDefinition, VillagerUpgrade {

    override val custom: CustomItem = CustomItem.TOOLSMITH_UPGRADE

    private val material = Material.STICK
    private val color = arrayOf(179, 163, 136)
    private val name = Utils.text("Toolsmith Upgrade", color)
    private val lore = Utils.loreBlockToList(
        Utils.text(
            "Right click on a master level toolsmith to gain two gain two new trades. The trades are for the Tool Handle and Strengthened Handle which are materials for other custom items.",
            Utils.GRAY
        ),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractAtEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.rightClicked is Villager &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(OvermaxVillagerComponent::class) != true &&
            (e.rightClicked as Villager).let { it.profession == Villager.Profession.TOOLSMITH && it.villagerLevel == 5 }
        },
            {e ->
                val item = e.player.inventory.itemInMainHand
                e.isCancelled = true
                val villager = e.rightClicked as Villager
                val newRecipes = listOf(
                    Pair(
                        ItemRegistry.get(CustomItem.HANDLE_BINDING),
                        ItemStack(Material.IRON_CHAIN, 16)
                    ) to ItemRegistry.get(CustomItem.TOOL_HANDLE),
                    Pair(
                        ItemRegistry.get(CustomItem.STRENGTHENING_RODS),
                        ItemStack(Material.COPPER_CHAIN, 16)
                    ) to ItemRegistry.get(CustomItem.REINFORCED_HANDLE)
                )

                upgradeVillager(villager, newRecipes)

                item.amount -= 1
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 1.4F)
                CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
            })
    }

}