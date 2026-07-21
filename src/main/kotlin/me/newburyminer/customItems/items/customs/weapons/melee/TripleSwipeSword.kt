package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.arcTraceManyEntities
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class TripleSwipeSword: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRIPLE_SWIPE_SWORD

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(230, 69, 77)
    private val name = text("Triple Swipe Sword", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.item?.setCooldown(e.player, 15.0)
            var k = 2
            //tasks.add()
            object : BukkitRunnable() { override fun run() {
                if (k == 0) this.cancel()

                val startLoc = e.player.eyeLocation.clone()
                val direction = e.player.location.direction.clone().normalize()
                val damage = DamageSettings(
                    30.0, DamageType.PLAYER_ATTACK, e.player, iframes = 3
                )
                val radius = 4.0
                val totalRadians = Math.toRadians(80.0)
                val toDamage = e.player.world.arcTraceManyEntities(e.player.eyeLocation, e.player.location.direction, radius, totalRadians,
                    {it != e.player})

                e.player.velocity = e.player.velocity.add(e.player.location.direction.normalize().multiply(0.55))

                for (entity in toDamage) {
                    (entity as? LivingEntity)?.applyDamage(damage)
                }

                CustomEffects.playSound(e.player.location, Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.2F)
                CustomEffects.rotatedArc(Particle.ENCHANTED_HIT.builder(), startLoc, radius, totalRadians, 5.0, direction)

                k--
            }}.runTaskTimer(CustomItems.plugin, 0L, 4L).taskId
        })
    }

}