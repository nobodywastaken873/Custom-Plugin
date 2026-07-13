package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.entity.EntityType

object BattleMedic : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.EVOKER) {

        ability(
            HealerAbility(
                linear(14.0 to 28.0, ctx),
                linear(20.0 to 25.0, ctx),
                linear(10.0 to 15.0, ctx),
                linear(40 to 35, ctx),
                linear(200 to 150, ctx),
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.2 to 0.4, ctx),
                linear(150 to 100, ctx),
                0.8
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.2 to 0.4, ctx)
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.6 to 1.9, ctx)
        )

    }

}