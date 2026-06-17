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

class FletcherUpgrade: CustomItemDefinition, VillagerUpgrade {

    override val custom: CustomItem = CustomItem.FLETCHER_UPGRADE

    private val material = Material.STICK
    private val color = arrayOf(145, 116, 57)
    private val name = Utils.text("Fletcher Upgrade", color)
    private val lore = Utils.loreBlockToList(
        Utils.text(
            "Right click on a master level fletcher to gain two random custom arrow trades. The possible arrows are: dripstone, ender pearl, llama spit, wither skull, and shulker bullet. They will cost diamonds and emeralds to buy.",
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
            (e.rightClicked as Villager).let { it.profession == Villager.Profession.FLETCHER && it.villagerLevel == 5 }
        },
        {e ->
            val item = e.player.inventory.itemInMainHand
            e.isCancelled = true
            val villager = e.rightClicked as Villager

            val arrowTypes = listOf(
                CustomItem.DRIPSTONE_ARROW, CustomItem.ENDER_PEARL_ARROW, CustomItem.WITHER_SKULL_ARROW,
                CustomItem.LLAMA_SPIT_ARROW, CustomItem.SHULKER_BULLET_ARROW
            )

            val tradeTypes = arrowTypes.shuffled().take(2)
            val newRecipes = mutableListOf<Pair<Pair<ItemStack, ItemStack>, ItemStack>>()
            tradeTypes.forEach { arrowType ->
                newRecipes.add((ItemStack(Material.EMERALD, 32) to ItemStack(Material.DIAMOND, 5)) to ItemRegistry.get(arrowType))
            }

            upgradeVillager(villager, newRecipes)

            item.amount -= 1
            CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 1.4F)
            CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
        })
    }

}