package me.newburyminer.customItems.mobprovider

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.entity
import me.newburyminer.customItems.entity.EntityWrapperManager
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

object MobFactory {

    fun create(builder: MobBuilder, ctx: MobContext): Entity {

        builder.abilities.forEach {
            with (it) {
                builder.apply(ctx)
            }
        }

        val entity = ctx.location.world.spawnEntity(ctx.location, builder.entityType, false) as LivingEntity
        val wrapper = EntityWrapperManager.getWrapperorNew(entity)

        // double check for no duplicate components of certain types
        builder.components.forEach { wrapper.addComponent(it) }

        entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue = builder.health
        entity.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = builder.movementSpeed
        entity.getAttribute(Attribute.ARMOR)?.baseValue = builder.armor

        builder.extraAttributes.forEach { (attribute, modifier) ->
            entity.getAttribute(attribute)?.addModifier(modifier)
        }

        builder.equipment.equipment.forEach { (slot, stack) ->
            entity.equipment?.setItem(slot, stack)
        }

        return entity

    }

}