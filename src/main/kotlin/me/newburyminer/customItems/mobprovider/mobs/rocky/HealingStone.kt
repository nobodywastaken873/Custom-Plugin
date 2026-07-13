package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.components.spells.MobHealerComponent
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.entity.EntityType

object HealingStone : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SHULKER) {

        ability(
            HealerAbility(
                linear(20.0 to 30.0, ctx),
                linear(15.0 to 25.0, ctx),
                linear(5.0 to 10.0, ctx),
                linear(30 to 20, ctx),
                linear(150 to 100, ctx),
            )
        )

        health(
            linear(50.0 to 100.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        scale(0.8)

    }

}