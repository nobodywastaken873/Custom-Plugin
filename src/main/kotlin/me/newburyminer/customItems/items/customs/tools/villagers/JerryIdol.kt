package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
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
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class JerryIdol: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.JERRY_IDOL

    private val material = Material.EMERALD
    private val color = arrayOf(28, 148, 54)
    private val name = text("Jerry Idol", color)
    private val lore = Utils.loreBlockToList(
        text("Gives all players in a 50 block radius 1 level of hero of the village per 3 stacks of emerald blocks deposited. " +
                "Right click with a stack of emerald blocks to deposit, right click to place, with an empty hand to pickup.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.action != Action.RIGHT_CLICK_BLOCK) return
                val loc = e.clickedBlock!!.location
                loc.add(Vector(0.5, 1.0, 0.5))
                val villager: Villager = e.player.world.spawnEntity(loc, EntityType.VILLAGER) as Villager
                villager.setTag("id", CustomEntity.JERRY_IDOL.id)
                villager.setTag("source", CustomItem.JERRY_IDOL.name)
                val emeraldStacks = item.getTag<Int>("emeraldstacks") ?: 0
                villager.setTag("emeraldstacks", emeraldStacks)
                villager.getAttribute(Attribute.SCALE)!!.baseValue = emeraldStacks*0.1 + 1
                villager.isInvulnerable = true
                villager.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false))
                villager.setAI(false)
                item.amount -= 1
                CustomEffects.playSound(villager.location, Sound.ENTITY_VILLAGER_TRADE, 20F, 0.9F)
            }

            is PlayerInteractEntityEvent -> {
                if (ctx.itemType != EventItemType.SUMMONED_ENTITY) return
                if (e.rightClicked !is Villager) return
                e.isCancelled = true
                if (e.player.inventory.itemInMainHand.type == Material.AIR) {
                    val emBlockStacks = e.rightClicked.getTag<Int>("emeraldstacks")
                    val newJerryIdol = Items.get(CustomItem.JERRY_IDOL)
                    newJerryIdol.setTag("emeraldstacks", emBlockStacks)
                    e.player.addItemorDrop(newJerryIdol)
                    CustomEffects.playSound(e.rightClicked.location, Sound.ENTITY_ITEM_PICKUP, 20F, 0.5F)
                    e.rightClicked.remove()
                } else if (e.player.inventory.itemInMainHand.type == Material.EMERALD_BLOCK && e.player.inventory.itemInMainHand.amount == 64) {
                    val emBlockStacks = e.rightClicked.getTag<Int>("emeraldstacks") ?: 0
                    e.rightClicked.setTag("emeraldstacks", emBlockStacks + 1)
                    (e.rightClicked as Villager).getAttribute(Attribute.SCALE)!!.baseValue += 0.1
                    e.player.inventory.itemInMainHand.amount -= 64
                    for (i in 0..5+emBlockStacks/2) CustomEffects.particleCircle(Particle.HAPPY_VILLAGER.builder(), e.rightClicked.location.clone().add(Vector(0.0, i.toDouble()/2.5, 0.0)), 0.5 * (1 + emBlockStacks*0.1), (20 * (1 + emBlockStacks*0.1)).toInt(), 0.01)
                    CustomEffects.playSound(e.rightClicked.location, Sound.ENTITY_VILLAGER_YES, 20F, 1.5F)
                }
            }

        }

    }

}
