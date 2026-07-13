package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.hasCustom
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.entity.components.villager.FlightPylonComponent
import me.newburyminer.customItems.entity.components.villager.JerryIdolComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Arrow
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.entity.Villager
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*

class FlightPylon: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FLIGHT_PYLON

    private val material = Material.FEATHER
    private val color = arrayOf(146, 223, 232)
    private val name = text("Flight Pylon", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to place down. Gives creative flight to players within a 30 block radius if they are not in combat.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.action == Action.RIGHT_CLICK_BLOCK
        },
        {e ->
            val loc = e.clickedBlock?.location ?: return@register
            //loc.add(Vector(0.5, 1.0, 0.5))

            val interaction = loc.world.spawn(loc.clone().add(0.5, 1.0, 0.5), Interaction::class.java) {
                it.interactionHeight = 2.0F
                it.interactionWidth = 1.0F
            }
            val display = loc.world.spawn(loc.clone().add(0.0, 1.0, 0.0), BlockDisplay::class.java) {
                it.block = Material.CHISELED_STONE_BRICKS.createBlockData()
                it.displayWidth = 1F
                it.displayHeight = 1F
                it.transformation = Transformation(
                    Vector3f(),
                    Quaternionf(),
                    Vector3f(1.0F, 2.0F, 1.0F),
                    Quaternionf()
                )
            }
            val text = loc.world.spawn(loc.clone().add(0.5, 3.2, 0.5), TextDisplay::class.java) {
                it.text(text("Flight Pylon", arrayOf(146, 223, 232)))
                it.billboard = Display.Billboard.CENTER
            }

            val wrapper = EntityWrapperManager.getWrapperorNew(interaction)
            wrapper.addComponent(
                FlightPylonComponent(display.uniqueId, text.uniqueId)
            )

            e.item?.amount -= 1
        })

        register(PlayerToggleFlightEvent::class, { e ->
            e.isFlying
        },
        {e ->
            val loc = e.player.location
            if (!isPylonClose(loc, e.player)) {
                e.isCancelled = true
                return@register
            }
            e.isCancelled = false
        })
    }
    private fun isPylonClose(loc: Location, player: Player): Boolean {
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return true
        val pylon = loc.getNearbyEntities(40.0, 40.0, 40.0).filter {
            it is Interaction &&
                    EntityWrapperManager.getWrapper(it.uniqueId)?.hasComponent(FlightPylonComponent::class) == true
        }.firstOrNull() ?: return false

        return pylon.location.subtract(loc).length() < 30.0
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(20 to {player -> updateFlightStatus(player)})

    private fun updateFlightStatus(player: Player) {

        if ((isPylonClose(player.location, player) && !player.isInCombat()) || player.gameMode == GameMode.CREATIVE) {
            player.allowFlight = true
        } else {
            player.allowFlight = false
            player.isFlying = false
        }

    }

}