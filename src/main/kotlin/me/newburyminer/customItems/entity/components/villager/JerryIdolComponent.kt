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
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class JerryIdolComponent(private var emeraldStacks: Int): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "emeraldStacks" to emeraldStacks
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.JERRY_IDOL_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return JerryIdolComponent(
                map["emeraldStacks"].toInt(),
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

                val newJerryIdol = ItemRegistry.get(CustomItem.JERRY_IDOL)
                newJerryIdol.setTag("emeraldstacks", emeraldStacks)
                e.player.addItemorDrop(newJerryIdol)
                CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_ITEM_PICKUP, 20F, 0.5F)
                wrapper.entity.remove()

            }

            else if (e.player.inventory.itemInMainHand.type == Material.EMERALD_BLOCK && e.player.inventory.itemInMainHand.amount == 64) {

                emeraldStacks++
                (wrapper.entity as Villager).getAttribute(Attribute.SCALE)!!.baseValue += 0.1
                e.player.inventory.itemInMainHand.amount -= 64

                for (i in 0..5+emeraldStacks/2) {
                    CustomEffects.particleCircle(Particle.HAPPY_VILLAGER.builder(),
                        wrapper.entity.location.clone().add(Vector(0.0, i.toDouble()/2.5, 0.0)),
                        0.5 * (1 + emeraldStacks*0.1),
                        (20 * (1 + emeraldStacks*0.1)).toInt(), 0.01)
                }
                CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_VILLAGER_YES, 20F, 1.5F)

            }
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {

                if (e.rightClicked !is Villager) return
                e.isCancelled = true
                if (e.player.inventory.itemInMainHand.type == Material.AIR) {

                    val newJerryIdol = ItemRegistry.get(CustomItem.JERRY_IDOL)
                    newJerryIdol.setTag("emeraldstacks", emeraldStacks)
                    e.player.addItemorDrop(newJerryIdol)
                    CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_ITEM_PICKUP, 20F, 0.5F)
                    wrapper.entity.remove()

                }

                else if (e.player.inventory.itemInMainHand.type == Material.EMERALD_BLOCK && e.player.inventory.itemInMainHand.amount == 64) {

                    emeraldStacks++
                    (wrapper.entity as Villager).getAttribute(Attribute.SCALE)!!.baseValue += 0.1
                    e.player.inventory.itemInMainHand.amount -= 64

                    for (i in 0..5+emeraldStacks/2) {
                        CustomEffects.particleCircle(Particle.HAPPY_VILLAGER.builder(),
                            wrapper.entity.location.clone().add(Vector(0.0, i.toDouble()/2.5, 0.0)),
                            0.5 * (1 + emeraldStacks*0.1),
                            (20 * (1 + emeraldStacks*0.1)).toInt(), 0.01)
                    }
                    CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_VILLAGER_YES, 20F, 1.5F)

                }

            }

        }
    }*/

    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 60 == 0) {

            for (player in wrapper.entity.location.getNearbyPlayers(50.0, 128.0, 50.0)) {
                player.addPotionEffect(PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 340, emeraldStacks))
            }

        }
    }

}