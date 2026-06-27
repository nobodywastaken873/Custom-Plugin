package me.newburyminer.customItems.mobprovider

import org.bukkit.entity.Entity

class MobProvider(
    val entries: List<MobEntry>
) {

    fun new(context: MobContext): Entity {

        // Need proper selector here + check that a certain mob definition fits within selected location (does not intersect blocks)
        val definition = entries.random().definition
        val builder = definition.build(context)

        return MobFactory.create(builder, context)

    }

}