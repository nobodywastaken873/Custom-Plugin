package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.spell.DeathSummonAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType

object SickenedPirate : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            DeathSummonAbility(
                FattenedMaggot,
                linear(4 to 10, ctx)
            )
        )

        health(
            linear(40.0 to 70.0, ctx)
        )

        movementSpeed(
            linear(1.25 to 2.0, ctx)
        )

        equipment {
            mainhand(Material.AIR)
        }

    }

}