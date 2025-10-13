package me.newburyminer.customItems.items.customs.tools.villagers

import com.google.common.collect.Lists
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class FletcherUpgrade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FLETCHER_UPGRADE

    private val material = Material.STICK
    private val color = arrayOf(145, 116, 57)
    private val name = text("Fletcher Upgrade", color)
    private val lore = Utils.loreBlockToList(
        text("Right click on a master level fletcher to gain a random custom arrow trade. The possible arrows are: dripstone, ender pearl, llama spit, wither skull, and shulker bullet. They will cost diamonds and emeralds to buy.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.rightClicked !is Villager) return
                val villager: Villager = e.rightClicked as Villager
                if (villager.profession != Villager.Profession.FLETCHER || villager.villagerLevel != 5 || villager.getTag<Int>("id") == CustomEntity.MAX_FLETCHER.id) return
                e.isCancelled = true
                val arrowTypes = arrayOf(CustomItem.DRIPSTONE_ARROW, CustomItem.ENDER_PEARL_ARROW, CustomItem.WITHER_SKULL_ARROW, CustomItem.LLAMA_SPIT_ARROW, CustomItem.SHULKER_BULLET_ARROW)
                val newRecipes = Lists.newArrayList(villager.recipes)
                val newRecipe = MerchantRecipe(ItemRegistry.get(arrowTypes.random()), 0, 10000, true, 0, 0F)
                newRecipe.addIngredient(ItemStack(Material.EMERALD_BLOCK, 4))
                newRecipe.addIngredient(ItemStack(Material.DIAMOND_BLOCK))
                newRecipes.add(newRecipe)
                villager.recipes = newRecipes
                villager.setTag("id", CustomEntity.MAX_FLETCHER.id)
                item.amount -= 1
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 5F, 1.4F)
                CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
            }

        }

    }

}
