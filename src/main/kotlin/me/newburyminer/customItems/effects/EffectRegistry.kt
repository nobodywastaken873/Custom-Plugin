package me.newburyminer.customItems.effects

object EffectRegistry {
    val effects: MutableMap<CustomEffectType, EffectFactory> = mutableMapOf()

    fun register(type: CustomEffectType, factory: EffectFactory) {
        effects[type] = factory
    }

    fun registerBulk(map: Map<CustomEffectType, EffectFactory>) {
        map.forEach { register(it.key, it.value) }
    }

    fun create(type: CustomEffectType, data: EffectData): EffectBehavior? {
        val factory = effects[type] ?: return null
        val behavior = factory.create(data)
        return behavior
    }

}