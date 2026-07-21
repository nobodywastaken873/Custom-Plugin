package me.newburyminer.customItems.effects

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.effects.behaviors.DoubleChestLootEffect
import me.newburyminer.customItems.effects.behaviors.ElytraDisableEffect
import me.newburyminer.customItems.effects.factories.*
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.systems.playertask.PlayerTask
import me.newburyminer.customItems.systems.playertask.PlayerTaskHandler
import org.reflections.Reflections
import java.lang.reflect.Modifier
import kotlin.collections.component1
import kotlin.collections.component2

object CustomEffectBootstrapper {
    fun registerAll() {
        EffectRegistry.registerBulk(
            mapOf(
                CustomEffectType.ATTRIBUTE to AttributeEffectFactory(),
                CustomEffectType.ELYTRA_DISABLED to ElytraDisableEffectFactory(),
                CustomEffectType.ENDER_CRIT to EnderCritEffectFactory(),
                CustomEffectType.FANG_STAFF_VEXING to FangStaffVexingEffectFactory(),
                CustomEffectType.GRAVE_INVULNERABILITY to GraveInvulnerabilityEffectFactory(),
                CustomEffectType.LAST_PRISM_ZAP to LastPrismZapEffectFactory(),
                CustomEffectType.DOUBLE_GRAVE_LOOTING to DoubleGraveLootEffectFactory(),
                CustomEffectType.POCKET_WORMHOLE_REMAINING to PocketWormholeRemainingEffectFactory(),
                CustomEffectType.SURVIVAL_BUFFS to SurvivalBuffsEffectFactory(),
                CustomEffectType.CHEST_LOOT_BUFFS to DoubleChestLootEffectFactory(),
                CustomEffectType.QUADRUPLE_CHEST_LOOT to QuadrupleChestLootEffectFactory(),
                CustomEffectType.MOB_ENRAGED to MobEnragedEffectFactory(),
            )
        )

        val reflections: Reflections = Reflections("me.newburyminer.customItems.effects")
        reflections.getSubTypesOf(EffectBehavior::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && !it.isInterface }
            .forEach { cls ->
                val constructor = cls.declaredConstructors
                    .firstOrNull { it.parameterCount == 0 }
                    ?: return@forEach

                val instance = constructor.newInstance() as EffectBehavior
                instance.registerListeners()
            }
        CustomItems.plugin.logger.info("Successfully registered all effects")
    }
}