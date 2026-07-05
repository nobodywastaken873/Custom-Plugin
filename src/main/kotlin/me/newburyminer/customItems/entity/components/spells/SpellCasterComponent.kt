package me.newburyminer.customItems.entity.components.spells

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.inventory.EquipmentSlot
import kotlin.math.pow

class SpellCasterComponent(private val slowdown: Double = 0.0): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "slowdown" to slowdown,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SPELL_CASTER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SpellCasterComponent(
                map["slowdown"].asDouble(),
            )
        }
    }

    private val nameKey = NamespacedKey(CustomItems.plugin, "spell_slowdown")
    override fun onCast(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? LivingEntity ?: return
        caster.getAttribute(Attribute.MOVEMENT_SPEED)?.addModifier(
            AttributeModifier(
                nameKey, slowdown, AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun onFinishCast(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? LivingEntity ?: return
        caster.getAttribute(Attribute.MOVEMENT_SPEED)?.removeModifier(nameKey)
    }

    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.isCasting() && wrapper.entity.ticksLived % 3 == 0) {

            val caster = wrapper.entity as? LivingEntity ?: return

            val castingComponent =
                wrapper.getComponentsExtending(AbstractSpellComponent::class)
                    .firstOrNull { it.castingTicks > 0 } ?: return

            val currentFraction = castingComponent.castingTicks.toDouble() / castingComponent.spellDuration
            val colorInt = ( (1 - currentFraction) * (255) ).toInt().coerceIn(0, 255)
            ParticleBuilder(Particle.DUST)
                .color(colorInt, colorInt, colorInt)
                .location(caster.location.add(0.0, 0.3 + caster.height, 0.0))
                .receivers(80)
                .spawn()
        }
    }
}