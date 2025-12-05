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
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class ToolsmithUpgrade: CustomItemDefinition {

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

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.rightClicked !is Villager) return
                val villager: Villager = e.rightClicked as Villager

                if (EntityWrapperManager.getWrapper(villager.uniqueId)
                        ?.hasComponent(OvermaxVillagerComponent::class) == true) return

                if (villager.profession != Villager.Profession.TOOLSMITH || villager.villagerLevel != 5) return
                e.isCancelled = true

                val trades = arrayOf(CustomItem.TOOL_HANDLE, CustomItem.REINFORCED_HANDLE)
                val newRecipes = Lists.newArrayList(villager.recipes)

                var newRecipe = MerchantRecipe(ItemRegistry.get(CustomItem.TOOL_HANDLE), 0, 10000, true, 0, 0F, true)

                newRecipe.addIngredient(ItemRegistry.get(CustomItem.HANDLE_BINDING))
                newRecipe.addIngredient(ItemStack(Material.IRON_CHAIN, 16))
                newRecipes.add(newRecipe)

                newRecipe = MerchantRecipe(ItemRegistry.get(CustomItem.REINFORCED_HANDLE), 0, 10000, true, 0, 0F, true)

                newRecipe.addIngredient(ItemRegistry.get(CustomItem.STRENGTHENING_RODS))
                newRecipe.addIngredient(ItemStack(Material.COPPER_CHAIN, 16))
                newRecipes.add(newRecipe)

                villager.recipes = newRecipes
                EntityWrapperManager.getWrapperorNew(villager).addComponent(OvermaxVillagerComponent())

                item.amount -= 1
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 1.4F)
                CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
            }

        }

    }

}