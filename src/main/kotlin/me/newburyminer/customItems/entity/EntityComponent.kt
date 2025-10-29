package me.newburyminer.customItems.entity

interface EntityComponent {

    val componentType: EntityComponentType
    fun serialize(): Map<String, Any>
    fun deserialize(map: Map<String, Any>): EntityComponent?
    fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {}
    fun tick(wrapper: EntityWrapper) {}

}