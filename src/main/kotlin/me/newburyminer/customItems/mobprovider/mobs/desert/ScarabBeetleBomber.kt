package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.creeper.CustomExplosionAbility
import me.newburyminer.customItems.mobprovider.ability.creeper.PreIgniteAbility
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

object ScarabBeetleBomber : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.CREEPER) {

        ability(
            CustomExplosionAbility(
                linear(2.0 to 4.0, ctx),
                false
            )
        )

        ability(
            PreIgniteAbility(linear(5.0 to 7.0, ctx))
        )

        health(
            linear(20.0 to 40.0, ctx)
        )

        movementSpeed(
            linear(1.25 to 1.5, ctx)
        )

        scale(0.6)

    }

}