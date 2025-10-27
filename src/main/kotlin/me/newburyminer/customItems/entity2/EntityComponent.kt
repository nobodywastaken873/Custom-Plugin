package me.newburyminer.customItems.entity2

import org.bukkit.entity.Entity

interface EntityComponent {

    fun handle(ctx: EntityEventContext) {}
    fun tick(entity: Entity) {}

}