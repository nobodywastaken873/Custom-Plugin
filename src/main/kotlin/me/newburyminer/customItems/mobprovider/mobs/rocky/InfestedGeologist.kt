package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.DamageShieldAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object InfestedGeologist : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.QUARTZ, TrimPattern.WARD)
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.ZOMBIE) {

        ability(
            MeleeEffectAbility(
                damage(linear(28.0 to 56.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.2 to 0.4, ctx),
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(1.4 to 1.8, ctx)
        )

        equipment {
            mainhand(Material.IRON_PICKAXE)
            offhand(Material.AIR)
            setArmor(arrayOf(80, 82, 89), trim)
        }

    }

}