package me.newburyminer.customItems.mobprovider.mobs.caves

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.MultiMissileAbility
import org.bukkit.entity.Bat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Warden

object CaveDweller: MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.CAVES
    override val tier: MobTier = MobTier.ELITE
    override val targetRange: Double = 80.0
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WARDEN) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            MultiMissileAbility(
                linear(100.0 to 120.0, ctx),
                linear(2 to 4, ctx),
                linear(25.0 to 50.0, ctx),
                CustomKnockbackApply(-1.5, 0.5, -1.5),
                linear(30.0 to 40.0, ctx),
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                ParticleTheme.SURFACE,
            )
        )

        health(
            linear(400.0 to 800.0, ctx)
        )

        movementSpeed(
            linear(1.5 to 2.0, ctx)
        )

        apply {
            if (this !is Warden) return@apply
            val target = this.world.spawn(this.location, Bat::class.java)
            this.setAnger(target, 100)
            target.remove()
        }

    }

}