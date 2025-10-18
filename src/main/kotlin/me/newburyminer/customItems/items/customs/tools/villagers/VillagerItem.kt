package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Villager
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class VillagerItem: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.VILLAGER

    private val material = Material.CLAY_BALL
    private val color = arrayOf(125, 95, 60)
    private val name = text("Villager", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                if (e.action != Action.RIGHT_CLICK_BLOCK) return
                val item = ctx.item ?: return
                val loc = e.clickedBlock!!.location
                loc.add(Vector(0.5, 1.0, 0.5))
                val villagerAsString = item.getTag<String>("storedvillager")
                if (villagerAsString == null) loc.world.spawn(loc, Villager::class.java)
                else Bukkit.getEntityFactory().createEntitySnapshot(villagerAsString).createEntity(loc)
                item.amount -= 1
                CustomEffects.playSound(loc, Sound.ENTITY_VILLAGER_TRADE, 20F, 1.2F)
            }

        }

    }

}