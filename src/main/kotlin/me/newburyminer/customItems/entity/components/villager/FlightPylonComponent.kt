package me.newburyminer.customItems.entity.components.villager

import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.TextDisplay
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.UUID

class FlightPylonComponent(private val block: UUID, private val text: UUID): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "block" to block.toString(),
            "text" to text.toString(),
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.FLIGHT_PYLON_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return FlightPylonComponent(
                UUID.fromString(map["block"].asString()),
                UUID.fromString(map["text"].asString()),
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(PlayerInteractAtEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.rightClicked == wrapper.entity
        },
        {e ->
            e.isCancelled = true
            if (e.player.inventory.itemInMainHand.type == Material.AIR) {

                val newJerryIdol = ItemRegistry.get(CustomItem.FLIGHT_PYLON)
                e.player.addItemorDrop(newJerryIdol)
                CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_ITEM_PICKUP, 1F, 0.5F)
                wrapper.entity.remove()
                Bukkit.getEntity(block)?.remove()
                Bukkit.getEntity(text)?.remove()

            }
        })
    }

    /*override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 60 == 0) {

            for (player in wrapper.entity.location.getNearbyPlayers(50.0, 128.0, 50.0)) {
                player.addPotionEffect(PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 340, emeraldStacks))
            }

        }
    }*/

}