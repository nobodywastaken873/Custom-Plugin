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

class FletcherUpgrade: CustomItemDefinition {

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

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.rightClicked !is Villager) return
                val villager: Villager = e.rightClicked as Villager

                if (EntityWrapperManager.getWrapper(villager.uniqueId)
                        ?.hasComponent(OvermaxVillagerComponent::class) == true) return

                if (villager.profession != Villager.Profession.FLETCHER || villager.villagerLevel != 5) return
                e.isCancelled = true

                val arrowTypes = listOf(CustomItem.DRIPSTONE_ARROW, CustomItem.ENDER_PEARL_ARROW, CustomItem.WITHER_SKULL_ARROW, CustomItem.LLAMA_SPIT_ARROW, CustomItem.SHULKER_BULLET_ARROW)
                val newRecipes = Lists.newArrayList(villager.recipes)

                val tradeTypes = arrowTypes.shuffled().take(2)
                tradeTypes.forEach {
                    val newRecipe = MerchantRecipe(ItemRegistry.get(it), 0, 10000, true, 0, 0F)

                    newRecipe.addIngredient(ItemStack(Material.EMERALD, 32))
                    newRecipe.addIngredient(ItemStack(Material.DIAMOND, 5))
                    newRecipes.add(newRecipe)
                }


                villager.recipes = newRecipes
                EntityWrapperManager.getWrapperorNew(villager).addComponent(OvermaxVillagerComponent())

                item.amount -= 1
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 5F, 1.4F)
                CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
            }

        }

    }

}