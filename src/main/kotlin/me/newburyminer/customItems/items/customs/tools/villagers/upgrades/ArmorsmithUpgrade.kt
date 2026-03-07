package me.newburyminer.customItems.items.customs.tools.villagers.upgrades

import com.google.common.collect.Lists
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.OvermaxVillagerComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.items.behaviors.VillagerUpgrade
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class ArmorsmithUpgrade: CustomItemDefinition, VillagerUpgrade {

    override val custom: CustomItem = CustomItem.ARMORSMITH_UPGRADE

    private val material = Material.IRON_CHAIN
    private val color = arrayOf(117, 121, 133)
    private val name = Utils.text("Armorer Upgrade", color)
    private val lore = Utils.loreBlockToList(
        Utils.text(
            "Right click on a master level armorer to gain two gain two new trades. The trades are for the Steel Plating and Fire-resistant Resin which are materials for other custom items.",
            Utils.GRAY
        ),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.rightClicked is Villager &&
            EntityWrapperManager.getWrapper(e.rightClicked.uniqueId)?.hasComponent(OvermaxVillagerComponent::class) != true &&
            (e.rightClicked as Villager).let { it.profession == Villager.Profession.ARMORER && it.villagerLevel == 5 }
        },
        {e ->
            val item = e.player.inventory.itemInMainHand
            e.isCancelled = true
            val villager = e.rightClicked as Villager
            val newRecipes = listOf(
                Pair(
                    ItemRegistry.get(CustomItem.FIRE_RESISTANT_RESIN),
                    ItemRegistry.get(CustomItem.STEEL_CHUNK)
                ) to ItemRegistry.get(CustomItem.STEEL_PLATING),
                Pair(
                    ItemRegistry.get(CustomItem.MOLTEN_MIXTURE),
                    ItemStack(Material.BLAZE_ROD, 16)
                ) to ItemRegistry.get(CustomItem.FIRE_RESISTANT_RESIN)
            )

            upgradeVillager(villager, newRecipes)

            item.amount -= 1
            CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 1.4F)
            CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
        })
    }

    /*override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.rightClicked !is Villager) return
                val villager: Villager = e.rightClicked as Villager

                if (EntityWrapperManager.getWrapper(villager.uniqueId)
                        ?.hasComponent(OvermaxVillagerComponent::class) == true) return

                if (villager.profession != Villager.Profession.ARMORER || villager.villagerLevel != 5) return
                e.isCancelled = true

                val trades = arrayOf(CustomItem.STEEL_PLATING, CustomItem.FIRE_RESISTANT_RESIN)
                val newRecipes = Lists.newArrayList(villager.recipes)

                var newRecipe = MerchantRecipe(ItemRegistry.get(CustomItem.STEEL_PLATING), 0, 10000, true, 0, 0F, true)

                newRecipe.addIngredient(ItemRegistry.get(CustomItem.FIRE_RESISTANT_RESIN))
                newRecipe.addIngredient(ItemRegistry.get(CustomItem.STEEL_CHUNK))
                newRecipes.add(newRecipe)

                newRecipe = MerchantRecipe(ItemRegistry.get(CustomItem.FIRE_RESISTANT_RESIN), 0, 10000, true, 0, 0F, true)

                newRecipe.addIngredient(ItemRegistry.get(CustomItem.MOLTEN_MIXTURE))
                newRecipe.addIngredient(ItemStack(Material.BLAZE_ROD, 16))
                newRecipes.add(newRecipe)

                villager.recipes = newRecipes
                EntityWrapperManager.getWrapperorNew(villager).addComponent(OvermaxVillagerComponent())

                item.amount -= 1
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 1.4F)
                CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
            }

        }

    }*/

}