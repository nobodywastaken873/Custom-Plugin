package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.villager.JerryIdolComponent
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.action == Action.RIGHT_CLICK_BLOCK
        },
        {e ->
            val loc = e.clickedBlock?.location ?: return@register
            loc.add(Vector(0.5, 1.0, 0.5))
            val villager: Villager = e.player.world.spawnEntity(loc, EntityType.VILLAGER) as Villager

            val emeraldStacks = e.item?.getTag<Int>("emeraldstacks") ?: 0
            val wrapper = EntityWrapperManager.getWrapperorNew(villager)
            wrapper.addComponent(JerryIdolComponent(emeraldStacks))
            wrapper.addComponent(NonPickuppableComponent())

            villager.getAttribute(Attribute.SCALE)!!.baseValue = emeraldStacks*0.1 + 1
            villager.isInvulnerable = true
            villager.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false))
            villager.setAI(false)

            val item = e.player.inventory.itemInMainHand
            item.amount -= 1
            CustomEffects.playSound(villager.location, Sound.ENTITY_VILLAGER_TRADE, 1F, 0.9F)
        })
    }

}