package me.newburyminer.customItems.effects

import me.newburyminer.customItems.effects.behaviors.ElytraDisableEffect
import me.newburyminer.customItems.effects.factories.AttributeEffectFactory
import me.newburyminer.customItems.effects.factories.ElytraDisableEffectFactory
import me.newburyminer.customItems.effects.factories.EnderCritEffectFactory

object CustomEffectBootstrapper {
    fun registerAll() {
        EffectRegistry.registerBulk(
            mapOf(
                CustomEffectType.ATTRIBUTE to AttributeEffectFactory(),
                CustomEffectType.ELYTRA_DISABLED to ElytraDisableEffectFactory(),
                CustomEffectType.ENDER_CRIT to EnderCritEffectFactory()
            )
        )

        EffectEventHandler.register(CustomEffectType.ELYTRA_DISABLED, ElytraDisableEffect())

    }
}