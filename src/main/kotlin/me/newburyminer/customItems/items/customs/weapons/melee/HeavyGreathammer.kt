package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class HeavyGreathammer: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HEAVY_GREATHAMMER

    private val material = Material.NETHERITE_AXE
    private val color = arrayOf(87, 75, 62)
    private val name = text("Heavy Greathammer", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-3.5MA", "ATD+16.5MA")
        .loreList(lore)
        .cleanAttributeLore()
        .setTag("criticalcount", 0)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                val item = ctx.item ?: return
                if (!e.isCritical) return
                item.setTag("criticalcount", item.getTag<Int>("criticalcount")!!+1)
                if (item.getTag<Int>("criticalcount")!! % 3 != 0) return
                e.damage *= 2
                CustomEffects.playSound(e.entity.location, Sound.BLOCK_CALCITE_BREAK, 1.5F, 0.7F)
                CustomEffects.particle(Particle.CRIMSON_SPORE.builder(), e.entity.location, 20, 0.5, 0.5)
                if (e.entity !is Player) return
                for (armor in (e.entity as Player).inventory.armorContents) {
                    if (armor?.itemMeta?.isUnbreakable != true) armor?.reduceDura(10)
                }
            }

        }

    }

}