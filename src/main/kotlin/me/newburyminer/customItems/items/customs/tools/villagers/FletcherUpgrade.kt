package me.newburyminer.customItems.items.customs.tools.villagers

import com.google.common.collect.Lists
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.convertVillagerLevel
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.entities.bosses.BossListeners
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Container
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

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
                val newRecipe = MerchantRecipe(Items.get(arrowTypes.random()), 0, 10000, true, 0, 0F)
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
