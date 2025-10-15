package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class TripleSwipeSword: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRIPLE_SWIPE_SWORD

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(230, 69, 77)
    private val name = text("Triple Swipe Sword", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()


    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (!item.offCooldown(e.player)) return
                if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
                item.setCooldown(e.player, 15.0)
                var k = 2
                //tasks.add()
                object : BukkitRunnable() { override fun run() {
                    if (k == 0) this.cancel()

                    val startLoc = e.player.eyeLocation.clone()
                    val direction = e.player.location.direction.clone().normalize()
                    val damage = DamageSettings(
                        30.0, DamageType.PLAYER_ATTACK, e.player, iframes = 3
                    )
                    val radius = 4.0
                    val totalDegrees = 80.0
                    val toDamage = mutableSetOf<UUID>()

                    for (i in 0..totalDegrees.toInt()) {
                        val currentDegree = -totalDegrees / 2 + i
                        val currentRad = Math.toRadians(currentDegree)
                        val vect = Vector(cos(currentRad), 0.0, sin(currentRad)).rotateToAxis(direction)
                        val unit = vect.normalize().multiply(0.1)

                        val currentLoc = startLoc.clone()
                        for (j in 0..(radius * 10).toInt()) {
                            currentLoc.add(unit)
                            for (entity in currentLoc.getNearbyEntities(1.0, 1.0, 1.0)) {
                                if (entity == e.player) continue
                                if (entity !is LivingEntity) continue
                                if (entity.boundingBox.containsLoc(currentLoc, entity.world)) {
                                    toDamage.add(entity.uniqueId)
                                }
                            }
                        }
                    }

                    e.player.velocity = e.player.velocity.add(e.player.location.direction.normalize().multiply(0.55))

                    for (entity in toDamage) {
                        (Bukkit.getEntity(entity) as LivingEntity?)?.applyDamage(damage)
                    }

                    CustomEffects.playSound(e.player.location, Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.2F)
                    CustomEffects.rotatedArc(Particle.ENCHANTED_HIT.builder(), startLoc, radius, totalDegrees, (Math.PI * radius.pow(2) * (totalDegrees/360.0) * 50).toInt(), direction, Utils.randomRange(-0.25, 0.25))

                    k--
                }}.runTaskTimer(CustomItems.plugin, 0L, 4L).taskId
            }

        }

    }

}