package me.newburyminer.customItems.effects

interface EffectFactory {
    fun create(effectData: EffectData): EffectBehavior
}