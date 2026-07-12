package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.PullingBeamAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object GrapplingMaster : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.COPPER, TrimPattern.WARD)
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        ability(
            PullingBeamAbility(
                linear(1.0 to 2.0, ctx),
                200,
                10,
                ParticleTheme.COLD_OCEAN
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.0 to 5.0, ctx),
                1.0,
                ParticleTheme.COLD_OCEAN,
                20,
                VanillaEffectApply(PotionEffectType.SLOWNESS, 100, 1),
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                CustomKnockbackApply(Vector(-0.5, -0.5, -0.5))
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.4, ctx)
        )

        equipment {
            mainhand(Material.IRON_SWORD)
            offhand(Material.FISHING_ROD)
            setArmor(arrayOf(47, 64, 158), trim)
        }

    }

}