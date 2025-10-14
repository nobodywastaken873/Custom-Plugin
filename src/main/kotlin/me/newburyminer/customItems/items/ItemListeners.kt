package me.newburyminer.customItems.items

import com.destroystokyo.paper.MaterialTags
import com.destroystokyo.paper.ParticleBuilder
import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import com.google.common.collect.Lists
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import io.papermc.paper.event.player.PlayerItemGroupCooldownEvent
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.clearCrossbowProj
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.convertVillagerLevel
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.firework
import me.newburyminer.customItems.Utils.Companion.fireworkBooster
import me.newburyminer.customItems.Utils.Companion.getCorners
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getHitboxCorners
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.hasCustom
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.removeAttr
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.resist
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.Utils.Companion.serializeAsBytes
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.timeSinceCombatTimeStamp
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.gui.GraveHolder
import me.newburyminer.customItems.gui.GuiInventory
import me.newburyminer.customItems.gui.ShulkerHolder
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.damage.DamageSettings
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Container
import org.bukkit.block.ShulkerBox
import org.bukkit.block.data.Ageable
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.inventory.ComplexRecipe
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.*

class ItemListeners: Listener, Runnable {

    //@EventHandler fun onMapFind(e: Map)
    /*@EventHandler fun onTradeAdd(e: VillagerAcquireTradeEvent) {
        if (e.recipe.result.type == Material.FILLED_MAP) {
            e.recipe = MerchantRecipe(ItemStack(Material.MAP), 12)
        }
    }*/

    @EventHandler fun onEntityTakeDamage(e: EntityDamageEvent) {
        assassinsCancel(e)
    }
    private fun assassinsCancel(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        val fullSet = (
            player.inventory.helmet?.isItem(CustomItem.ASSASSINS_HOOD) == true &&
            player.inventory.chestplate?.isItem(CustomItem.ASSASSINS_ROBE) == true &&
            player.inventory.leggings?.isItem(CustomItem.ASSASSINS_LEGGINGS) == true &&
            player.inventory.boots?.isItem(CustomItem.ASSASSINS_LOAFERS) == true
        )
        if (!fullSet) return
        player.setTag("assassinsstep", 0)
        if (player.getAttribute(Attribute.MOVEMENT_SPEED)!!.getModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed")) != null) {
            player.getAttribute(Attribute.MOVEMENT_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"))
            player.getAttribute(Attribute.ATTACK_DAMAGE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"))
            player.getAttribute(Attribute.ATTACK_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"))
            player.getAttribute(Attribute.SCALE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"))
        }
    }

    // Should also be in a different systems file or smth
    @EventHandler fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.getAttribute(Attribute.MAX_ABSORPTION)!!.baseValue = 2048.0
    }

    /*@EventHandler fun onEnemyAggro(e: EntityTargetEvent) {
        if (e.target !is Player) return
        for (player in e.target!!.location.getNearbyPlayers(40.0)) {
            if (player.inventory.leggings?.isItem(CustomItem.ENCRUSTED_PANTS) != true) continue
            e.target = player
        }
    }*/

    @EventHandler fun onPlayerSneak(e: PlayerToggleSneakEvent) {
        //xrayGoggles(e)
        //aqueousSandals(e)
        tankSetActivate(e)
        //mechanizedElytraActivate(e)
    }
    /*private fun xrayGoggles(e: PlayerToggleSneakEvent) {
        if (!e.isSneaking) return
        if (e.player.inventory.helmet?.isItem(CustomItem.XRAY_GOGGLES) != true) return
        if (!e.player.offCooldown(CustomItem.XRAY_GOGGLES)) return
        for (entity in e.player.location.getNearbyEntities(20.0, 20.0, 20.0)) {
            if (e.player.location.subtract(entity.location).length() > 20.0) continue
            if (entity !is LivingEntity) continue
            if (entity == e.player) continue
            entity.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 400, 0, true, false, false))
        }
        e.player.setCooldown(CustomItem.XRAY_GOGGLES, 20.0)
        CustomEffects.playSound(e.player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
    }*/
    /*private fun aqueousSandals(e: PlayerToggleSneakEvent) {
        if (!e.isSneaking) return
        if (e.player.inventory.boots?.isItem(CustomItem.AQUEOUS_SANDALS) != true) return
        if (!e.player.offCooldown(CustomItem.AQUEOUS_SANDALS)) return
        e.player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 0, true, true, true))
        e.player.setCooldown(CustomItem.AQUEOUS_SANDALS, 25.0)
        CustomEffects.playSound(e.player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
    }*/
    private fun tankSetActivate(e: PlayerToggleSneakEvent) {
        if (!e.isSneaking) return
        val fullSet = (
            e.player.inventory.helmet?.isItem(CustomItem.HARD_HAT) == true &&
            e.player.inventory.chestplate?.isItem(CustomItem.TURTLE_SHELL) == true &&
            e.player.inventory.leggings?.isItem(CustomItem.ENCRUSTED_PANTS) == true &&
            e.player.inventory.boots?.isItem(CustomItem.STEEL_TOED_BOOTS) == true
        )
        if (!fullSet) return
        if (!e.player.offCooldown(CustomItem.TURTLE_SHELL)) return
        e.player.absorptionAmount = (e.player.absorptionAmount + 20).coerceAtMost(20.0)
        for (custom in arrayOf(CustomItem.HARD_HAT, CustomItem.TURTLE_SHELL, CustomItem.ENCRUSTED_PANTS, CustomItem.STEEL_TOED_BOOTS)) {
            e.player.setCooldown(custom, 60.0)
        }
        CustomEffects.playSound(e.player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
    }
    /*private fun mechanizedElytraActivate(e: PlayerToggleSneakEvent) {
        if (!e.isSneaking) return
        if (!e.player.isGliding) return
        if (e.player.inventory.chestplate?.isItem(CustomItem.MECHANIZED_ELYTRA) != true) return
        if (!e.player.offCooldown(CustomItem.MECHANIZED_ELYTRA, "Boost")) return
        e.player.setCooldown(CustomItem.MECHANIZED_ELYTRA, 10.0, "Boost")
        e.player.fireworkBoost(ItemStack(Material.FIREWORK_ROCKET).fireworkBooster(1))
    }*/

    /*@EventHandler fun onTotemPop(e: EntityResurrectEvent) {
        shadowLegs(e)
        repellantPants(e)
    }
    private fun shadowLegs(e: EntityResurrectEvent) {
        if (e.isCancelled) return
        if (e.entity !is Player) return
        if ((e.entity as Player).inventory.leggings?.isItem(CustomItem.SHADOW_LEGS) != true) return
        if (!(e.entity as Player).offCooldown(CustomItem.SHADOW_LEGS)) return
        (e.entity as Player).setCooldown(CustomItem.SHADOW_LEGS, 60.0)
        val duration = if ((e.entity as Player).inventory.helmet?.isItem(CustomItem.DRINKING_HAT) == true) 1000 else 500
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            (e.entity as Player).addPotionEffects(mutableListOf(
                PotionEffect(PotionEffectType.RESISTANCE, duration, 1),
                PotionEffect(PotionEffectType.STRENGTH, duration, 2),
                PotionEffect(PotionEffectType.SPEED, duration, 2),
                PotionEffect(PotionEffectType.REGENERATION, duration, 2)
            ))
        })
    }
    private fun repellantPants(e: EntityResurrectEvent) {
        if (e.isCancelled) return
        if (e.entity !is Player) return
        if ((e.entity as Player).inventory.leggings?.isItem(CustomItem.REPELLANT_PANTS) != true) return
        for (entity in e.entity.location.getNearbyEntities(8.0, 8.0, 8.0)) {
            if (entity == e.entity) continue
            entity.velocity = entity.velocity.add(entity.location.subtract(e.entity.location).toVector().normalize().multiply(3).add(Vector(0.0, 1.0, 0.0)))
        }
    }*/

    /*@EventHandler fun onPotionApply(e: EntityPotionEffectEvent) {
        //drinkingCapDouble(e)
        debuffSetBlock(e)
    }
    /*private fun drinkingCapDouble(e: EntityPotionEffectEvent) {
        if (e.entity !is Player) return
        if ((e.entity as Player).inventory.helmet?.isItem(CustomItem.DRINKING_HAT) != true) return
        if (e.cause !in arrayOf(
                EntityPotionEffectEvent.Cause.POTION_SPLASH,
                EntityPotionEffectEvent.Cause.POTION_DRINK,
                EntityPotionEffectEvent.Cause.FOOD,
                EntityPotionEffectEvent.Cause.TOTEM,
        )) return
        if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
        if (e.newEffect == null) return
        e.isCancelled = true
        (e.entity as Player).addPotionEffect(PotionEffect(e.newEffect!!.type, e.newEffect!!.duration * 2, e.newEffect!!.amplifier, e.newEffect!!.isAmbient, e.newEffect!!.hasParticles()))
    }*/
    private fun debuffSetBlock(e: EntityPotionEffectEvent) {
        if (e.entity !is Player) return
        if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
        val player = e.entity as Player
        val fullSet = (
            player.inventory.helmet?.isItem(CustomItem.WELDING_HELMET) == true &&
            player.inventory.chestplate?.isItem(CustomItem.ANTI_VENOM_SHIRT) == true &&
            player.inventory.leggings?.isItem(CustomItem.ENERGY_RESTORING_PANTS) == true &&
            player.inventory.boots?.isItem(CustomItem.STABILZING_SNEAKERS) == true
        )
        //if ((e.entity as Player).inventory.helmet?.isItem(CustomItem.WELDING_HELMET) != true) return
        if (e.newEffect!!.type !in arrayOf(
            PotionEffectType.SLOWNESS, PotionEffectType.MINING_FATIGUE, PotionEffectType.NAUSEA, PotionEffectType.BLINDNESS,
            PotionEffectType.HUNGER, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.WITHER,
            PotionEffectType.LEVITATION, PotionEffectType.SLOW_FALLING, PotionEffectType.DARKNESS
        )) return
        if (fullSet) {
            e.isCancelled = true
            val oldPot = e.newEffect!!
            player.addPotionEffect(PotionEffect(Utils.flipPotion(oldPot.type), oldPot.duration * 3, oldPot.amplifier + 1))
        }
        else if (player.inventory.helmet?.isItem(CustomItem.WELDING_HELMET) == true &&
            e.newEffect!!.type in arrayOf(
                PotionEffectType.NAUSEA, PotionEffectType.BLINDNESS, PotionEffectType.DARKNESS
            )) {
            e.isCancelled = true
        } else if (player.inventory.chestplate?.isItem(CustomItem.ANTI_VENOM_SHIRT) == true &&
            e.newEffect!!.type in arrayOf(
                PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.WITHER
            )) {
            e.isCancelled = true
        } else if (player.inventory.leggings?.isItem(CustomItem.ENERGY_RESTORING_PANTS) == true &&
            e.newEffect!!.type in arrayOf(
                PotionEffectType.SLOWNESS, PotionEffectType.WEAKNESS, PotionEffectType.MINING_FATIGUE
            )) {
            e.isCancelled = true
        } else if (player.inventory.boots?.isItem(CustomItem.STABILZING_SNEAKERS) == true &&
            e.newEffect!!.type in arrayOf(
                PotionEffectType.LEVITATION, PotionEffectType.SLOW_FALLING
            )) {
            e.isCancelled = true
        }
        //e.isCancelled = true
    }*/

    /*@EventHandler fun onEntityDespawn(e: EntityRemoveEvent) {
        cancelLandmineDespawn(e)
    }
    private fun cancelLandmineDespawn(e: EntityRemoveEvent) {
        if (e.entity.type != EntityType.ARROW) return
        if (e.entity.getTag<Int>("id") != CustomEntity.LANDMINE_SHOT.id) return
        if (e.cause != EntityRemoveEvent.Cause.DESPAWN) return
        e.entity.world.spawn(e.entity.location, Arrow::class.java, CreatureSpawnEvent.SpawnReason.CUSTOM) {
            it.setTag("id", CustomEntity.LANDMINE_SHOT.id)
            it.color = (e.entity as Arrow).color
        }
    }*/

    /*@EventHandler fun onPlayerMove(e: PlayerMoveEvent) {
        restoreFlight(e)
    }
    private fun restoreFlight(e: PlayerMoveEvent) {
        if (e.player.inventory.boots?.getCustom() != CustomItem.DOUBLE_JUMP_BOOTS) {
            e.player.allowFlight = !(e.player.gameMode == GameMode.SURVIVAL || e.player.gameMode == GameMode.ADVENTURE)
            return
        }
        if (!e.player.isOnGround) return
        if (!e.player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return
        e.player.allowFlight = true
    }
    @EventHandler fun onPlayerStartFly(e: PlayerToggleFlightEvent) {
        doubleJumpBoots(e)
    }
    private fun doubleJumpBoots(e: PlayerToggleFlightEvent) {
        if (e.player.inventory.boots?.getCustom() != CustomItem.DOUBLE_JUMP_BOOTS) return
        if (e.player.gameMode == GameMode.CREATIVE) return
        e.isCancelled = true
        if (!e.player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return
        e.player.allowFlight = false
        e.player.velocity = e.player.location.direction.multiply(1.0).setY(0.7)
        e.player.setCooldown(CustomItem.DOUBLE_JUMP_BOOTS, 5.0)
    }*/

    // should be in a broader systems file
    @EventHandler fun onCooldownSet(e: PlayerItemGroupCooldownEvent) {
        if (e.cooldownGroup.namespace != "customitems") return
        if (e.cooldown != 1) return
        e.isCancelled = true
    }

    // one specific tag-based debuff, maybe need an actual debuff system
    @EventHandler fun onPlayerGlide(e: EntityToggleGlideEvent) {
        if (!e.isGliding) return
        if (e.entity.getTag<Int>("elytradisabled") == 0 || e.entity.getTag<Int>("elytradisabled") == null) return
        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            (e.entity as Player).isGliding = false
        }, 1)
    }

    // item cycling functionality
    /*@EventHandler fun onPlayerSwapHeldSlot(e: PlayerItemHeldEvent) {
        //cycleRedstonePlacers(e)
    }
    /*private fun cycleRedstonePlacers(e: PlayerItemHeldEvent) {
        val slotDiff = e.previousSlot - e.newSlot
        if (abs(slotDiff) != 1 && abs(slotDiff) != 8) return
        val item = e.player.inventory.getItem(e.previousSlot) ?: return
        if (item.getCustom() !in arrayOf(
            CustomItem.INPUT_DEVICES, CustomItem.MINECART_MATERIALS, CustomItem.ACTUAL_REDSTONE,
            CustomItem.CONTAINERS, CustomItem.REDSTONE_AMALGAMATION
        )) return
        if (!e.player.isSneaking) return
        e.isCancelled = true
        val movement = if (slotDiff == -1 || slotDiff == 8) 1 else -1
        // basically scroll check if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        //0 = input devices, 1 = minecart materials, 2 = actual redstone, 3 = containers
        val outerLayer = item.getTag<Int>("redstonegroup")!!
        val innerLayer = item.getTag<Int>("redstoneitem")!!
        //input devices : stone button, wood button, lever, wood/stone/weighted pressure plate, minecart stuff: detector rail, rail, powered rail, activator rail, minecart, hopper minecart, chest minecart
        //redstone components: redstone dust, block, repeater, comparator,  torch, observer, containers and other: hopper, barrel, chest, crafter, dispenser, dropper, note block, piston, sticky piston, slime block
        val order: Array<Material> = getRedstoneItems(outerLayer)
        var newIndex = innerLayer + movement
        newIndex = if (newIndex == -1) order.lastIndex else if (newIndex == order.size) 0 else newIndex
        item.type = order[newIndex]
        item.setTag("redstoneitem", newIndex)
        if (item.isItem(CustomItem.REDSTONE_AMALGAMATION)) {
            val prevOrders = item.getTag<IntArray>("storedinner")!!
            prevOrders[outerLayer] = newIndex
            item.setTag("storedinner", prevOrders)
        }
        CustomEffects.playSound(e.player.location, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
    }
    private fun getRedstoneItems(outerLayer: Int): Array<Material> {
        return when (outerLayer) {
            0 -> arrayOf(Material.STONE_BUTTON, Material.OAK_BUTTON, Material.LEVER, Material.OAK_PRESSURE_PLATE,
                Material.STONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
            1 -> arrayOf(Material.MINECART, Material.DETECTOR_RAIL, Material.RAIL, Material.POWERED_RAIL,
                Material.ACTIVATOR_RAIL, Material.HOPPER_MINECART, Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.TNT_MINECART)
            2 -> arrayOf(Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REPEATER, Material.COMPARATOR,
                Material.REDSTONE_TORCH, Material.OBSERVER)
            else -> arrayOf(Material.BARREL, Material.HOPPER, Material.CHEST, Material.CRAFTER,
                Material.DISPENSER, Material.DROPPER, Material.NOTE_BLOCK, Material.PISTON, Material.STICKY_PISTON, Material.SLIME_BLOCK)
        }
    }*/*/

    // both inventory-based items entirely, maybe move to inventory events
    @EventHandler fun onInventoryClick(e: InventoryClickEvent) {
        //enderNode(e)
        openShulker(e)
    }
    /*private fun enderNode(e: InventoryClickEvent) {
        if ((e.whoClicked as Player).isBeingTracked()) return
        val player = e.inventory.viewers.first() as Player
        if (e.slot < 0) return
        if (e.clickedInventory?.getItem(e.slot)?.isItem(CustomItem.ENDER_NODE) != true) return
        if (e.inventory.holder is GuiInventory) return
        if (e.action != InventoryAction.PICKUP_HALF) return
        e.isCancelled = true
        if (e.inventory.type == InventoryType.ENDER_CHEST) return
        CustomEffects.playSound(player.location, Sound.BLOCK_ENDER_CHEST_OPEN, 0.5F, (1.0F - Math.random() * 0.1F).toFloat())
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            player.closeInventory()
            player.openInventory(player.enderChest)
        })
    }*/
    private fun openShulker(e: InventoryClickEvent) {
        if (e.action != InventoryAction.PICKUP_HALF) return
        if (e.whoClicked.getTag<Boolean>("inventoryshulker") != true) return
        if (e.clickedInventory?.type !in
            arrayOf(InventoryType.ENDER_CHEST, InventoryType.PLAYER)
            ) return
        if (!Tag.SHULKER_BOXES.isTagged(e.clickedInventory!!.getItem(e.slot)?.type ?: Material.AIR)) return
        e.isCancelled = true
        val shulker = e.clickedInventory!!.getItem(e.slot)!!
        if (shulker.getTag<Boolean>("shulkeropen") == true) {
            CustomEffects.playSound(e.whoClicked.location, Sound.ENTITY_SHULKER_HURT, 1.0F, 1.2F)
            return
        }
        CustomEffects.playSound(e.whoClicked.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F)
        val player = e.whoClicked as Player
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            shulker.setTag("shulkeropen", true)
            player.closeInventory()
            player.openInventory(ShulkerHolder(shulker).inventory)
        })
    }

    // item eat event
    /*@EventHandler fun onFoodEat(e: PlayerItemConsumeEvent) {
        mysticalGreenApple(e)
        shulkerFruit(e)
    }
    private fun mysticalGreenApple(e: PlayerItemConsumeEvent) {
        if (!e.item.isItem(CustomItem.MYSTICAL_GREEN_APPLE)) return
        if ((e.player.getTag<Int>("experiencekept") ?: 0) == 4) {
            e.isCancelled = true
            e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
            return
        }
        e.player.setTag("experiencekept", (e.player.getTag<Int>("experiencekept") ?: 0) + 1)
        CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
    }
    private fun shulkerFruit(e: PlayerItemConsumeEvent) {
        if (!e.item.isItem(CustomItem.SHULKER_FRUIT)) return
        if (e.player.getTag<Boolean>("inventoryshulker") == true) {
            e.isCancelled = true
            e.player.sendActionBar(text("Max amount already consumed", Utils.FAILED_COLOR))
            return
        }
        e.player.setTag("inventoryshulker", true)
        CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.4F)
    }*/

    // another direct weapon hit
    /*@EventHandler fun knockbackEntity(e: EntityKnockbackByEntityEvent) {
        hookedCutlass(e)
    }
    private fun hookedCutlass(e: EntityKnockbackByEntityEvent) {
        if (e.hitBy !is Player) return
        val player = e.hitBy as Player
        if (!player.inventory.itemInMainHand.isItem(CustomItem.HOOKED_CUTLASS)) return
        //e.knockback = e.knockback.clone().multiply(-1)
        // or
        val newKnockback = e.knockback.clone()
        newKnockback.x *= -1
        newKnockback.z *= -1
        e.knockback = newKnockback
    }*/

    // all grave handling functions
    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        createGrave(e)
    }
    private fun createGrave(e: PlayerDeathEvent) {
        // soulbound, perm consumable to keep xp
        // change settings for custom worlds
        // prevent chest from being broken,
        if (e.player.world == CustomItems.bossWorld) {
            e.keepInventory = true
            e.keepLevel = true
            e.drops.clear()
            e.droppedExp = 0
            return
        }
        if (e.player.world != Bukkit.getServer().worlds[0] && e.player.world != Bukkit.getServer().worlds[1]
            && e.player.world != Bukkit.getServer().worlds[2] && e.player.world != CustomItems.aridWorld) return
        if (e.isCancelled) return
        if ((e.player.getTag<Int>("experiencekept") ?: 0) != 0) {
            val extraExp = e.player.exp * (Utils.toExpAmount(e.player.level + 1) - Utils.toExpAmount(e.player.level))
            val totalExp = (Utils.toExpAmount(e.player.level, extraExp.toInt())) * (e.player.getTag<Int>("experiencekept")!! / 4.0)
            val newAmounts = Utils.toExpLevel(totalExp.toInt())
            e.newLevel = newAmounts.first
            e.newExp = newAmounts.second
            e.newTotalExp = totalExp.toInt()
            e.setShouldDropExperience(false)
        }
        if (e.drops.size == 0) return
        // finding location
        val loc = e.player.location
        loc.y = if (e.player.world == Bukkit.getServer().worlds[0] && loc.y < -64.0) -64.0 else loc.y
        loc.y = if (e.player.world != Bukkit.getServer().worlds[0] && loc.y < 0.0) 0.0 else loc.y
        if (loc.world.getBlockAt(loc).type != Material.AIR && loc.world.getBlockAt(loc).type != Material.WATER) {
            while (loc.world.getBlockAt(loc).type != Material.AIR && loc.world.getBlockAt(loc).type != Material.WATER) {
                loc.y += 1
                if (loc.y >= 255) {
                    e.keepInventory = true
                    e.drops.clear()
                    return
                }
            }
        } else {
            while (loc.world.getBlockAt(loc).type == Material.AIR || loc.world.getBlockAt(loc).type == Material.WATER) {
                loc.y -= 1
            }
            loc.y += 1
        }
        val graveList = (e.player.getListTag<Location>("gravelist") ?: listOf()).toMutableList()
        graveList.add(loc.world.getBlockAt(loc).location)
        e.player.setListTag("gravelist", graveList)
        val chestDisplay: BlockDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location, BlockDisplay::class.java)
        val armorStand: Interaction = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 0.0, 0.5), Interaction::class.java) {
            it.interactionHeight = 1.0F
            it.interactionWidth = 1.0F
        }
        val textDisplay: TextDisplay = e.player.world.spawn(loc.world.getBlockAt(loc).location.add(0.5, 1.2, 0.5), TextDisplay::class.java)
        // remove anything not to be dropped here
        var helmet: ItemStack? = null
        var chestplate: ItemStack? = null
        var leggings: ItemStack? = null
        var boots: ItemStack? = null
        var offhand: ItemStack? = null
        val drops = e.drops.toMutableList()
        if (e.player.inventory.itemInOffHand.type != Material.AIR) offhand = drops.removeLast()
        for (item in drops) {
            if (CustomEnchantments.SOULBOUND in item.enchantments) {e.itemsToKeep.add(item); continue}
            if (Tag.ITEMS_HEAD_ARMOR.isTagged(item.type)) helmet = item
            if (Tag.ITEMS_CHEST_ARMOR.isTagged(item.type)) chestplate = item
            if (Tag.ITEMS_LEG_ARMOR.isTagged(item.type)) leggings = item
            if (Tag.ITEMS_FOOT_ARMOR.isTagged(item.type)) boots = item
        }
        helmet?.setTag("graveslot", 0)
        chestplate?.setTag("graveslot", 1)
        leggings?.setTag("graveslot", 2)
        boots?.setTag("graveslot", 3)
        offhand?.setTag("graveslot", 4)
        for (item in e.itemsToKeep) {
            e.drops.remove(item)
        }
        armorStand.setListTag("graveitems", e.drops.toMutableList())
        // can change this later to for loop and skip anything
        saveGrave(e, e.drops.toMutableList())
        e.drops.clear()
        chestDisplay.isInvulnerable = true
        chestDisplay.block = Material.CHEST.createBlockData()
        chestDisplay.displayWidth = 1F
        chestDisplay.displayHeight = 1F
        chestDisplay.setGravity(false)
        chestDisplay.setNoPhysics(true)
        armorStand.isInvulnerable = true
        armorStand.isInvisible = true
        armorStand.setGravity(false)
        armorStand.setNoPhysics(true)
        //armorStand.customName(text("${e.player.name}'s grave", arrayOf(199, 4, 30)))
        //armorStand.isCustomNameVisible = false
        armorStand.setTag("id", CustomEntity.GRAVE_MARKER.id)
        armorStand.setTag("currentlyopen", false)
        armorStand.setTag("owner", e.player.uniqueId)
        if (e.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", false)
        } else if (e.damageSource.damageType == DamageType.GENERIC && e.player.lastDamageCause!!.damageSource.causingEntity is Player) {
            armorStand.setTag("killer", (e.player.lastDamageCause!!.damageSource.causingEntity as Player).uniqueId)
            armorStand.setTag("looted", false)
        }
        e.player.setTag("gravetpcooldown", (5 * 60 * 1000 + System.currentTimeMillis()))
        textDisplay.billboard = Display.Billboard.CENTER
        textDisplay.text(text("${e.player.name}'s grave", arrayOf(199, 4, 30)))
    }
    private fun saveGrave(e: PlayerDeathEvent, drops: MutableList<ItemStack>) {
        val fileName = System.getProperty("user.dir") + "/plugins/customItems/savedGraves.txt"
        val file = File("plugins/customItems/savedGraves.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val locBytes = Base64.getEncoder().encodeToString(e.player.location.serializeAsBytes())
        val itemBytes = Base64.getEncoder().encodeToString(drops.first().serializeAsBytes())
        var totalStr = ("NEWLINE${e.player.name},${ZonedDateTime.now(ZoneId.systemDefault())}LOCATION${locBytes}")
        for (item in drops) {
            totalStr += "ITEMSTACK${Base64.getEncoder().encodeToString(item.serializeAsBytes())}"
        }
        //e.player.sendMessage(locBytes)
        //e.player.sendMessage(itemBytes)
        totalStr += "\n"
        val total = totalStr.toByteArray()
        try {
            Files.write(Paths.get(fileName), total, StandardOpenOption.APPEND)
        } catch (e: IOException) {Bukkit.getLogger().info(e.toString())}
    }

    @EventHandler fun onPlayerInteractThing(e: PlayerInteractAtEntityEvent) {
        openGrave(e)
    }
    private fun openGrave(e: PlayerInteractAtEntityEvent) {
        e.player.activeBossBars()
        if (e.rightClicked !is Interaction) return
        if (e.rightClicked.getTag<Int>("id") != CustomEntity.GRAVE_MARKER.id) return
        val armorStand: Interaction = e.rightClicked as Interaction
        if (armorStand.getTag<Boolean>("currentlyopen") == true) {e.player.sendActionBar(text("Grave is currently opened by another player", arrayOf(199, 4, 30))); return}
        val owner = armorStand.getTag<UUID>("owner")!!
        if (e.player.uniqueId != owner) {
            if (e.player.uniqueId == armorStand.getTag<UUID>("killer")) {
                if (armorStand.getTag<Boolean>("looted") == true) {
                    e.player.sendMessage(text("You have already looted this grave.", Utils.FAILED_COLOR))
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                    e.isCancelled = true
                    return
                }
                val items: MutableList<ItemStack> = armorStand.getListTag<ItemStack>("graveitems")!!.toMutableList()
                val possibleSteals = mutableListOf<ItemStack>()
                for (item in items) {
                    if (item.getCustom() != null) {
                        possibleSteals.add(item)
                    }
                    var overMax = false
                    var totalMax = 0
                    for (enchantment in item.enchantments.keys) {
                        if (item.enchantments[enchantment]!! > enchantment.maxLevel) {
                            overMax = true
                            totalMax++
                        } else if (item.enchantments[enchantment]!! == enchantment.maxLevel) {
                            totalMax++
                        }
                    }
                    if (totalMax > 2 || overMax) {
                        possibleSteals.add(item)
                    }
                }
                if (possibleSteals.isEmpty()) {
                    for (item in items) {
                        if (item.hasData(DataComponentTypes.MAX_DAMAGE)) {
                            possibleSteals.add(item)
                        }
                    }
                }
                armorStand.setTag("looted", true)
                if (possibleSteals.isEmpty()) {
                    e.player.sendMessage(text("No possible items to steal.", Utils.FAILED_COLOR))
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                    e.isCancelled = true
                    return
                }
                val steal = possibleSteals.random()
                items.remove(steal.clone())
                armorStand.setListTag("graveitems", items)
                e.player.sendMessage(text("Item stolen.", Utils.SUCCESS_COLOR))
                CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
                e.player.addItemorDrop(steal)
            }
            return
        }
        if (e.player.isSneaking) {
            val items: MutableList<ItemStack> = armorStand.getListTag<ItemStack>("graveitems")!!.toMutableList()
            val toRemove = mutableListOf<ItemStack>()
            val toAdd = mutableListOf<ItemStack>()

            for (item in items) {
                if (item.getTag<Int>("graveslot") != null) {
                    var extraItem: ItemStack? = null
                    when (item.getTag<Int>("graveslot")) {
                        0 -> {if (e.player.inventory.helmet != null) extraItem = e.player.inventory.helmet!!.clone(); e.player.inventory.helmet = item}
                        1 -> {if (e.player.inventory.chestplate != null) extraItem = e.player.inventory.chestplate!!.clone(); e.player.inventory.chestplate = item}
                        2 -> {if (e.player.inventory.leggings != null) extraItem = e.player.inventory.leggings!!.clone(); e.player.inventory.leggings = item}
                        3 -> {if (e.player.inventory.boots != null) extraItem = e.player.inventory.boots!!.clone(); e.player.inventory.boots = item}
                        4 -> {if (e.player.inventory.itemInOffHand.type != Material.AIR) extraItem = e.player.inventory.itemInOffHand.clone(); e.player.inventory.setItemInOffHand(item)}
                    }
                    if (extraItem != null) {
                        if (e.player.inventory.firstEmpty() == -1) {
                            toAdd.add(extraItem.clone())
                            item.removeTag("graveslot")
                            toRemove.add(item)
                            break
                        }
                        e.player.inventory.addItem(extraItem)
                    }
                    item.removeTag("graveslot")
                    toRemove.add(item)
                } else {
                    if (e.player.inventory.firstEmpty() == -1) break
                    toRemove.add(item.clone())
                    e.player.inventory.addItem(item.clone())
                }
            }
            for (item in toRemove) {
                items.remove(item)
            }
            for (item in toAdd) {
                items.add(item)
            }
            items.removeIf { it.type == Material.AIR }
            if (items.size == 0) {
                for (entity in armorStand.location.subtract(0.5, 0.0, 0.5).getNearbyEntities(0.1, 0.1, 0.1))  {
                    if (entity.type == EntityType.BLOCK_DISPLAY) {entity.remove(); break}
                }
                for (entity in armorStand.location.add(0.0,1.2, 0.0).getNearbyEntities(0.1, 0.1, 0.1)) {
                    if (entity.type == EntityType.TEXT_DISPLAY) {entity.remove(); break}
                }
                val uuid = armorStand.getTag<UUID>("owner")!!
                val player: Player? = if (Bukkit.getServer().getPlayer(uuid) == null) Bukkit.getServer().getOfflinePlayer(uuid).player else Bukkit.getServer().getPlayer(uuid)
                if (player != null) {
                    val graves = player.getListTag<Location>("gravelist")!!
                    for (i in graves.indices.reversed()) {
                        if (armorStand.world == graves[i].world && armorStand.location.clone().subtract(Vector(0.5, 0.0, 0.5)).subtract(graves[i]).length() < 0.5) {
                            graves.removeAt(i)
                            break
                        }
                    }
                    player.removeTag("gravelist")
                    player.setListTag("gravelist", graves)
                }
                armorStand.remove()
            }
            armorStand.setListTag("graveitems", items)
            CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.2F)
        } else {
            val graveHolder = GraveHolder(armorStand)
            e.player.openInventory(graveHolder.inventory)
            armorStand.setTag("currentlyopen", true)
            CustomEffects.playSound(armorStand.location, Sound.BLOCK_CHEST_OPEN, 1.0F, 1.2F)
        }
    }

    /*@EventHandler fun onEntityAttackEntity(e: EntityDamageByEntityEvent) {
        //axeOfPeace(e)
        //enderBladeHit(e)
        //heavyGreathammer(e)
        //crestedDagger(e)
        //sniperRifleHit(e)
        //gravityHammerHit(e)
        maceShieldedPlating(e)
        //darkSteelRapierHit(e)
        //frozenShardHit(e)
        //barbedBladeHit(e)
        //dualBarreledCrossbowHit(e)
    }
    // reducing damage via armor
    private fun maceShieldedPlating(e: EntityDamageByEntityEvent) {
        if (e.entity !is Player) return
        if (e.damager !is Player) return
        if (e.damageSource.damageType != DamageType.MACE_SMASH) return
        if ((e.entity as Player).inventory.chestplate?.isItem(CustomItem.MACE_SHIELDED_PLATING) != true) return
        e.damage *= 0.4
    }

    // direct weapon hits
    private fun darkSteelRapierHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.DARK_STEEL_RAPIER)) return
        if (!e.isCritical) return
        e.damage *= 0.66
    }
    private fun frozenShardHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.FROZEN_SHARD)) return
        if (Math.random() < 1.0 / 5.0 && e.entity is LivingEntity) {
            (e.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 100, 0))
        }
        if (!(e.damager as Player).inventory.itemInMainHand.offCooldown(e.damager as Player)) return
        if (e.entity !is Player) return
        (e.entity as Player).tempAttribute(Attribute.MOVEMENT_SPEED, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), -100.0, AttributeModifier.Operation.ADD_SCALAR), 6.0, "frozenshard")
        (e.entity as Player).tempAttribute(Attribute.JUMP_STRENGTH, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), -100.0, AttributeModifier.Operation.ADD_SCALAR), 6.0, "frozenshard")
        (e.entity as Player).tempAttribute(Attribute.KNOCKBACK_RESISTANCE, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), 100.0, AttributeModifier.Operation.ADD_NUMBER), 6.0, "frozenshard")
        (e.damager as Player).inventory.itemInMainHand.setCooldown(e.damager as Player, 60.0)
    }
    private fun barbedBladeHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.BARBED_BLADE)) return
        if (Math.random() < 1.0 / 5.0 && e.entity is LivingEntity) {
            (e.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 100, 0))
        }
        if (!(e.damager as Player).inventory.itemInMainHand.offCooldown(e.damager as Player)) return
        if (e.entity !is Player) return
        (e.entity as Player).tempAttribute(Attribute.ARMOR, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), -4.0, AttributeModifier.Operation.ADD_NUMBER), 4.0, "barbedblade", checkForDupe = true)
        (e.damager as Player).inventory.itemInMainHand.setCooldown(e.damager as Player, 15.0)
    }
    private fun gravityHammerHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (e.entity !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.GRAVITY_HAMMER)) return
        if (!(e.damager as Player).offCooldown(CustomItem.GRAVITY_HAMMER)) return
        if ((e.damager as Player).attackCooldown.toDouble() != 1.0) return
        CustomEffects.playSound(e.entity.location, Sound.ITEM_MACE_SMASH_AIR, 1.0F, 1.2F)
        (e.entity as Player).tempAttribute(Attribute.GRAVITY, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), 2.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1), 7.0, "a")
        (e.damager as Player).setCooldown(CustomItem.GRAVITY_HAMMER, 20.0)
    }
    private fun crestedDagger(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.CRESTED_DAGGER)) return
        if (e.entity !is LivingEntity) return
        // prevent infinite looping with damage
        if (e.damageSource.damageType == DamageType.STARVE) return
        e.isCancelled = true
        val damage = 20.0 / 13 * (e.damager as Player).attackCooldown
        (e.entity as LivingEntity).damage(damage, DamageSource.builder(DamageType.STARVE).withDirectEntity(e.damager).withCausingEntity(e.damager).build())
        (e.entity as LivingEntity).noDamageTicks = 5
    }
    private fun axeOfPeace(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.AXE_OF_PEACE)) return
        if (e.damage >= 15) (e.damager as Player).heal(1.5, EntityRegainHealthEvent.RegainReason.REGEN)
    }
    private fun enderBladeHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.ENDER_BLADE)) return
        if ((e.damager as Player).getTag<Int>("enderbladecrittime") == 0 || (e.damager as Player).getTag<Int>("enderbladecrittime") == null) return
        if (e.isCritical) return
        e.damage *= 1.5
        CustomEffects.playSound(e.entity.location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.8F, 1.0F)
        CustomEffects.particle(Particle.CRIT.builder(), e.entity.location, 20, 0.5, 0.5)
    }
    private fun heavyGreathammer(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return
        if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.HEAVY_GREATHAMMER)) return
        if (!e.isCritical) return
        val player = e.damager as Player
        player.inventory.itemInMainHand.setTag("criticalcount", player.inventory.itemInMainHand.getTag<Int>("criticalcount")!!+1)
        if (player.inventory.itemInMainHand.getTag<Int>("criticalcount")!! % 3 != 0) return
        e.damage *= 2
        CustomEffects.playSound(e.entity.location, Sound.BLOCK_CALCITE_BREAK, 1.5F, 0.7F)
        CustomEffects.particle(Particle.CRIMSON_SPORE.builder(), e.entity.location, 20, 0.5, 0.5)
        if (e.entity !is Player) return
        for (armor in (e.entity as Player).inventory.armorContents) {
            if (armor?.itemMeta?.isUnbreakable != true) armor?.reduceDura(10)
        }
    }

    // projectile hits
    /*private fun dualBarreledCrossbowHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.damager.getTag<Int>("id") != CustomEntity.DUAL_BARRELED_CROSSBOW_SHOT.id) return
        if (e.entity !is LivingEntity) return
        e.damage = 17.0
    }*/
    /*private fun sniperRifleHit(e: EntityDamageByEntityEvent) {
        if (e.damager !is Arrow) return
        if (e.damager.getTag<Int>("id") != CustomEntity.SNIPER_RIFLE_SHOT.id) return
        if (e.entity !is LivingEntity) return
        if (e.damageSource.damageType == CustomDamageType.ALL_BYPASS) return
        e.isCancelled = true
        val damage = 13.0
        (e.entity as LivingEntity).damage(damage,
            DamageSource.builder(CustomDamageType.ALL_BYPASS)
                .withDirectEntity(e.damageSource.directEntity ?: e.damageSource.causingEntity!!)
                .withCausingEntity(e.damageSource.causingEntity ?: e.damageSource.directEntity!!).build()
        )
        e.damager.remove()
    }*/*/

    /*@EventHandler fun onBlockBreak(e: BlockBreakEvent) {
        //veinyPickaxe(e)
        //treecapitator(e)
        //excavator(e)
        //removeDurability(e)
        //hoe(e)
    }
    /*private fun veinyPickaxe(e: BlockBreakEvent) {
        //for (item in e.block.drops) e.player.sendMessage(item.type.toString() + item.amount.toString())
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.VEINY_PICKAXE)) return
        val pickaxe = e.player.inventory.itemInMainHand
        if (!pickaxe.offCooldown(e.player)) return
        val material = e.block.type
        val drops: MutableList<ItemStack> = mutableListOf()
        var total = 1
        val checked = mutableListOf(e.block.location.clone())
        val toContinue = mutableListOf(e.block.location.clone())
        while (toContinue.isNotEmpty() && total <= 32) {
            val currentLoc = toContinue[0]
            for (loc in getAround(currentLoc)) {
                if (loc in checked) continue
                if (e.block.world.getBlockAt(loc).type == material) {
                    for (drop in e.block.world.getBlockAt(loc).getDrops(pickaxe, e.player)) drops.add(drop)
                    e.block.world.getBlockAt(loc).type = Material.AIR
                    if (total < 5) CustomEffects.playSound(loc, e.block.blockData.soundGroup.breakSound, 1.0F, e.block.blockData.soundGroup.pitch)
                    total++
                    checked.add(loc)
                    toContinue.add(loc)
                } else {
                    checked.add(loc)
                }
            }
            toContinue.removeFirst()
        }
        if (pickaxe.itemMeta.hasEnchant(CustomEnchantments.AUTOSMELT)) {
            for (drop in drops) {
                drop.smelt()
            }
        }
        for (drop in drops) {
            e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
        }
        pickaxe.setCooldown(e.player, 3.0)
    }*/
    /*private fun treecapitator(e: BlockBreakEvent) {
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.TREECAPITATOR)) return
        val axe = e.player.inventory.itemInMainHand
        val material = e.block.type
        if (!Tag.LOGS.isTagged(material) && !Tag.LEAVES.isTagged(material)) return
        val drops: MutableList<ItemStack> = mutableListOf()
        var total = 1
        val checked = mutableListOf(e.block.location.clone())
        val toContinue = mutableListOf(e.block.location.clone())
        while (toContinue.isNotEmpty() && total <= 200) {
            val currentLoc = toContinue[0]
            for (loc in getAround(currentLoc)) {
                if (loc in checked) continue
                if (e.block.world.getBlockAt(loc).type == material) {
                    for (drop in e.block.world.getBlockAt(loc).getDrops(axe, e.player)) drops.add(drop)
                    e.block.world.getBlockAt(loc).type = Material.AIR
                    if (total < 5) CustomEffects.playSound(loc, e.block.blockData.soundGroup.breakSound, 1.0F, e.block.blockData.soundGroup.pitch)
                    total++
                    checked.add(loc)
                    toContinue.add(loc)
                } else {
                    checked.add(loc)
                }
            }
            toContinue.removeFirst()
        }
        if (axe.itemMeta.hasEnchant(CustomEnchantments.AUTOSMELT)) {
            for (drop in drops) {
                drop.smelt()
            }
        }
        for (drop in drops) {
            e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
        }
    }*/
    /*private fun excavator(e: BlockBreakEvent) {
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.EXCAVATOR)) return
        val pickaxe = e.player.inventory.itemInMainHand
        val drops = mutableListOf<ItemStack>()
        for (block in getAround(e.block.location)) {
            if (e.block.world.getBlockAt(block).type.hardness.toInt() == -1) continue
            if (e.block.world.getBlockAt(block).state is Container) continue
            for (drop in block.block.getDrops(pickaxe, e.player)) drops.add(drop)
            e.block.world.getBlockAt(block).type = Material.AIR
        }
        if (CustomEnchantments.AUTOSMELT in e.player.inventory.itemInMainHand.enchantments) {
            for (drop in drops) {
                drop.smelt()
            }
        }
        for (drop in drops) {
            e.block.world.dropItem(e.block.location.clone().add(Vector(0.5, 0.5, 0.5)), drop)
        }
    }*/
    /*private fun getAround(loc: Location): MutableList<Location> {
        val locs = mutableListOf<Location>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    locs.add(loc.clone().add(Vector(x, y, z)))
                }
            }
        }
        return locs
    }*/
    private fun removeDurability(e: BlockBreakEvent) {
        val item = e.player.inventory.itemInMainHand
        if (!item.isItem(CustomItem.HOEVEL) && !item.isItem(CustomItem.AXEPICK) && !item.isItem(CustomItem.NETHERITE_MATTOCK)) return
        val newMeta = item.itemMeta as Damageable
        if (Math.random() < (1.0 - 1.0 / (1 + (item.enchantments[Enchantment.UNBREAKING] ?: 0)))) return
        newMeta.damage += 1
        if (newMeta.damage == 2031) {item.amount = 0; CustomEffects.playSound(e.player.location, Sound.ENTITY_ITEM_BREAK, 1F, 1F)}
        item.itemMeta = newMeta
    }
    /*private fun hoe(e: BlockBreakEvent) {
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.HOE)) return
        val hoe = e.player.inventory.itemInMainHand
        if (!Tag.CROPS.isTagged(e.block.type) && e.block.type !in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)) return
        e.isCancelled = true
        val blocks = getAround(e.block.location)
        blocks.add(e.block.location)
        val drops: MutableList<ItemStack> = mutableListOf()
        for (loc in blocks) {
            val block = e.block.world.getBlockAt(loc)
            if (!Tag.CROPS.isTagged(block.type) && block.type !in arrayOf(Material.BAMBOO, Material.COCOA, Material.PITCHER_CROP)) continue
            val newMeta = block.blockData as Ageable
            if (newMeta.age != newMeta.maximumAge) continue
            for (drop in block.getDrops(hoe, e.player)) drops.add(drop)
            newMeta.age = 0
            block.blockData = newMeta
        }
        for (drop in drops) e.player.addItemorDrop(drop)
    }*/*/

    @EventHandler fun onBlockDropItems(e: BlockDropItemEvent) {
        autoSmelt(e)
    }
    private fun autoSmelt(e: BlockDropItemEvent) {
        if (CustomEnchantments.AUTOSMELT !in e.player.inventory.itemInMainHand.enchantments) return
        for (drop in e.items) {
            drop.itemStack.smelt()
            if (drop.itemStack.type == Material.RAW_GOLD_BLOCK) drop.itemStack.type = Material.GOLD_BLOCK
            if (drop.itemStack.type == Material.RAW_IRON_BLOCK) drop.itemStack.type = Material.IRON_BLOCK
            if (drop.itemStack.type == Material.RAW_COPPER_BLOCK) drop.itemStack.type = Material.COPPER_BLOCK
        }
        CustomEffects.particle(Particle.FLAME.builder(), e.block.location.add(Vector(0.5, 0.5, 0.5)), 10, 0.5)
    }

    /*@EventHandler fun onCrossbowLoad(e: EntityLoadCrossbowEvent) {
        //redstoneRepeaterLoad(e)
        //multiloadShotgunLoad(e)
        //dualBarreledCrossbowLoad(e)
    }
    private fun redstoneRepeaterLoad(e: EntityLoadCrossbowEvent) {
        if (e.entity !is Player) return
        val shooter = e.entity as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.REDSTONE_REPEATER)) crossbow =
            shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.REDSTONE_REPEATER)) crossbow =
            shooter.inventory.itemInMainHand
        if (crossbow == null) return
        e.isCancelled = true
        if (crossbow.getTag<Int>("subshot") == (20 * (1.25 - 0.25 * (crossbow.enchantments[Enchantment.QUICK_CHARGE] ?: 0))).toInt()) {
            var loadedArrows = crossbow.getTag<Int>("loadedarrows")!!
            if (loadedArrows >= 8) {
                CustomEffects.playSound(e.entity.location, Sound.ITEM_CROSSBOW_LOADING_MIDDLE, 1F, 0.7F)
                loadedArrows = 10
            } else loadedArrows += 2
            crossbow.setTag("loadedarrows", loadedArrows)
            crossbow.name(
                text(
                    (if (crossbow.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - " + crossbow.getTag<Int>(
                        "loadedarrows"
                    ).toString(), arrayOf(125, 30, 30), bold = true
                )
            )
            crossbow.setTag("subshot", 0)
        } else {
            val subshot = crossbow.getTag<Int>("subshot") ?: 0
            crossbow.setTag("subshot", subshot + 1)
        }
    }
    private fun multiloadShotgunLoad(e: EntityLoadCrossbowEvent) {
        if (e.entity !is Player) return
        val shooter = e.entity as Player
        var crossbow: ItemStack? = null
        var offhand = false
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) {crossbow = shooter.inventory.itemInOffHand; offhand = true}
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        e.isCancelled = true
        if (crossbow.getTag<Int>("subshot") == (20 * (1.25 - 0.25 * (crossbow.enchantments[Enchantment.QUICK_CHARGE] ?: 0))).toInt()) {
            var loadedArrows = crossbow.getTag<Int>("loadedshot")!!
            if (loadedArrows >= 24) {
                CustomEffects.playSound(e.entity.location, Sound.ITEM_CROSSBOW_LOADING_MIDDLE, 1F, 0.7F)
                loadedArrows = 25
            } else loadedArrows += 1
            crossbow.setTag("loadedshot", loadedArrows)
            crossbow.name(
                text(
                    "Multi-load Shotgun - " + crossbow.getTag<Int>("loadedshot").toString(),
                    arrayOf(214, 125, 0), bold = true
                )
            )
            crossbow.setTag("subshot", 0)
        } else {
            val subshot = crossbow.getTag<Int>("subshot") ?: 0
            crossbow.setTag("subshot", subshot + 1)
        }
        //shooter.sendMessage(Bukkit.getServer().currentTick.toString())
        /*
        e.entity.setTag("tempcrossbow", crossbow.clone())


        val slot = (e.entity as Player).inventory.heldItemSlot
        if (offhand) {
            (e.entity as Player).inventory.setItemInOffHand(null)
        } else {
            (e.entity as Player).inventory.setItem(slot, null)
        }

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            if (offhand && (e.entity as Player).inventory.itemInOffHand.type == Material.AIR) {
                (e.entity as Player).inventory.setItemInOffHand(e.entity.getTag<ItemStack>("tempcrossbow"))
            } else if ((e.entity as Player).inventory.getItem(slot) == null) {
                (e.entity as Player).inventory.setItem(slot, e.entity.getTag<ItemStack>("tempcrossbow"))
            } else {
                (e.entity as Player).addItemorDrop(e.entity.getTag<ItemStack>("tempcrossbow")!!)
            }
        }, 1L)*/
    }
    private fun dualBarreledCrossbowLoad(e: EntityLoadCrossbowEvent) {
        if (e.entity !is Player) return
        val shooter = e.entity as Player
        e.isCancelled = true
        e.crossbow.crossbowProj(ItemStack(Material.ARROW), 2)
    }*/

    /*@EventHandler fun onSwapHands(e: PlayerSwapHandItemsEvent) {
        cycleRedstoneItem(e)
        //cycleRedstoneRepeater(e)
        //cycleMultiloadShotgun(e)
    }
    private fun cycleRedstoneItem(e: PlayerSwapHandItemsEvent) {
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.REDSTONE_AMALGAMATION)) return
        if (!e.player.isSneaking) return
        e.isCancelled = true
        var outerLayer = e.player.inventory.itemInMainHand.getTag<Int>("redstonegroup")!!
        outerLayer = if (outerLayer == 3) 0 else outerLayer+1
        val newInnerLayer = e.player.inventory.itemInMainHand.getTag<IntArray>("storedinner")!![outerLayer]
        e.player.inventory.itemInMainHand.setTag("redstonegroup", outerLayer)
        e.player.inventory.itemInMainHand.setTag("redstoneitem", newInnerLayer)
        e.player.inventory.itemInMainHand.type = getRedstoneItems(outerLayer)[newInnerLayer]
        CustomEffects.playSound(e.player.location, Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0F, 1.1F)
    }
    /*private fun cycleRedstoneRepeater(e: PlayerSwapHandItemsEvent) {
        var crossbow: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.REDSTONE_REPEATER)) {crossbow = e.player.inventory.itemInOffHand}
        if (e.player.inventory.itemInMainHand.isItem(CustomItem.REDSTONE_REPEATER)) {crossbow = e.player.inventory.itemInMainHand}
        if (crossbow == null) return
        if (!e.player.isSneaking) return
        e.isCancelled = true
        crossbow.setTag("loading", !crossbow.getTag<Boolean>("loading")!!)
        if (!crossbow.getTag<Boolean>("loading")!!) {
            crossbow.crossbowProj(ItemStack(Material.ARROW), crossbow.getTag<Int>("arrowcount")!!)
        } else {
            crossbow.clearCrossbowProj()
        }
        CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 1.0F, 1.2F)
    }*/
    /*private fun cycleMultiloadShotgun(e: PlayerSwapHandItemsEvent) {
        var crossbow: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) {crossbow = e.player.inventory.itemInOffHand}
        if (e.player.inventory.itemInMainHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) {crossbow = e.player.inventory.itemInMainHand}
        if (crossbow == null) return
        if (!e.player.isSneaking) return
        e.isCancelled = true
        if (crossbow.getTag<Int>("loadedshot")!! > 0) crossbow.setTag("loading", !crossbow.getTag<Boolean>("loading")!!)
        else {crossbow.setTag("loading", true); CustomEffects.playSound(e.player.location, Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.2F); return}
        if (!crossbow.getTag<Boolean>("loading")!!) {
            crossbow.crossbowProj(ItemStack(Material.ARROW), crossbow.getTag<Int>("loadedshot")!!)
        } else {
            crossbow.clearCrossbowProj()
        }
        CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 1.0F, 1.2F)
    }*/*/

    /*@EventHandler fun onBoatEnter(e: EntityMountEvent) {
        badBoy(e)
        cowboyHat(e)
    }
    private fun badBoy(e: EntityMountEvent) {
        if (e.entity !is Player) return
        val tag = Bukkit.getServer().getTag("entity_types", NamespacedKey.minecraft("boat"), EntityType::class.java)
        if (tag?.isTagged(e.mount.type) == false) return
        if ((e.entity as Player).name.lowercase() == "SparkFire_".lowercase()) e.isCancelled = true
    }
    private fun cowboyHat(e: EntityMountEvent) {
        if (e.entity !is Player) return
        if (e.mount !is Horse) return
        val player = e.entity as Player
        if (!((player.inventory.helmet?: ItemStack(Material.AIR)).isItem(CustomItem.COWBOY_HAT))) return
        (e.mount as Horse).addPotionEffects(mutableListOf(
            PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
            PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
            PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
        ))
    }*/

    /*@EventHandler fun onDismount(e: EntityDismountEvent) {
        cowboyHatRemove(e)
    }
    private fun cowboyHatRemove(e: EntityDismountEvent) {
        if (e.entity !is Player) return
        if (e.dismounted !is Horse) return
        if ((e.dismounted as Horse).hasPotionEffect(PotionEffectType.RESISTANCE))
        (e.dismounted as Horse).clearActivePotionEffects()
    }*/

    @EventHandler fun onArrowLand(e: ProjectileHitEvent) {
        //windHookLand(e)
        //redstoneRepeaterLand(e)
        //multiloadShotgunLand(e)
        //llamaSpitLand(e)
        //surfaceToAirMissileHit(e)
        assassinsCloakDodge(e)
    }
    /*private fun windHookLand(e: ProjectileHitEvent) {
        if (e.entity.getTag<Int>("id") != CustomEntity.WIND_HOOK_SHOT.id) return
        val arrow = e.entity as Arrow
        arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
        val shooter = e.entity.shooter as Player
        shooter.setTag("windhookpullcoords", "${e.entity.location.x},${e.entity.location.y},${e.entity.location.z}")
        shooter.setTag("windhookpulltime", 50)
    }*/
    /*private fun redstoneRepeaterLand(e: ProjectileHitEvent) {
        if (e.entity.getTag<Int>("id") != CustomEntity.REDSTONE_REPEATER_SHOT.id) return
        val arrow = e.entity as Arrow
        val shooter = e.entity.shooter as Player
        if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
        e.isCancelled = true
        val hit = e.hitEntity as LivingEntity
        hit.damage(10.5, DamageSource.builder(DamageType.ARROW).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
        hit.noDamageTicks = 0
        e.entity.remove()
    }*/
    /*private fun multiloadShotgunLand(e: ProjectileHitEvent) {
        if (e.entity.getTag<Int>("id") != CustomEntity.MULTI_LOAD_CROSSBOW_SHOT.id) return
        if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
        e.isCancelled = true
        val hit = e.hitEntity as LivingEntity
        hit.damage(10.0, DamageSource.builder(DamageType.ARROW).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
        hit.noDamageTicks = 0
        e.entity.remove()
    }*/
    /*private fun llamaSpitLand(e: ProjectileHitEvent) {
        if (e.entity.getTag<Int>("id") != CustomEntity.TRUE_LLAMA_SPIT.id) return
        if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
        e.isCancelled = true
        val hit = e.hitEntity as LivingEntity
        hit.damage(1.5, DamageSource.builder(DamageType.STARVE).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
        hit.noDamageTicks = 0
        e.entity.remove()
    }*/
    /*private fun surfaceToAirMissileHit(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_ARROW.id) return
        if (e.hitEntity !is Player) return
        e.entity.remove()
        e.hitEntity!!.setTag("elytradisabled", 25)
        (e.hitEntity!! as Player).playSound(e.hitEntity!!, Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F)
        (e.hitEntity!! as Player).setCooldown(Material.ELYTRA, 500)
        (e.hitEntity as Player).isGliding = false
        //(e.hitEntity as Player).setCool
    }*/
    private fun assassinsCloakDodge(e: ProjectileHitEvent) {
        if (e.entity !is Arrow) return
        if (e.hitEntity == null || e.hitEntity!! !is Player) return
        val hit = e.hitEntity as Player
        var totalPieces = 0
        if (hit.inventory.helmet?.isItem(CustomItem.ASSASSINS_HOOD) == true) totalPieces += 1
        if (hit.inventory.chestplate?.isItem(CustomItem.ASSASSINS_ROBE) == true) totalPieces += 1
        if (hit.inventory.leggings?.isItem(CustomItem.ASSASSINS_LEGGINGS) == true) totalPieces += 1
        if (hit.inventory.boots?.isItem(CustomItem.ASSASSINS_LOAFERS) == true) totalPieces += 1
        if (Math.random() >= 0.125 * totalPieces) return
        e.isCancelled = true
        e.entity.remove()
        CustomEffects.playSound(hit.location, Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.2F)
    }

    /*@EventHandler fun onProjectileLaunch(e: ProjectileLaunchEvent) {
        customArrows(e)
        //windHook(e)
        //redstoneRepeater(e)
        //multiloadShotgun(e)
        //surfaceToAirMissileLauncher(e)
        //homingWindChargeCannon(e)
        //sniperRifle(e)
        //ridableCrossbow(e)
        //landmineLauncher(e)
        //dualBarreledCrossbowShoot(e)
        //sonicCrossbowShoot(e)
    }
    private fun customArrows(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        if (e.entity !is Arrow) return
        val arrow = e.entity as Arrow
        if (arrow.color?.asRGB() == Color.fromRGB(CustomItem.DRIPSTONE_ARROW.id).asRGB()) {
            e.isCancelled = true
            val dripstone = e.entity.world.spawn(e.entity.location, FallingBlock::class.java)
            dripstone.blockData = Material.DRIPSTONE_BLOCK.createBlockData()
            dripstone.maxDamage = 40
            dripstone.damagePerBlock = 10F
            dripstone.setHurtEntities(true)
            dripstone.fallDistance = 100F
            dripstone.velocity = e.entity.velocity
            e.entity.remove()
        } else if (arrow.color?.asRGB() == Color.fromRGB(CustomItem.LLAMA_SPIT_ARROW.id).asRGB()) {
            val spit = e.entity.world.spawn(e.entity.location, LlamaSpit::class.java)
            spit.velocity = e.entity.velocity
            spit.shooter = shooter
            spit.setTag("id", CustomEntity.TRUE_LLAMA_SPIT.id)
            e.entity.remove()
        } else if (arrow.color?.asRGB() == Color.fromRGB(CustomItem.SHULKER_BULLET_ARROW.id).asRGB()) {
            val bullet = e.entity.world.spawn(e.entity.location, ShulkerBullet::class.java)
            bullet.velocity = e.entity.velocity
            bullet.shooter = shooter
            e.entity.remove()
        } else if (arrow.color?.asRGB() == Color.fromRGB(CustomItem.WITHER_SKULL_ARROW.id).asRGB()) {
            val skull = e.entity.world.spawn(e.entity.location, WitherSkull::class.java)
            skull.velocity = e.entity.velocity
            skull.shooter = shooter
            e.entity.remove()
        } else if (arrow.color?.asRGB() == Color.fromRGB(CustomItem.ENDER_PEARL_ARROW.id).asRGB()) {
            val pearl = e.entity.world.spawn(e.entity.location, EnderPearl::class.java)
            pearl.velocity = e.entity.velocity
            pearl.shooter = shooter
            e.entity.remove()
        }
    }
    /*private fun windHook(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var mainHand: Boolean? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.WIND_HOOK)) mainHand = false
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.WIND_HOOK)) mainHand = true
        if (mainHand == null) return
        if (!shooter.offCooldown(CustomItem.WIND_HOOK)) {e.isCancelled = true; return}
        e.entity.setTag("id", CustomEntity.WIND_HOOK_SHOT.id)
        (e.entity as Arrow).color = Color.fromRGB(211, 195, 219)
        shooter.setCooldown(CustomItem.WIND_HOOK, 15.0)
        shooter.stopSound(Sound.ENTITY_ARROW_SHOOT)
        CustomEffects.playSound(shooter.location, Sound.ENTITY_BREEZE_JUMP, 1F, 0.8F)
    }
    private fun redstoneRepeater(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var mainHand: Boolean? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.REDSTONE_REPEATER)) mainHand = false
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.REDSTONE_REPEATER)) mainHand = true
        if (mainHand == null) return
        val crossbow = if (mainHand) shooter.inventory.itemInMainHand else shooter.inventory.itemInOffHand
        if (crossbow.getTag<Boolean>("loading")!!) {e.isCancelled = true; return}
        if (crossbow.getTag<Int>("loadedarrows")!! < crossbow.getTag<Int>("arrowcount")!!) {e.isCancelled = true; return}
        crossbow.crossbowProj(ItemStack(Material.ARROW))
        crossbow.setTag("loadedarrows", crossbow.getTag<Int>("loadedarrows")!!-1)
        if (crossbow.getTag<Int>("loadedarrows")!! < crossbow.getTag<Int>("arrowcount")!!) {crossbow.setTag("loading", true); crossbow.clearCrossbowProj()}
        crossbow.name(text((if (crossbow.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - "+crossbow.getTag<Int>("loadedarrows").toString(), arrayOf(125, 30, 30), bold = true))
        e.entity.setTag("id", CustomEntity.REDSTONE_REPEATER_SHOT.id)
    }
    private fun multiloadShotgun(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.MULTI_LOAD_CROSSBOW)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        crossbow.setTag("loadedshot", 0)
        crossbow.name(text("Multi-load Shotgun - "+crossbow.getTag<Int>("loadedshot").toString(), arrayOf(214, 125, 0), bold = true))
        crossbow.setTag("loading", true)
        e.entity.setTag("id", CustomEntity.MULTI_LOAD_CROSSBOW_SHOT.id)
    }
    private fun surfaceToAirMissileLauncher(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.SURFACE_TO_AIR_MISSILE)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.SURFACE_TO_AIR_MISSILE)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.SURFACE_TO_AIR_MISSILE)) {e.isCancelled = true; return}
        var flyer: Player? = null
        for (player in shooter.location.getNearbyPlayers(120.0)) {
            if (player == e.entity.shooter) continue
            if (player.isGliding) flyer = player
        }
        if (flyer == null) {e.isCancelled = true; return}
        e.entity.setTag("target", flyer.uniqueId)
        shooter.setCooldown(CustomItem.SURFACE_TO_AIR_MISSILE, 20.0)
        e.entity.setTag("id", CustomEntity.ELYTRA_BREAKER_ARROW.id)
    }
    private fun homingWindChargeCannon(e: ProjectileLaunchEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.WIND_CHARGE_CANNON)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.WIND_CHARGE_CANNON)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.WIND_CHARGE_CANNON)) {e.isCancelled = true; return}
        val mode = crossbow.getTag<Int>("mode")

        val windCharges = mutableListOf<WindCharge>()
        for (i in 0..1) {
            val windCharge = e.entity.world.spawn(e.entity.location, WindCharge::class.java) {
                it.velocity = e.entity.velocity.multiply(0.7)
                it.shooter = shooter
            }
            windCharges.add(windCharge)
        }

        if (mode == 0) {
            shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 7.0)
            for (windCharge in windCharges) {
                var closest: Entity? = null
                var closestAngle: Double = Math.PI / 3
                for (entity in shooter.getNearbyEntities(60.0, 60.0, 60.0)) {
                    if (entity !is LivingEntity) continue
                    val angle = entity.location.subtract(shooter.location).toVector().angle(shooter.location.direction)
                    if (abs(angle) < abs(closestAngle)) {
                        closest = entity
                        closestAngle = angle.toDouble()
                    }
                }
                val target = closest?.uniqueId ?: shooter.uniqueId
                windCharge.setTag("target", target)
                windCharge.setTag("id", CustomEntity.WIND_CANNON_CHARGE.id)
            }
        } else {
            shooter.setCooldown(CustomItem.WIND_CHARGE_CANNON, 5.0)
        }

        e.entity.remove()
    }
    private fun sniperRifle(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.SNIPER_RIFLE)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.SNIPER_RIFLE)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.SNIPER_RIFLE)) {e.isCancelled = true; return}

        e.entity.velocity = shooter.location.direction.normalize().multiply(50)
        shooter.setCooldown(CustomItem.SNIPER_RIFLE, 40.0)
        e.entity.setTag("id", CustomEntity.SNIPER_RIFLE_SHOT.id)
    }
    private fun ridableCrossbow(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.RIDABLE_CROSSBOW)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.RIDABLE_CROSSBOW)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        if (!(e.entity.shooter!! as Player).offCooldown(CustomItem.RIDABLE_CROSSBOW)) {e.isCancelled = true; return}

        e.entity.velocity = e.entity.velocity.multiply(1.2)
        shooter.setCooldown(CustomItem.RIDABLE_CROSSBOW, 60.0)
        e.entity.addPassenger(shooter)
    }
    private fun landmineLauncher(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var mainHand: Boolean? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.LANDMINE_LAUNCHER)) mainHand = false
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.LANDMINE_LAUNCHER)) mainHand = true
        if (mainHand == null) return
        if (!shooter.offCooldown(CustomItem.LANDMINE_LAUNCHER)) {e.isCancelled = true; return}
        e.entity.setTag("id", CustomEntity.LANDMINE_SHOT.id)
        (e.entity as Arrow).color = Color.fromRGB(61, 57, 56)
        shooter.setCooldown(CustomItem.LANDMINE_LAUNCHER, 10.0)
        (e.entity as Arrow).pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
    }
    private fun dualBarreledCrossbowShoot(e: ProjectileLaunchEvent) {
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.DUAL_BARRELED_CROSSBOW)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.DUAL_BARRELED_CROSSBOW)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return
        e.entity.setTag("id", CustomEntity.DUAL_BARRELED_CROSSBOW_SHOT.id)
        val arrow = e.entity as Arrow
        arrow.pierceLevel = 6
    }
    private fun sonicCrossbowShoot(e: ProjectileLaunchEvent) {
        if (e.entity !is Arrow) return
        if (e.entity.shooter !is Player) return
        val shooter = e.entity.shooter as Player
        var crossbow: ItemStack? = null
        if (shooter.inventory.itemInOffHand.itemMeta != null && shooter.inventory.itemInOffHand.isItem(CustomItem.SONIC_CROSSBOW)) crossbow = shooter.inventory.itemInOffHand
        if (shooter.inventory.itemInMainHand.itemMeta != null && shooter.inventory.itemInMainHand.isItem(CustomItem.SONIC_CROSSBOW)) crossbow = shooter.inventory.itemInMainHand
        if (crossbow == null) return

        val direction = shooter.location.direction.normalize().multiply(0.5)
        val current = shooter.location
        val toDamage = mutableListOf<LivingEntity>()
        for (i in 0..30) {
            current.add(direction)
            for (entity in current.getNearbyEntitiesByType(LivingEntity::class.java, 4.0, 4.0, 4.0)) {
                var within = entity.boundingBox.getCorners(entity.world).any { it.subtract(current).length() < 1.0 }
                if (within) {
                    toDamage.add(entity)
                }
            }
        }

        for (entity in toDamage) {
            if (entity is Player) {
                entity.applyDamage(DamageSettings(
                    8.0, CustomDamageType.ALL_BYPASS, shooter)
                )
            } else {
                entity.applyDamage(DamageSettings(
                    35.0, DamageType.PLAYER_ATTACK, shooter)
                )
            }
        }

        CustomEffects.particleLine(ParticleBuilder(Particle.SONIC_BOOM), shooter.location, current, 15)
        CustomEffects.playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F)

        shooter.setCooldown(CustomItem.SONIC_CROSSBOW, 20.0)
        e.entity.remove()
    }*/*/

    /*@EventHandler fun onEntityInteract(e: PlayerInteractEntityEvent) {
        //jerryIdol(e)
        //villagerAtomizer(e)
        //goldenZombie(e)
        //fletcherUpgrade(e)
        //tradingScrambler(e)
        //reinforcedCagePick(e)
    }
    /*private fun jerryIdol(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is Villager) return
        if (e.rightClicked.getTag<Int>("id") != CustomEntity.JERRY_IDOL.id) return
        e.isCancelled = true
        if (e.player.inventory.itemInMainHand.type == Material.AIR) {
            val emBlockStacks = e.rightClicked.getTag<Int>("emeraldstacks")
            val newJerryIdol = Items.get(CustomItem.JERRY_IDOL)
            newJerryIdol.setTag("emeraldstacks", emBlockStacks)
            e.player.addItemorDrop(newJerryIdol)
            CustomEffects.playSound(e.rightClicked.location, Sound.ENTITY_ITEM_PICKUP, 20F, 0.5F)
            e.rightClicked.remove()
        } else if (e.player.inventory.itemInMainHand.type == Material.EMERALD_BLOCK && e.player.inventory.itemInMainHand.amount == 64) {
            val emBlockStacks = e.rightClicked.getTag<Int>("emeraldstacks") ?: 0
            e.rightClicked.setTag("emeraldstacks", emBlockStacks + 1)
            (e.rightClicked as Villager).getAttribute(Attribute.SCALE)!!.baseValue += 0.1
            e.player.inventory.itemInMainHand.amount -= 64
            for (i in 0..5+emBlockStacks/2) CustomEffects.particleCircle(Particle.HAPPY_VILLAGER.builder(), e.rightClicked.location.clone().add(Vector(0.0, i.toDouble()/2.5, 0.0)), 0.5 * (1 + emBlockStacks*0.1), (20 * (1 + emBlockStacks*0.1)).toInt(), 0.01)
            CustomEffects.playSound(e.rightClicked.location, Sound.ENTITY_VILLAGER_YES, 20F, 1.5F)
        }
    }*/
    /*private fun villagerAtomizer(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is Villager) return
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.VILLAGER_ATOMIZER)) return
        e.isCancelled = true
        val newItem = Items.get(CustomItem.VILLAGER)
        val villager: Villager = e.rightClicked as Villager

        val snapshot = villager.createSnapshot()!!.asString
        newItem.setTag("storedvillager", snapshot)

        newItem.lore(
            text("Profession: ${villager.profession}", arrayOf(255, 255, 255)),
            text("Level: ${convertVillagerLevel(villager.villagerLevel)}", arrayOf(255, 255, 255)),
        )
        villager.remove()
        e.player.addItemorDrop(newItem)
    }*/
    /*private fun goldenZombie(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is Villager && e.rightClicked !is ZombieVillager) return
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.GOLDEN_ZOMBIE)) return
        if (e.rightClicked.getTag<Int>("id") == CustomEntity.JERRY_IDOL.id) return
        e.isCancelled = true
        if (e.rightClicked is Villager) {
            val villager: Villager = e.rightClicked as Villager
            villager.zombify()
        } else if (e.rightClicked is ZombieVillager) {
            val zombieVillager: ZombieVillager = e.rightClicked as ZombieVillager
            zombieVillager.conversionTime = 50
            zombieVillager.conversionPlayer = e.player
        }
    }*/
    /*private fun fletcherUpgrade(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is Villager) return
        val villager: Villager = e.rightClicked as Villager
        if (villager.profession != Villager.Profession.FLETCHER || villager.villagerLevel != 5 || villager.getTag<Int>("id") == CustomEntity.MAX_FLETCHER.id) return
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.FLETCHER_UPGRADE)) return
        e.isCancelled = true
        val arrowTypes = arrayOf(CustomItem.DRIPSTONE_ARROW, CustomItem.ENDER_PEARL_ARROW, CustomItem.WITHER_SKULL_ARROW, CustomItem.LLAMA_SPIT_ARROW, CustomItem.SHULKER_BULLET_ARROW)
        val newRecipes = Lists.newArrayList(villager.recipes)
        val newRecipe = MerchantRecipe(Items.get(arrowTypes.random()), 0, 10000, true, 0, 0F)
        newRecipe.addIngredient(ItemStack(Material.EMERALD_BLOCK, 4))
        newRecipe.addIngredient(ItemStack(Material.DIAMOND_BLOCK))
        newRecipes.add(newRecipe)
        villager.recipes = newRecipes
        villager.setTag("id", CustomEntity.MAX_FLETCHER.id)
        e.player.inventory.itemInMainHand.amount -= 1
        CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_TRADE, 5F, 1.4F)
        CustomEffects.particleCloud(Particle.HAPPY_VILLAGER.builder(), villager.location, 100, 1.0, 0.5)
    }*/
    /*private fun tradingScrambler(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is Villager) return
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.TRADING_SCRAMBLER)) return
        if (e.rightClicked.getTag<Int>("id") != null) return
        val villager = e.rightClicked as Villager
        val maxLevel = villager.villagerLevel
        val profession = villager.profession
        val experience = villager.villagerExperience
        //villager.profession = Villager.Profession.NONE
        villager.villagerExperience = 0
        villager.villagerLevel = 1
        villager.recipes = mutableListOf()
        //villager.profession = profession
        for (i in 2..maxLevel) {
            villager.addTrades(2)
            villager.villagerLevel = i
        }
        villager.addTrades(2)
        villager.villagerExperience = experience
        CustomEffects.playSound(e.player.location, Sound.BLOCK_BAMBOO_BREAK, 1.0F, 1.0F)
    }*/
    /*private fun reinforcedCagePick(e: PlayerInteractEntityEvent) {
        if (e.rightClicked.type in arrayOf(
                EntityType.WARDEN, EntityType.ENDER_DRAGON, EntityType.ARROW, EntityType.TEXT_DISPLAY, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY,
                EntityType.AREA_EFFECT_CLOUD, EntityType.BREEZE_WIND_CHARGE, EntityType.DRAGON_FIREBALL, EntityType.END_CRYSTAL, EntityType.ENDER_PEARL,
                EntityType.EVOKER_FANGS, EntityType.EXPERIENCE_ORB, EntityType.EXPERIENCE_BOTTLE, EntityType.EYE_OF_ENDER, EntityType.FALLING_BLOCK,
                EntityType.FIREBALL, EntityType.FIREWORK_ROCKET, EntityType.FISHING_BOBBER, EntityType.GLOW_ITEM_FRAME, EntityType.ITEM_FRAME,
                EntityType.INTERACTION, EntityType.ITEM, EntityType.LEASH_KNOT, EntityType.LINGERING_POTION, EntityType.MARKER, EntityType.PAINTING,
                EntityType.PLAYER, EntityType.SHULKER_BULLET, EntityType.SMALL_FIREBALL, EntityType.SPECTRAL_ARROW, EntityType.SPLASH_POTION,
                EntityType.TRIDENT, EntityType.WIND_CHARGE, EntityType.WITHER, EntityType.WITHER_SKULL
        )) return
        if (e.rightClicked.getTag<Int>("id") != null) return
        if (e.rightClicked.getTag<Int>("bossid") != null) return
        if (!e.player.offCooldown(CustomItem.REINFORCED_CAGE)) return
        if (!e.player.inventory.itemInMainHand.isItem(CustomItem.REINFORCED_CAGE)) return
        if (e.player.inventory.itemInMainHand.getTag<String>("storedmob") !in arrayOf(null, "")) return
        e.isCancelled = true
        val entity = e.rightClicked

        val snapshot = entity.createSnapshot()!!.asString
        e.player.inventory.itemInMainHand.setTag("storedmob", snapshot)

        e.player.inventory.itemInMainHand.loreBlock(
            text("Type: ${entity.type}", Utils.GRAY),
            text(""),
            text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
        )
        if (entity is InventoryHolder) entity.inventory.clear()
        entity.remove()
        e.player.setCooldown(CustomItem.REINFORCED_CAGE, 0.5)
    }*/*/

    @EventHandler fun onInteract(e: PlayerInteractEvent) {
        //jerryIdolPlace(e)
        //villagerPlace(e)
        //fangedStaffTick(e)
        cancelProjectileCharge(e)
        //arrowCountRedstoneRepeater(e)
        //polarizedMagnet(e)
        //lastPrism(e)
        //pewMaticHorn(e)
        //experienceFlask(e)
        //netheriteMultitool(e)
        //pocketknifeMultitool(e)
        //hoeHoe(e)
        ancientTome(e)
        //enderBlade(e)
        //autoSmeltUpgrade(e)
        //soulCrystal(e)
        //netheriteCoating(e)
        //witherCoating(e)
        //reinforcingStruts(e)
        //tripleSwipeBlade(e)
        //windChargeCannonMode(e)
        //landmineLauncherTrigger(e)
        //jetpackControllerPack(e)
        //jetpackController(e)
        //reinforcedCagePlace(e)
        //darkSteelRapierActivate(e)
    }
    private fun cancelProjectileCharge(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        if (e.item == null) return
        if (e.item!!.type != Material.BOW && e.item!!.type != Material.CROSSBOW) return
        for (custom in arrayOf(CustomItem.WIND_HOOK)) {
            if (e.item!!.isItem(custom) && !e.item!!.offCooldown(e.player)) e.isCancelled = true
        }
    }
    private fun ancientTome(e: PlayerInteractEvent) {
        var tome: ItemStack? = null
        for (custom in arrayOf<CustomItem>()) if (e.player.inventory.itemInOffHand.isItem(custom)) tome = e.player.inventory.itemInOffHand
        if (tome == null) return
        val tomeEnchant = tome.enchantments.entries.random()
        val enchantable = e.player.inventory.itemInMainHand
        if (enchantable.enchantments[tomeEnchant.key] != tomeEnchant.value-1) return
        e.isCancelled = true
        enchantable.removeEnchantment(tomeEnchant.key)
        enchantable.addUnsafeEnchantment(tomeEnchant.key, tomeEnchant.value)
        tome.amount -= 1
        CustomEffects.playSound(e.player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.1F)
    }
    /*/*private fun jerryIdolPlace(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.JERRY_IDOL)) return
        val loc = e.clickedBlock!!.location
        loc.add(Vector(0.5, 1.0, 0.5))
        val villager: Villager = e.player.world.spawnEntity(loc, EntityType.VILLAGER) as Villager
        villager.setTag("id", CustomEntity.JERRY_IDOL.id)
        val emeraldStacks = e.item!!.getTag<Int>("emeraldstacks") ?: 0
        villager.setTag("emeraldstacks", emeraldStacks)
        villager.getAttribute(Attribute.SCALE)!!.baseValue = emeraldStacks*0.1 + 1
        villager.isInvulnerable = true
        villager.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false))
        villager.setAI(false)
        e.item!!.amount -= 1
        CustomEffects.playSound(villager.location, Sound.ENTITY_VILLAGER_TRADE, 20F, 0.9F)
    }*/
    /*private fun villagerPlace(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.VILLAGER)) return
        val loc = e.clickedBlock!!.location
        loc.add(Vector(0.5, 1.0, 0.5))
        val villagerAsString = e.item!!.getTag<String>("storedvillager")
        if (villagerAsString == null) loc.world.spawn(loc, Villager::class.java)
        else Bukkit.getEntityFactory().createEntitySnapshot(villagerAsString).createEntity(loc)
        e.item!!.amount -= 1
        CustomEffects.playSound(loc, Sound.ENTITY_VILLAGER_TRADE, 20F, 1.2F)
    }*/
    /*private fun fangedStaffTick(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.FANGED_STAFF)) return
        if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) && e.item!!.offCooldown(e.player, "Vexing")) {
            e.player.setTag("evokerstaffused", true)
        } else if ((e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) && e.item!!.offCooldown(e.player, "Fangs")) {
            val facing = e.player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = e.player.location.clone().add(Vector(0.0, 1.6, 0.0))
            for (i in 0..800) {
                if (!startingLocation.add(facing).block.isPassable) {
                    break
                }
            }
            for (x in -1..1) {
                for (z in -1..1) {
                    val fangs = startingLocation.world.spawn(startingLocation.clone().add(x.toDouble(), 0.0, z.toDouble()), EvokerFangs::class.java)
                    fangs.owner = e.player
                }
            }
            e.item!!.setCooldown(e.player, 0.5, "Fangs")
            CustomEffects.playSound(e.player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1F, 0.7F)
        }
    }*/
    /*private fun arrowCountRedstoneRepeater(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.REDSTONE_REPEATER)) return
        if (e.action != Action.LEFT_CLICK_BLOCK && e.action != Action.LEFT_CLICK_AIR) return
        if (!e.player.offCooldown(CustomItem.REDSTONE_REPEATER)) return
        e.item!!.setTag("arrowcount", if (e.item!!.getTag<Int>("arrowcount") == 1) 2 else 1)
        e.item!!.name(text((if (e.item!!.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - "+e.item!!.getTag<Int>("loadedarrows").toString(), arrayOf(125, 30, 30), bold = true))
        if (!e.item!!.getTag<Boolean>("loading")!!) {
            val newMeta = e.item!!.itemMeta as CrossbowMeta
            newMeta.setChargedProjectiles(null)
            e.item!!.itemMeta = newMeta
            e.item!!.crossbowProj(ItemStack(Material.ARROW), e.item!!.getTag<Int>("arrowcount")!!)
        }
        CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_LOADING_START, 1.0F, 1.5F)
        e.player.setCooldown(CustomItem.REDSTONE_REPEATER, 0.5)
    }*/
    /*private fun polarizedMagnet(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            if (!e.player.isSneaking) return
            if (!e.item!!.isItem(CustomItem.POLARIZED_MAGNET)) return
            if (!e.player.offCooldown(CustomItem.POLARIZED_MAGNET)) return
            e.player.setTag("polarizedmagnetitempull", !(e.player.getTag<Boolean>("polarizedmagnetitempull")?:false))
            if (e.player.getTag<Boolean>("polarizedmagnetitempull")!!) e.item!!.name(text("Polarized Magnet", arrayOf(36, 36, 255), bold = true))
            else e.item!!.name(text("Polarized Magnet", arrayOf(255, 36, 36), bold = true))
            CustomEffects.playSound(e.player.location, Sound.BLOCK_CANDLE_PLACE, 1F, 0.9F)
            e.player.setCooldown(CustomItem.POLARIZED_MAGNET, 0.5)
        } else {
            var magnet: ItemStack? = null
            if (e.player.inventory.itemInOffHand.isItem(CustomItem.POLARIZED_MAGNET)) magnet = e.player.inventory.itemInOffHand
            if (e.player.inventory.itemInMainHand.isItem(CustomItem.POLARIZED_MAGNET)) magnet = e.player.inventory.itemInMainHand
            if (magnet == null) return
            e.player.setTag("polarizedmagnetpulling", 4)
            CustomEffects.playSound(e.player.location, Sound.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 1.0F, 1.1F)
        }
    }*/
    /*private fun lastPrism(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            var prism: ItemStack? = null
            if (e.player.inventory.itemInOffHand.isItem(CustomItem.LAST_PRISM)) prism = e.player.inventory.itemInOffHand
            if (e.player.inventory.itemInMainHand.isItem(CustomItem.LAST_PRISM)) prism = e.player.inventory.itemInMainHand
            if (prism == null) return
            if (!prism.offCooldown(e.player, "Zap")) return
            val facing = e.player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = e.player.location.clone().add(Vector(0.0, 1.6, 0.0))
            for (i in 0..800) {
                CustomEffects.particle(Particle.END_ROD.builder(), startingLocation, 1)
                if (i%5 == 0) {
                    for (entity in startingLocation.getNearbyEntities(3.0, 3.0, 3.0)) {
                        if (entity is LivingEntity && entity != e.player) {
                            if (!entity.boundingBox.overlaps(
                                    startingLocation.clone().add(Vector(0.75, 0.75, 0.75)).toVector(),
                                    startingLocation.clone().add(Vector(-0.75, -0.75, -0.75)).toVector())) continue
                            if (entity is Player) entity.setTag("lastprismzapdebuff", 20)
                            else entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(e.player as Entity).withCausingEntity(e.player as Entity).build())
                        }
                    }
                }
                if (!startingLocation.add(facing).block.isPassable) {
                    break
                }
            }
            prism.setCooldown(e.player, 10.0, "Zap")
            CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_HIT, 1F, 1.3F)
        } else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
            var prism: ItemStack? = null
            if (e.player.inventory.itemInOffHand.isItem(CustomItem.LAST_PRISM)) prism = e.player.inventory.itemInOffHand
            if (e.player.inventory.itemInMainHand.isItem(CustomItem.LAST_PRISM)) prism = e.player.inventory.itemInMainHand
            if (prism == null) return
            if (!prism.offCooldown(e.player, "Beam")) return
            e.player.setTag("lastprismused", true)
        }
    }*/
    /*private fun pewMaticHorn(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        var horn: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.PEW_MATIC_HORN)) horn = e.player.inventory.itemInOffHand
        if (e.player.inventory.itemInMainHand.isItem(CustomItem.PEW_MATIC_HORN)) horn = e.player.inventory.itemInMainHand
        if (horn == null) return
        if (!horn.offCooldown(e.player)) return
        e.player.setTag("pewmatichornused", true)
    }*/
    /*private fun experienceFlask(e: PlayerInteractEvent) {
        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            if (e.item == null) return
            if (!e.item!!.isItem(CustomItem.EXPERIENCE_FLASK)) return
            if (!e.player.offCooldown(CustomItem.EXPERIENCE_FLASK)) return
            if (e.player.timeSinceCombatTimeStamp() < 20 * 60 * 5) {
                e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                return
            }
            if (e.player.isSneaking) {
                e.player.giveExp(e.item!!.getTag<Int>("storedexp")!!, false)
                e.item!!.setTag("storedexp", 0)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.1F)
            } else {
                e.item!!.setTag("storedexp", e.item!!.getTag<Int>("storedexp")!! + e.player.calculateTotalExperiencePoints())
                e.player.level = 0
                e.player.exp = 0F
                CustomEffects.playSound(e.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.9F)
            }
            e.item!!.loreBlock(text("Stored experience: ${e.item!!.getTag<Int>("storedexp")!!}", arrayOf(73, 209, 10)),
                text(""),
                text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY))
            e.player.setCooldown(CustomItem.EXPERIENCE_FLASK, 0.5)
        } else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
            var flask: ItemStack? = null
            if (e.player.inventory.itemInOffHand.isItem(CustomItem.EXPERIENCE_FLASK)) flask = e.player.inventory.itemInOffHand
            if (e.player.inventory.itemInMainHand.isItem(CustomItem.EXPERIENCE_FLASK)) flask = e.player.inventory.itemInMainHand
            if (flask == null) return
            if (!e.player.offCooldown(CustomItem.EXPERIENCE_FLASK)) return
            if (e.player.timeSinceCombatTimeStamp() < 20 * 60 * 5) {
                e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                return
            }
            if (e.player.isSneaking) {
                e.player.giveExp(flask.getTag<Int>("storedexp")!!.coerceAtMost(1395), false)
                flask.setTag("storedexp", flask.getTag<Int>("storedexp")!! - flask.getTag<Int>("storedexp")!!.coerceAtMost(1395))
            } else {
                e.player.giveExp(flask.getTag<Int>("storedexp")!!.coerceAtMost(1395), true)
                flask.setTag("storedexp", flask.getTag<Int>("storedexp")!! - flask.getTag<Int>("storedexp")!!.coerceAtMost(1395))
            }
            e.item!!.loreBlock(text("Stored experience: ${e.item!!.getTag<Int>("storedexp")!!}", arrayOf(73, 209, 10)),
                text(""),
                text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY))
            e.player.setCooldown(CustomItem.EXPERIENCE_FLASK, 0.5)
        }
    }*/
    /*private fun netheriteMultitool(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.NETHERITE_MULTITOOL)) return
        if (!e.player.offCooldown(CustomItem.NETHERITE_MULTITOOL)) return
        if (!e.player.isSneaking) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val toolNum = e.item!!.getTag<Int>("tool")!!
        val newMat = when (toolNum) {
            0 -> Material.NETHERITE_PICKAXE
            1 -> Material.NETHERITE_AXE
            2 -> Material.NETHERITE_SHOVEL
            3 -> Material.NETHERITE_HOE
            else -> Material.AIR
        }
        e.item!!.type = newMat
        e.item!!.setTag("tool", if (toolNum == 3) 0 else toolNum + 1)
        e.player.setCooldown(CustomItem.NETHERITE_MULTITOOL, 0.5)
    }*/
    /*private fun pocketknifeMultitool(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.POCKETKNIFE_MULTITOOL)) return
        if (!e.player.offCooldown(CustomItem.POCKETKNIFE_MULTITOOL)) return
        if (!e.player.isSneaking) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val toolNum = e.item!!.getTag<Int>("tool")!!
        val newMat = when (toolNum) {
            0 -> Material.SHEARS
            1 -> Material.FLINT_AND_STEEL
            2 -> Material.BRUSH
            else -> Material.AIR
        }
        e.item!!.type = newMat
        e.item!!.setTag("tool", if (toolNum == 2) 0 else toolNum + 1)
        e.player.setCooldown(CustomItem.POCKETKNIFE_MULTITOOL, 0.5)
    }*/
    /*private fun hoeHoe(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.HOE)) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!Tag.DIRT.isTagged(e.clickedBlock!!.type)) return
        e.isCancelled = false
        for (loc in getAround(e.clickedBlock!!.location)) {
            if (e.player.world.getBlockAt(loc.clone().add(Vector(0, 1, 0))).type != Material.AIR) continue
            if (!Tag.DIRT.isTagged(e.player.world.getBlockAt(loc).type)) continue
            e.player.world.getBlockAt(loc).type = Material.FARMLAND
        }
    }*/
    /*private fun enderBlade(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.ENDER_BLADE)) return
        if (!e.item!!.offCooldown(e.player)) return
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        val startLoc = e.player.eyeLocation.clone()
        val toAdd = e.player.location.direction.clone().normalize().multiply(0.05)
        for (i in 1..120) {
            startLoc.add(toAdd)
            if (!startLoc.add(toAdd).block.isPassable || !startLoc.clone().add(Vector(0, 1, 0)).block.isPassable) {
                startLoc.subtract(toAdd.multiply(2))
                break
            }
        }
        startLoc.pushOut(e.player.width)
        e.player.teleport(startLoc)
        CustomEffects.playSound(e.player.location, Sound.ENTITY_SHULKER_TELEPORT, 1.0F, 0.94F)
        if (e.player.isSneaking) {
            e.item!!.setCooldown(e.player, 15.0)
            e.player.setTag("enderbladecrittime", 6)
        } else {
            e.item!!.setCooldown(e.player, 6.5)
        }
    }*/
    /*private fun autoSmeltUpgrade(e: PlayerInteractEvent) {
        var smelt: ItemStack? = null
        for (custom in arrayOf(CustomItem.FIERY_SHARD)) if (e.player.inventory.itemInOffHand.isItem(custom)) smelt = e.player.inventory.itemInOffHand
        if (smelt == null) return
        val smeltable = e.player.inventory.itemInMainHand
        if (!Tag.ITEMS_PICKAXES.isTagged(smeltable.type) && !Tag.ITEMS_AXES.isTagged(smeltable.type) && !Tag.ITEMS_SHOVELS.isTagged(smeltable.type)) return
        if (CustomEnchantments.AUTOSMELT in smeltable.enchantments.keys) return
        e.isCancelled = true
        smelt.amount -= 1
        smeltable.addUnsafeEnchantment(CustomEnchantments.AUTOSMELT, 1)
        CustomEffects.playSound(e.player.location, Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1.0F, 1.1F)
    }*/
    /*private fun soulCrystal(e: PlayerInteractEvent) {
        var upgrade: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.SOUL_CRYSTAL)) upgrade = e.player.inventory.itemInOffHand
        if (upgrade == null) return
        val toSoulbind = e.player.inventory.itemInMainHand
        if (e.player.inventory.itemInMainHand.type == Material.AIR) return
        if (CustomEnchantments.SOULBOUND in toSoulbind.enchantments.keys) return
        e.isCancelled = true
        upgrade.amount -= 1
        toSoulbind.addUnsafeEnchantment(CustomEnchantments.SOULBOUND, 1)
        CustomEffects.playSound(e.player.location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 1.1F)
    }*/
    /*private fun netheriteCoating(e: PlayerInteractEvent) {
        var upgrade: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.NETHERITE_COATING)) upgrade = e.player.inventory.itemInOffHand
        if (upgrade == null) return
        val toUpgrade = e.player.inventory.itemInMainHand
        if (e.player.inventory.itemInMainHand.type == Material.AIR) return
        if (CustomEnchantments.FIREPROOF in toUpgrade.enchantments.keys) return
        e.isCancelled = true
        upgrade.amount -= 1
        toUpgrade.resist(DamageTypeTagKeys.IS_FIRE)
        toUpgrade.addUnsafeEnchantment(CustomEnchantments.FIREPROOF, 1)
        //lore
        CustomEffects.playSound(e.player.location, Sound.BLOCK_SMITHING_TABLE_USE, 1.0F, 1.1F)
    }*/
    /*private fun witherCoating(e: PlayerInteractEvent) {
        var upgrade: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.WITHER_COATING)) upgrade = e.player.inventory.itemInOffHand
        if (upgrade == null) return
        val toUpgrade = e.player.inventory.itemInMainHand
        if (e.player.inventory.itemInMainHand.type == Material.AIR) return
        if (CustomEnchantments.BLAST_RESISTANT in toUpgrade.enchantments.keys) return
        e.isCancelled = true
        upgrade.amount -= 1
        toUpgrade.resist(DamageTypeTagKeys.IS_EXPLOSION)
        toUpgrade.addUnsafeEnchantment(CustomEnchantments.BLAST_RESISTANT, 1)
        CustomEffects.playSound(e.player.location, Sound.ENTITY_WITHER_AMBIENT, 1.0F, 1.1F)
    }*/
    /*private fun reinforcingStruts(e: PlayerInteractEvent) {
        var upgrade: ItemStack? = null
        if (e.player.inventory.itemInOffHand.isItem(CustomItem.REINFORCING_STRUTS)) upgrade = e.player.inventory.itemInOffHand
        if (upgrade == null) return
        val toUpgrade = e.player.inventory.itemInMainHand
        if (e.player.inventory.itemInMainHand.type == Material.AIR) return
        if (toUpgrade.itemMeta !is Damageable) return
        if ((toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) >= 5) return
        e.isCancelled = true
        upgrade.amount -= 1
        val newMeta = toUpgrade.itemMeta as Damageable
        if (newMeta.hasMaxDamage()) toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, newMeta.maxDamage + 200)
        else toUpgrade.setData(DataComponentTypes.MAX_DAMAGE, toUpgrade.type.maxDurability + 200)
        toUpgrade.addUnsafeEnchantment(CustomEnchantments.REINFORCED, (toUpgrade.enchantments[CustomEnchantments.REINFORCED] ?: 0) + 1)
        CustomEffects.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 1.0F, 1.1F)
    }*/
    /*private fun tripleSwipeBlade(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.TRIPLE_SWIPE_SWORD)) return
        if (!e.item!!.offCooldown(e.player)) return
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        e.item!!.setCooldown(e.player, 15.0)
        var k = 2

        tasks.add(object : BukkitRunnable() { override fun run() {
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
        }}.runTaskTimer(CustomItems.plugin, 0L, 4L).taskId)
    }*/
    /*private fun windChargeCannonMode(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.WIND_CHARGE_CANNON)) return
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        val mode = e.item!!.getTag<Int>("mode")!!
        e.item!!.setTag("mode", if (mode == 1) 0 else 1)
        e.item!!.name(text("Wind Charge Cannon - ${if (mode == 1) "Homing" else "Straight"}", arrayOf(201, 240, 238), bold = true))
        e.player.playSound(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.0F)
    }*/
    /*private fun landmineLauncherTrigger(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.LANDMINE_LAUNCHER)) return
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        for (entity in e.player.world.entities) {
            if (entity.type != EntityType.ARROW) continue
            if (entity.getTag<Int>("id") != CustomEntity.LANDMINE_SHOT.id) continue
            entity.world.createExplosion(entity.location, 6.0F, false, true, e.player)
            entity.remove()
        }
    }*/
    /*private fun jetpackControllerPack(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.item?.isItem(CustomItem.JETPACK_CONTROLLER_SET) != true) return
        e.item!!.amount -= 1
        e.player.addItemorDrop(Items.get(CustomItem.JETPACK))
        e.player.addItemorDrop(Items.get(CustomItem.JETPACK_CONTROLLER))
    }*/
    /*private fun jetpackController(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.JETPACK_CONTROLLER)) return
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        val mode = e.player.getTag<Boolean>("jetpackactive") ?: false
        e.player.setTag("jetpackactive", !mode)
        e.item!!.name(text("Jetpack Controller - ${if (!mode) "ON" else "OFF"}", arrayOf(148, 134, 111), bold = true))
    }*/
    /*private fun reinforcedCagePlace(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.REINFORCED_CAGE)) return
        if (!e.player.offCooldown(CustomItem.REINFORCED_CAGE)) return
        val loc = e.clickedBlock!!.location
        loc.add(Vector(0.5, 1.0, 0.5))
        val entityAsString = e.item!!.getTag<String>("storedmob")
        e.isCancelled = true
        if (entityAsString == null) return
        else Bukkit.getEntityFactory().createEntitySnapshot(entityAsString).createEntity(loc)
        e.item!!.setTag("storedmob", "")
        CustomEffects.playSound(loc, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1F, 1.2F)
        e.item!!.loreBlock(
            text("Type: NONE STORED", Utils.GRAY),
            text(""),
            text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
        )
        e.player.setCooldown(CustomItem.REINFORCED_CAGE, 0.5)
    }*/
    /*private fun darkSteelRapierActivate(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (!e.item!!.isItem(CustomItem.DARK_STEEL_RAPIER)) return
        if (!e.item!!.offCooldown(e.player)) return
        if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
        e.item!!.setCooldown(e.player, 40.0)
        for (player in e.player.location.getNearbyPlayers(10.0)) {
            if (e.player == player) continue
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 160, 0))
            Bukkit.getLogger().info(player.name)
        }
        e.player.tempAttribute(Attribute.MOVEMENT_SPEED, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), 0.06, AttributeModifier.Operation.ADD_NUMBER), 15.0, "darksteelsword")
    }*/*/

    /*@EventHandler fun onEntityPlace(e: EntityPlaceEvent) {
        redstonePlacersMinecart(e)
    }
    private fun redstonePlacersMinecart(e: EntityPlaceEvent) {
        if (e.player == null) return
        var cart: ItemStack? = null
        if (e.player!!.inventory.itemInOffHand.isItem(CustomItem.MINECART_MATERIALS) || e.player!!.inventory.itemInOffHand.isItem(CustomItem.REDSTONE_AMALGAMATION)) cart = e.player!!.inventory.itemInOffHand
        if (e.player!!.inventory.itemInMainHand.isItem(CustomItem.MINECART_MATERIALS) || e.player!!.inventory.itemInMainHand.isItem(CustomItem.REDSTONE_AMALGAMATION)) cart = e.player!!.inventory.itemInMainHand
        if (cart == null) return
        if (cart.type != Material.MINECART && cart.type != Material.CHEST_MINECART &&
            cart.type != Material.TNT_MINECART && cart.type != Material.HOPPER_MINECART &&
            cart.type != Material.FURNACE_MINECART) return
        e.isCancelled = false
        val itemType = cart.type
        val materials = Utils.getMaterials(itemType)
        var shulkerItem: ItemStack? = null
        for (item in e.player!!.inventory) {
            if ((item?.type ?: Material.AIR) != Material.RED_SHULKER_BOX) continue
            shulkerItem = item
        }
        if (shulkerItem == null) {
            e.isCancelled = true
            e.player!!.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
            CustomEffects.playSound(e.player!!.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
            return
        }
        val shulkerInventory = ((shulkerItem.itemMeta as BlockStateMeta).blockState as ShulkerBox).inventory
        val counts = mutableMapOf(Pair("iron", 0), Pair("wood", 0), Pair("redstone", 0), Pair("stone", 0), Pair("cobblestone", 0), Pair("gold", 0), Pair("quartz", 0), Pair("string", 0), Pair("slimeball", 0), Pair("tnt", 0))
        val neededCounts = mutableMapOf(Pair("iron", 0), Pair("wood", 0), Pair("redstone", 0), Pair("stone", 0), Pair("cobblestone", 0), Pair("gold", 0), Pair("quartz", 0), Pair("string", 0), Pair("slimeball", 0), Pair("tnt", 0))
        //sum materials in shulker
        for (item in shulkerInventory) {
            if (item == null) continue
            if (Tag.LOGS.isTagged(item.type)) counts["wood"] = counts["wood"]!! + 4 * item.amount
            if (Tag.PLANKS.isTagged(item.type)) counts["wood"] = counts["wood"]!! + 1 * item.amount
            if (item.type == Material.STICK) counts["wood"] = counts["wood"]!! + (0.5 * item.amount).toInt()
            if (item.type == Material.IRON_BLOCK) counts["iron"] = counts["iron"]!! + 9 * item.amount
            if (item.type == Material.IRON_INGOT) counts["iron"] = counts["iron"]!! + 1 * item.amount
            if (item.type == Material.REDSTONE_BLOCK) counts["redstone"] = counts["redstone"]!! + 9 * item.amount
            if (item.type == Material.REDSTONE) counts["redstone"] = counts["redstone"]!! + 1 * item.amount
            if (item.type == Material.STONE) counts["stone"] = counts["stone"]!! + 1 * item.amount
            if (item.type == Material.COBBLESTONE) counts["cobblestone"] = counts["cobblestone"]!! + 1 * item.amount
            if (item.type == Material.GOLD_BLOCK) counts["gold"] = counts["gold"]!! + 9 * item.amount
            if (item.type == Material.GOLD_INGOT) counts["gold"] = counts["gold"]!! + 1 * item.amount
            if (item.type == Material.SLIME_BLOCK) counts["slimeball"] = counts["slimeball"]!! + 9 * item.amount
            if (item.type == Material.SLIME_BALL) counts["slimeball"] = counts["slimeball"]!! + 1 * item.amount
            if (item.type == Material.QUARTZ) counts["quartz"] = counts["quartz"]!! + 1 * item.amount
            if (item.type == Material.STRING) counts["string"] = counts["string"]!! + 1 * item.amount
            if (item.type == Material.TNT) counts["tnt"] = counts["tnt"]!! + 1 * item.amount
        }
        //sum materials in recipe
        for (item in materials) {
            if (Tag.LOGS.isTagged(item.type)) neededCounts["wood"] = neededCounts["wood"]!! + 4 * item.amount
            if (Tag.PLANKS.isTagged(item.type)) neededCounts["wood"] = neededCounts["wood"]!! + 1 * item.amount
            if (item.type == Material.STICK) neededCounts["wood"] = neededCounts["wood"]!! + (0.5 * item.amount).toInt()
            if (item.type == Material.IRON_BLOCK) neededCounts["iron"] = neededCounts["iron"]!! + 9 * item.amount
            if (item.type == Material.IRON_INGOT) neededCounts["iron"] = neededCounts["iron"]!! + 1 * item.amount
            if (item.type == Material.REDSTONE_BLOCK) neededCounts["redstone"] = neededCounts["redstone"]!! + 9 * item.amount
            if (item.type == Material.REDSTONE) neededCounts["redstone"] = neededCounts["redstone"]!! + 1 * item.amount
            if (item.type == Material.STONE) neededCounts["stone"] = neededCounts["stone"]!! + 1 * item.amount
            if (item.type == Material.COBBLESTONE) neededCounts["cobblestone"] = neededCounts["cobblestone"]!! + 1 * item.amount
            if (item.type == Material.GOLD_BLOCK) neededCounts["gold"] = neededCounts["gold"]!! + 9 * item.amount
            if (item.type == Material.GOLD_INGOT) neededCounts["gold"] = neededCounts["gold"]!! + 1 * item.amount
            if (item.type == Material.SLIME_BLOCK) neededCounts["slimeball"] = neededCounts["slimeball"]!! + 9 * item.amount
            if (item.type == Material.SLIME_BALL) neededCounts["slimeball"] = neededCounts["slimeball"]!! + 1 * item.amount
            if (item.type == Material.QUARTZ) neededCounts["quartz"] = neededCounts["quartz"]!! + 1 * item.amount
            if (item.type == Material.STRING) neededCounts["string"] = neededCounts["string"]!! + 1 * item.amount
            if (item.type == Material.TNT) neededCounts["tnt"] = neededCounts["tnt"]!! + 1 * item.amount
        }
        //compare
        for (key in counts.keys) {
            if (counts[key]!! < neededCounts[key]!!) {
                e.isCancelled = true
                e.player!!.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
                CustomEffects.playSound(e.player!!.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                return
            }
        }
        //take mats out of shulker
        for (currentMat in materials) {
            val remaining = currentMat.clone()
            for (realItem in shulkerInventory) {
                if (realItem == null) continue
                if (remaining.type == realItem.type) {
                    if (realItem.amount >= remaining.amount) {
                        realItem.amount -= remaining.amount
                        remaining.amount = 0
                        break
                    } else {
                        remaining.amount -= realItem.amount
                        realItem.amount = 0
                    }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type in arrayOf(
                    Material.IRON_INGOT,
                    Material.GOLD_INGOT,
                    Material.REDSTONE,
                    Material.SLIME_BALL
                )
            ) {
                val blockForm = toBlock(remaining.type)
                for (realItem in shulkerInventory) {
                    if (realItem == null) continue
                    if (realItem.type == blockForm) {
                        val toConvert = remaining.amount / 9 + 1
                        if (realItem.amount < toConvert) {
                            remaining.amount -= realItem.amount * 9
                            realItem.amount = 0
                        } else {
                            realItem.amount -= toConvert
                            var total = toConvert * 9
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type == Material.OAK_PLANKS) {
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.PLANKS.isTagged(item.type)) {
                        if (item.amount >= remaining.amount) {
                            item.amount -= remaining.amount
                            remaining.amount = 0
                            break
                        } else {
                            remaining.amount -= item.amount
                            item.amount = 0
                        }
                    }
                }
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.LOGS.isTagged(item.type)) {
                        val toConvert = remaining.amount / 4 + 1
                        if (item.amount < toConvert) {
                            remaining.amount -= item.amount * 4
                            item.amount = 0
                        } else {
                            item.amount -= toConvert
                            var total = toConvert * 4
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type == Material.STICK) {
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.PLANKS.isTagged(item.type)) {
                        if (item.amount >= ceil(remaining.amount / 2.0)) {
                            item.amount -= ceil(remaining.amount / 2.0).toInt()
                            remaining.amount = 0
                            break
                        } else {
                            remaining.amount -= item.amount * 2
                            item.amount = 0
                        }
                    }
                }
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.LOGS.isTagged(item.type)) {
                        val toConvert = remaining.amount / 8 + 1
                        if (item.amount < toConvert) {
                            remaining.amount -= item.amount * 8
                            item.amount = 0
                        } else {
                            item.amount -= toConvert
                            var total = toConvert * 8
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount != 0) {
                e.isCancelled = true
                e.player!!.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
                CustomEffects.playSound(e.player!!.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                return
            }
        }
        val newMeta = shulkerItem.itemMeta as BlockStateMeta
        val newBlockState = newMeta.blockState as ShulkerBox
        val inventory = newBlockState.inventory
        inventory.contents = shulkerInventory.contents
        newMeta.blockState = newBlockState
        shulkerItem.itemMeta = newMeta
        newBlockState.update()
        var slot = 0
        slot = if (cart == e.player!!.inventory.itemInMainHand) e.player!!.inventory.heldItemSlot else 40
        val savedItem = ItemStack(cart)
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.player!!.inventory.getItem(slot)?.type == Material.AIR || e.player!!.inventory.getItem(slot) == null) {
                e.player!!.inventory.setItem(slot, savedItem)
            }
        })
    }*/

    @EventHandler fun onItemCraft(e: CraftItemEvent) {
        cancelCustomCrafts(e)
        duplicateArmorTrims(e)
    }
    private fun cancelCustomCrafts(e: CraftItemEvent) {
        for (item in e.inventory) {
            if (item == null) continue
            if (item.itemMeta == null) continue
            if (item.getTag<Int>("id") != null) e.isCancelled = true
        }
    }
    private fun duplicateArmorTrims(e: CraftItemEvent) {
        val result = e.recipe.result
        if (result.type !in arrayOf(
            Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
            Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE
        )) return
        if (e.inventory.getItem(2)!!.enchantments[CustomEnchantments.DUPLICATE] == 1) {
            e.whoClicked.sendMessage(text("You cannot use duplicated trims in this recipe.", Utils.FAILED_COLOR))
            (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            e.isCancelled = true
            return
        }
        if (e.isShiftClick) {
            e.isCancelled = true
            return
        }
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.inventory.getItem(2)?.type != result.type) {
                val newResult = ItemStack(result.type)
                e.inventory.setItem(2, newResult)
            } else {
                e.inventory.getItem(2)!!.amount = 2
            }
        })
    }
    /*@EventHandler fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.itemInHand.getTag<Int>("id") != null && e.itemInHand.getCustom() !in arrayOf(
                CustomItem.ACTUAL_REDSTONE, CustomItem.CONTAINERS, CustomItem.MINECART_MATERIALS, CustomItem.INPUT_DEVICES,
                CustomItem.POCKETKNIFE_MULTITOOL, CustomItem.TREECAPITATOR
            )) {
            e.isCancelled = true
        }
        redstonePlacers(e)
    }
    private fun redstonePlacers(e: BlockPlaceEvent) {
        if (e.itemInHand.itemMeta == null) return
        if (!e.itemInHand.isItem(CustomItem.INPUT_DEVICES) && !e.itemInHand.isItem(CustomItem.MINECART_MATERIALS) &&
            !e.itemInHand.isItem(CustomItem.ACTUAL_REDSTONE) && !e.itemInHand.isItem(CustomItem.CONTAINERS) &&
            !e.itemInHand.isItem(CustomItem.REDSTONE_AMALGAMATION)) return
        e.isCancelled = false
        val itemType = e.itemInHand.type
        val materials = Utils.getMaterials(itemType)
        var shulkerItem: ItemStack? = null
        for (item in e.player.inventory) {
            if ((item?.type ?: Material.AIR) != Material.RED_SHULKER_BOX) continue
            shulkerItem = item
        }
        if (shulkerItem == null) {
            e.isCancelled = true
            e.player.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
            CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
            return
        }
        val shulkerInventory = ((shulkerItem.itemMeta as BlockStateMeta).blockState as ShulkerBox).inventory
        val counts = mutableMapOf(Pair("iron", 0), Pair("wood", 0), Pair("redstone", 0), Pair("stone", 0), Pair("cobblestone", 0), Pair("gold", 0), Pair("quartz", 0), Pair("string", 0), Pair("slimeball", 0), Pair("tnt", 0))
        val neededCounts = mutableMapOf(Pair("iron", 0), Pair("wood", 0), Pair("redstone", 0), Pair("stone", 0), Pair("cobblestone", 0), Pair("gold", 0), Pair("quartz", 0), Pair("string", 0), Pair("slimeball", 0), Pair("tnt", 0))
        //sum materials in shulker
        for (item in shulkerInventory) {
            if (item == null) continue
            if (Tag.LOGS.isTagged(item.type)) counts["wood"] = counts["wood"]!! + 4 * item.amount
            if (Tag.PLANKS.isTagged(item.type)) counts["wood"] = counts["wood"]!! + 1 * item.amount
            if (item.type == Material.STICK) counts["wood"] = counts["wood"]!! + (0.5 * item.amount).toInt()
            if (item.type == Material.IRON_BLOCK) counts["iron"] = counts["iron"]!! + 9 * item.amount
            if (item.type == Material.IRON_INGOT) counts["iron"] = counts["iron"]!! + 1 * item.amount
            if (item.type == Material.REDSTONE_BLOCK) counts["redstone"] = counts["redstone"]!! + 9 * item.amount
            if (item.type == Material.REDSTONE) counts["redstone"] = counts["redstone"]!! + 1 * item.amount
            if (item.type == Material.STONE) counts["stone"] = counts["stone"]!! + 1 * item.amount
            if (item.type == Material.COBBLESTONE) counts["cobblestone"] = counts["cobblestone"]!! + 1 * item.amount
            if (item.type == Material.GOLD_BLOCK) counts["gold"] = counts["gold"]!! + 9 * item.amount
            if (item.type == Material.GOLD_INGOT) counts["gold"] = counts["gold"]!! + 1 * item.amount
            if (item.type == Material.SLIME_BLOCK) counts["slimeball"] = counts["slimeball"]!! + 9 * item.amount
            if (item.type == Material.SLIME_BALL) counts["slimeball"] = counts["slimeball"]!! + 1 * item.amount
            if (item.type == Material.QUARTZ) counts["quartz"] = counts["quartz"]!! + 1 * item.amount
            if (item.type == Material.STRING) counts["string"] = counts["string"]!! + 1 * item.amount
            if (item.type == Material.TNT) counts["tnt"] = counts["tnt"]!! + 1 * item.amount
        }
        //sum materials in recipe
        for (item in materials) {
            if (Tag.LOGS.isTagged(item.type)) neededCounts["wood"] = neededCounts["wood"]!! + 4 * item.amount
            if (Tag.PLANKS.isTagged(item.type)) neededCounts["wood"] = neededCounts["wood"]!! + 1 * item.amount
            if (item.type == Material.STICK) neededCounts["wood"] = neededCounts["wood"]!! + (0.5 * item.amount).toInt()
            if (item.type == Material.IRON_BLOCK) neededCounts["iron"] = neededCounts["iron"]!! + 9 * item.amount
            if (item.type == Material.IRON_INGOT) neededCounts["iron"] = neededCounts["iron"]!! + 1 * item.amount
            if (item.type == Material.REDSTONE_BLOCK) neededCounts["redstone"] = neededCounts["redstone"]!! + 9 * item.amount
            if (item.type == Material.REDSTONE) neededCounts["redstone"] = neededCounts["redstone"]!! + 1 * item.amount
            if (item.type == Material.STONE) neededCounts["stone"] = neededCounts["stone"]!! + 1 * item.amount
            if (item.type == Material.COBBLESTONE) neededCounts["cobblestone"] = neededCounts["cobblestone"]!! + 1 * item.amount
            if (item.type == Material.GOLD_BLOCK) neededCounts["gold"] = neededCounts["gold"]!! + 9 * item.amount
            if (item.type == Material.GOLD_INGOT) neededCounts["gold"] = neededCounts["gold"]!! + 1 * item.amount
            if (item.type == Material.SLIME_BLOCK) neededCounts["slimeball"] = neededCounts["slimeball"]!! + 9 * item.amount
            if (item.type == Material.SLIME_BALL) neededCounts["slimeball"] = neededCounts["slimeball"]!! + 1 * item.amount
            if (item.type == Material.QUARTZ) neededCounts["quartz"] = neededCounts["quartz"]!! + 1 * item.amount
            if (item.type == Material.STRING) neededCounts["string"] = neededCounts["string"]!! + 1 * item.amount
            if (item.type == Material.TNT) neededCounts["tnt"] = neededCounts["tnt"]!! + 1 * item.amount
        }
        //compare
        for (key in counts.keys) {
            if (counts[key]!! < neededCounts[key]!!) {
                e.isCancelled = true
                e.player.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                return
            }
        }
        //take mats out of shulker
        for (currentMat in materials) {
            val remaining = currentMat.clone()
            for (realItem in shulkerInventory) {
                if (realItem == null) continue
                if (remaining.type == realItem.type) {
                   if (realItem.amount >= remaining.amount) {
                       realItem.amount -= remaining.amount
                       remaining.amount = 0
                       break
                   } else {
                       remaining.amount -= realItem.amount
                       realItem.amount = 0
                   }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type in arrayOf(Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE, Material.SLIME_BALL)) {
                val blockForm = toBlock(remaining.type)
                for (realItem in shulkerInventory) {
                    if (realItem == null) continue
                    if (realItem.type == blockForm) {
                        val toConvert = remaining.amount/9 + 1
                        if (realItem.amount < toConvert) {
                            remaining.amount -= realItem.amount * 9
                            realItem.amount = 0
                        } else {
                            realItem.amount -= toConvert
                            var total = toConvert*9
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type == Material.OAK_PLANKS) {
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.PLANKS.isTagged(item.type)) {
                        if (item.amount >= remaining.amount) {
                            item.amount -= remaining.amount
                            remaining.amount = 0
                            break
                        } else {
                            remaining.amount -= item.amount
                            item.amount = 0
                        }
                    }
                }
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.LOGS.isTagged(item.type)) {
                        val toConvert = remaining.amount/4 + 1
                        if (item.amount < toConvert) {
                            remaining.amount -= item.amount * 4
                            item.amount = 0
                        } else {
                            item.amount -= toConvert
                            var total = toConvert*4
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount == 0) continue
            if (remaining.type == Material.STICK) {
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.PLANKS.isTagged(item.type)) {
                        if (item.amount >= ceil(remaining.amount / 2.0)) {
                            item.amount -= ceil(remaining.amount / 2.0).toInt()
                            remaining.amount = 0
                            break
                        } else {
                            remaining.amount -= item.amount * 2
                            item.amount = 0
                        }
                    }
                }
                for (item in shulkerInventory) {
                    if (item == null) continue
                    if (Tag.LOGS.isTagged(item.type)) {
                        val toConvert = remaining.amount/8 + 1
                        if (item.amount < toConvert) {
                            remaining.amount -= item.amount * 8
                            item.amount = 0
                        } else {
                            item.amount -= toConvert
                            var total = toConvert*8
                            total -= remaining.amount
                            shulkerInventory.addItem(ItemStack(remaining.type, total))
                            remaining.amount = 0
                            break
                        }
                    }
                }
            }
            if (remaining.amount != 0) {
                e.isCancelled = true
                e.player.sendActionBar(text("Not enough materials", arrayOf(227, 57, 27)))
                CustomEffects.playSound(e.player.location, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
                return
            }
        }
        val newMeta = shulkerItem.itemMeta as BlockStateMeta
        val newBlockState = newMeta.blockState as ShulkerBox
        val inventory = newBlockState.inventory
        inventory.contents = shulkerInventory.contents
        newMeta.blockState = newBlockState
        shulkerItem.itemMeta = newMeta
        newBlockState.update()
        val slot = if (e.itemInHand == e.player.inventory.itemInMainHand) e.player.inventory.heldItemSlot else 40
        val savedItem = ItemStack(e.itemInHand).clone()
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.player.inventory.getItem(slot)?.type == Material.AIR || e.player.inventory.getItem(slot) == null) {
                e.player.inventory.setItem(slot, savedItem)
            } else {
                e.player.addItemorDrop(savedItem)
            }
            if (e.blockPlaced.state is Container) {
                val newState = (e.block.state as Container)
                newState.customName(null)
                newState.update()
            }
        })
    }
    private fun toBlock(base: Material): Material {
        return when (base) {
            Material.IRON_INGOT -> Material.IRON_BLOCK
            Material.GOLD_INGOT -> Material.GOLD_BLOCK
            Material.REDSTONE -> Material.REDSTONE_BLOCK
            Material.SLIME_BALL -> Material.SLIME_BLOCK
            else -> Material.STONE
        }
    }*/

    private val tasks = mutableListOf<Int>()
    fun cancelTasks() {
        for (task in tasks) {
            Bukkit.getScheduler().cancelTask(task)
        }
        mainFuture.cancel()
    }

    private var counter: Int = 0
    private lateinit var mainFuture: BukkitTask

    override fun run() {
        mainFuture = Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
            counter = if (counter == 2400) 0 else counter + 1
            //every 3 seconds
            //Bukkit.getServer().getPlayer("NewburyMiner")?.sendMessage(counter.toString())
            //hotv beacon effect
            if ((counter % 60) == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    villagerIdolApply(player)
                    armorEffectApply(player)
                }
            }
            //ender blade crit, elytra disable time reduce time
            if ((counter % 20) == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    decrementOneSecond(player)
                    assassinsSetUpdate(player)
                }
            }
            //fang staff aura
            if ((counter % 10) == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    decrementTenTicks(player)
                }
            }
            //fang staff hold right click
            if (counter % 6 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    evokerStaffTick(player)
                    lastPrismTick(player)
                    pewmaticHornTick(player)
                }
            }
            //pewmatic horn shoot
            if (counter % 4 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    pewmaticHornShoot(player)
                    graveInvulnerabilityTick(player)
                }
            }
            //zap debuff tick
            if (counter % 2 == 0) {
                for (player in Bukkit.getServer().onlinePlayers) {
                    lastPrismZapDebuffTick(player)
                }
            }
            for (player in Bukkit.getServer().onlinePlayers) {
                windHookPull(player)
                polarizedMagnetPull(player)
                lastPrismDamage(player)
                surfaceToAirMissile(player)
                homingWindChargeUpdate(player)
                updateJetpack(player)
            }
        }, 0L, 1L)
    }

    private fun assassinsSetUpdate(player: Player) {
        if (player.getAttribute(Attribute.MOVEMENT_SPEED)!!.getModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed")) != null) {
            player.getAttribute(Attribute.MOVEMENT_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"))
            player.getAttribute(Attribute.ATTACK_DAMAGE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"))
            player.getAttribute(Attribute.ATTACK_SPEED)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"))
            player.getAttribute(Attribute.SCALE)!!.removeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"))
        }
        val fullSet = (
            player.inventory.helmet?.isItem(CustomItem.ASSASSINS_HOOD) == true &&
            player.inventory.chestplate?.isItem(CustomItem.ASSASSINS_ROBE) == true &&
            player.inventory.leggings?.isItem(CustomItem.ASSASSINS_LEGGINGS) == true &&
            player.inventory.boots?.isItem(CustomItem.ASSASSINS_LOAFERS) == true
        )
        if (!fullSet) return
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 25, 0, false, false))
        val currentStep = player.getTag<Int>("assassinsstep") ?: 0
        if (currentStep < 10) player.setTag("assassinsstep", currentStep + 1)
        player.getAttribute(Attribute.MOVEMENT_SPEED)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsspeed"), 0.004 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.ATTACK_DAMAGE)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsdamage"), 0.6 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.ATTACK_SPEED)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsattackspeed"), 0.02 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
        player.getAttribute(Attribute.SCALE)!!.addModifier(AttributeModifier(NamespacedKey(CustomItems.plugin, "assassinsscale"), -0.03 * currentStep, AttributeModifier.Operation.ADD_NUMBER))
    }
    private fun updateJetpack(player: Player) {
        val jetpackEquipped = player.inventory.chestplate?.isItem(CustomItem.JETPACK) ?: false
        val jetpackActive = player.getTag<Boolean>("jetpackactive") ?: false
        if (jetpackActive && player.isInCombat()) {
            player.setTag("jetpackactive", false)
            player.playSound(player, Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F)
        }
        if (!jetpackEquipped && !jetpackActive) return
        if (!jetpackActive) {
            player.inventory.chestplate!!.removeAttr()
            player.inventory.chestplate!!.attr("ARM+8.0CH","ART+3.0CH","KNR+0.1CH")
        } else if (jetpackEquipped) {
            var controller: ItemStack? = null
            if (player.inventory.itemInOffHand.isItem(CustomItem.JETPACK_CONTROLLER)) controller = player.inventory.itemInOffHand
            if (player.inventory.itemInMainHand.isItem(CustomItem.JETPACK_CONTROLLER)) controller = player.inventory.itemInMainHand
            val realUpVel = player.y - (player.getTag<Double>("prevyval") ?: player.y)
            player.setTag("prevyval", player.y)
            val upVel = player.velocity.y
            val controllerActive =
                if (controller == null) {false}
                else if (player.activeItemUsedTime > 0) {true}
                else {false}


            //player.sendMessage(controllerActive.toString())
            //player.sendMessage(upVel.toString())
            //player.sendMessage(realUpVel.toString())
            //player.sendMessage(player.isSneaking.toString())
            val grav: Double
            if /* acc up */ (controllerActive && realUpVel * 20 < 2.5) {
                grav = -0.08 + -0.08
                //Bukkit.getLogger().info("acc up")
            } /* constant up */ else if (controllerActive) {
                grav = -0.02 / 0.98 * realUpVel + -0.08
                //Bukkit.getLogger().info("constant up")
            } /* acc down */ else if (player.isSneaking && realUpVel * 20 > -2.5) {
                grav = 0.08 + -0.08
                //Bukkit.getLogger().info("acc down")
            } /* constant down */ else if (player.isSneaking) {
                grav = -0.02 / 0.98 * realUpVel + -0.08
                //Bukkit.getLogger().info("constant down")
            } /* slowing up */ else if (!player.isSneaking && realUpVel < -0.01) {
                grav = 0.1 * realUpVel + -0.08
                //Bukkit.getLogger().info("slowing up")
            } /* slowing down */ else if (realUpVel > 0.01) {
                grav = 0.1 * realUpVel + -0.08
                //Bukkit.getLogger().info("slowing down")
            } /* hovering */ else {
                grav = 0.0 + -0.08
                //Bukkit.getLogger().info("hovering")
            }
            val gravityString = if (grav >= 0.0) "+$grav" else grav.toString()
            player.inventory.chestplate!!.removeAttr()
            player.inventory.chestplate!!.attr("ARM+8.0CH","ART+3.0CH","KNR+0.1CH", "GRA${gravityString}CH", "FAD-1.0CH")
        }
    }
    private fun armorEffectApply(player: Player) {
        if (player.inventory.chestplate?.isItem(CustomItem.TURTLE_SHELL) == true) player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 65, 0, false, false))
        if (player.inventory.chestplate?.isItem(CustomItem.BERSERKER_CHESTPLATE) == true) player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 65, 0, false, false))
        if (player.inventory.chestplate?.isItem(CustomItem.MOLTEN_CHESTPLATE) == true) player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 65, 0, false, false))
        if (player.inventory.boots?.isItem(CustomItem.CLOUD_BOOTS) == true) player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 65, 1, false, false))
        if (player.inventory.helmet?.isItem(CustomItem.INVISIBILITY_CLOAK) == true) player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 65, 0, false, false))
        if (player.inventory.boots?.isItem(CustomItem.AQUEOUS_SANDALS) == true) player.addPotionEffect(PotionEffect(PotionEffectType.WATER_BREATHING, 65, 0, false, false))
        if (player.inventory.boots?.isItem(CustomItem.AQUEOUS_SANDALS) == true) player.addPotionEffect(PotionEffect(PotionEffectType.CONDUIT_POWER, 65, 0, false, false))
    }
    private fun homingWindChargeUpdate(player: Player) {
        for (entity in player.getNearbyEntities(60.0, 60.0, 60.0)) {
            if (entity.type != EntityType.WIND_CHARGE) continue
            if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
            entity.setTag("tick", Bukkit.getServer().currentTick)
            if (entity.getTag<Int>("id") != CustomEntity.WIND_CANNON_CHARGE.id) continue
            val target = Bukkit.getEntity(entity.getTag<UUID>("target")!!) ?: continue
            //val newDirection = entity.velocity.add(target.location.subtract(entity.location).toVector().normalize().multiply(1.5))
            //entity.velocity = newDirection.normalize().multiply(currentVelocity)
            val cross = entity.velocity.getCrossProduct(target.location.subtract(entity.location).toVector())
            val angle = entity.velocity.angle(target.location.subtract(entity.location).toVector())
            val newDirection = entity.velocity.rotateAroundAxis(cross, angle.coerceAtMost((Math.PI / 24).toFloat()).toDouble())
            entity.velocity = newDirection
        }
    }
    private fun surfaceToAirMissile(player: Player) {
        for (entity in player.getNearbyEntities(60.0, 60.0, 60.0)) {
            if (entity.type != EntityType.ARROW || (entity as Arrow).isInBlock) continue
            if (entity.getTag<Int>("tick") == Bukkit.getServer().currentTick) continue
            entity.setTag("tick", Bukkit.getServer().currentTick)
            if (entity.getTag<Int>("id") != CustomEntity.ELYTRA_BREAKER_ARROW.id) continue
            // here is null
            val target = Bukkit.getEntity(entity.getTag<UUID>("target")!!) ?: continue
            val newDirection = target.location.subtract(entity.location).toVector().add(Vector(0.0, 0.5, 0.0))
            entity.velocity = newDirection.normalize().multiply((target.location.subtract(entity.location).length() / 8).coerceAtLeast(8.0))
        }
    }
    private fun lastPrismDamage(player: Player) {
        if ((player.getTag<Int>("lastprismcounter") ?: 0) > 12) {
            val facing = player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
            for (i in 0..1200) {
                CustomEffects.particle(Particle.ELECTRIC_SPARK.builder(), startingLocation, 1)
                if (i % 5 == 0) {
                    for (entity in startingLocation.getNearbyEntities(3.0, 3.0, 3.0)) {
                        if (entity is LivingEntity && entity != player) {
                            if (!entity.boundingBox.overlaps(
                                    startingLocation.clone().add(Vector(0.75, 0.75, 0.75)).toVector(),
                                    startingLocation.clone().add(Vector(-0.75, -0.75, -0.75)).toVector()
                                )
                            ) continue
                            entity.damage(21.0,
                                DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(player as Entity)
                                    .withCausingEntity(player as Entity).build()
                            )
                        }
                    }
                }
                if (!startingLocation.add(facing).block.isPassable) {
                    break
                }
            }
        }
    }
    private fun polarizedMagnetPull(player: Player) {
        if ((player.getTag<Int>("polarizedmagnetpulling") ?: 0) > 0) {
            for (entity in player.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (entity is EnderPearl || entity is Arrow) continue
                val dist = player.location.subtract(entity.location).toVector()
                dist.multiply(0.05)
                entity.velocity = entity.velocity.add(dist)
            }
            player.setTag("polarizedmagnetpulling", player.getTag<Int>("polarizedmagnetpulling")!! - 1)
        }
        if (player.getTag<Boolean>("polarizedmagnetitempull") == true && player.hasCustom(CustomItem.POLARIZED_MAGNET)) {
            for (entity in player.getNearbyEntities(12.0, 12.0, 12.0)) {
                if (entity.type != EntityType.ITEM && entity.type != EntityType.EXPERIENCE_ORB) continue
                val dist = player.location.subtract(entity.location).toVector()
                dist.multiply(0.07)
                entity.velocity = entity.velocity.add(dist)
            }
        }
    }
    private fun windHookPull(player: Player) {
        if ((player.getTag<Int>("windhookpulltime") ?: 0) > 0) {
            val timeLeft = player.getTag<Int>("windhookpulltime")!!
            player.setTag("windhookpulltime", timeLeft - 1)
            val pullLoc = player.getTag<String>("windhookpullcoords")?.split(",")
            val direction =
                Location(player.world, pullLoc!![0].toDouble(), pullLoc[1].toDouble(), pullLoc[2].toDouble()).subtract(
                    player.location
                )
            if (direction.length() < 6.0) player.setTag("windhookpulltime", 0)
            val toAdd = direction.toVector().normalize().multiply(3)
            player.velocity = toAdd.clone().add(Vector(0.0, 0.4, 0.0))
            CustomEffects.particleLine(
                Particle.DOLPHIN.builder(), player.location,
                Location(player.world, pullLoc[0].toDouble(), pullLoc[1].toDouble(), pullLoc[2].toDouble()),
                400
            )
            if (counter % 20 == 0) {
                CustomEffects.playSound(
                    player.location,
                    arrayOf(Sound.ENTITY_BREEZE_IDLE_AIR, Sound.ENTITY_BREEZE_IDLE_GROUND).random(),
                    1F,
                    1.2F
                )
            }
        }
    }
    private fun lastPrismZapDebuffTick(player: Player) {
        if (player.getTag<Int>("lastprismzapdebuff") != 0 && player.getTag<Int>("lastprismzapdebuff") != null) {
            player.damage(2.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).build())
            player.noDamageTicks = 0
            player.setTag("lastprismzapdebuff", player.getTag<Int>("lastprismzapdebuff")!! - 1)
        }
    }
    private fun graveInvulnerabilityTick(player: Player) {
        if ((player.getTag<Int>("graveinvulnerability") ?: 0) > 0) {
            player.setTag("graveinvulnerability", player.getTag<Int>("graveinvulnerability")!! - 1)
            if (player.getTag<Int>("graveinvulnerability")!! == 0) {
                player.getAttribute(Attribute.GRAVITY)?.removeModifier(
                    AttributeModifier(
                        NamespacedKey(CustomItems.plugin, "gravity"),
                        -1.0,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                    )
                )
                player.isInvulnerable = false
            }
        }
    }
    private fun pewmaticHornShoot(player: Player) {
        if ((player.getTag<Int>("pewmatichorncounter") ?: 0) > 12) {
            val facing = player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
            val possProj: Array<Pair<EntityType, Double>> =
                arrayOf(
                    Pair(EntityType.ARROW, 1.0),
                    Pair(EntityType.SPECTRAL_ARROW, 1.0),
                    Pair(EntityType.SPLASH_POTION, 1.0),
                    Pair(EntityType.FALLING_BLOCK, 1.0),
                    Pair(EntityType.WIND_CHARGE, 1.0),
                    Pair(EntityType.FIREBALL, 1.0),
                    Pair(EntityType.TNT, 1.0),
                    Pair(EntityType.FIREWORK_ROCKET, 1.0),
                    Pair(EntityType.DRAGON_FIREBALL, 1.0),
                    Pair(EntityType.SMALL_FIREBALL, 1.0),
                    Pair(EntityType.BEE, 1.0),
                    Pair(EntityType.COD, 1.0),
                    Pair(EntityType.EGG, 1.0),
                    Pair(EntityType.SNOWBALL, 1.0),
                    Pair(EntityType.EXPERIENCE_BOTTLE, 1.0),
                    Pair(EntityType.PUFFERFISH, 1.0),
                    Pair(EntityType.SHULKER_BULLET, 1.0),
                    Pair(EntityType.SILVERFISH, 1.0),
                    Pair(EntityType.WITHER_SKULL, 1.0)
                )
            val type = possProj.random().first
            var entity: Entity? = null
            if (type == EntityType.ARROW) {
                entity = player.world.spawnEntity(startingLocation, type) as Arrow
                entity.basePotionType = PotionType.entries.random()
            } else if (type == EntityType.SPLASH_POTION) {
                entity = player.world.spawnEntity(startingLocation, type) as ThrownPotion
                val newMeta = entity.potionMeta
                newMeta.basePotionType = PotionType.entries.random()
                entity.potionMeta = newMeta
            } else if (type == EntityType.FALLING_BLOCK) {
                entity = player.world.spawnEntity(startingLocation, type) as FallingBlock
                entity.blockData = arrayOf(
                    Material.SAND.createBlockData(),
                    Material.POINTED_DRIPSTONE.createBlockData(),
                    Material.GRAVEL.createBlockData(),
                    Material.CYAN_CONCRETE.createBlockData(),
                    Material.DAMAGED_ANVIL.createBlockData()
                ).random()
            } else if (type == EntityType.FIREBALL) {
                entity = player.world.spawnEntity(startingLocation, type) as Fireball
                entity.yield = (Math.random() * 5).toFloat()
            } else if (type == EntityType.FIREWORK_ROCKET) {
                entity = player.world.spawnEntity(startingLocation, type) as Firework
                val newMeta = entity.fireworkMeta
                newMeta.power = (Math.random() * 6).toInt()
            } else {
                entity = player.world.spawnEntity(startingLocation, type)
            }
            entity.velocity = facing.multiply(40)
        }
    }
    private fun pewmaticHornTick(player: Player) {
        if (player.getTag<Boolean>("pewmatichornused") == true) {
            player.setTag("pewmatichorncounter", (player.getTag<Int>("pewmatichorncounter") ?: 0) + 1)
            val pewMaticHornCount = player.getTag<Int>("pewmatichorncounter")!!
            CustomEffects.playSound(
                player.location,
                Sound.BLOCK_AZALEA_PLACE,
                1F,
                (0.15 * (if (pewMaticHornCount > 12) 13 else pewMaticHornCount)).toFloat()
            )
        } else {
            if ((player.getTag<Int>("pewmatichorncounter") != 0 && player.getTag<Int>("pewmatichorncounter") != null) ||
                (player.getTag<Int>("pewmatichorncounter") ?: 0) > 42
            ) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
                val totalTime = player.getTag<Int>("pewmatichorncounter")!!
                if (totalTime > 12) {
                    player.setCooldown(CustomItem.PEW_MATIC_HORN, (0.3 * 4 * totalTime * 10).toInt() / 10.0)
                }
            }
            player.setTag("pewmatichorncounter", 0)
        }
        player.setTag("pewmatichornused", false)
    }
    private fun lastPrismTick(player: Player) {
        if (player.getTag<Boolean>("lastprismused") == true) {
            player.setTag("lastprismcounter", (player.getTag<Int>("lastprismcounter") ?: 0) + 1)
            val lastPrismCount = player.getTag<Int>("lastprismcounter")!!
            CustomEffects.playSound(
                player.location,
                Sound.ITEM_TRIDENT_THUNDER,
                1F,
                (0.15 * (if (lastPrismCount > 12) 13 else lastPrismCount)).toFloat()
            )
        } else {
            if (player.getTag<Int>("lastprismcounter") != 0 && player.getTag<Int>("lastprismcounter") != null) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
                val totalTime = player.getTag<Int>("lastprismcounter")!!
                if (totalTime > 12) {
                    player.setCooldown(CustomItem.LAST_PRISM, (0.3 * 4 * totalTime * 10).toInt() / 10.0, "Beam")
                }
            }
            player.setTag("lastprismcounter", 0)
        }
        player.setTag("lastprismused", false)
    }
    private fun evokerStaffTick(player: Player) {
        if (player.getTag<Int>("evokerstaffcounter") == 13) {
            player.setCooldown(CustomItem.FANGED_STAFF, 55.0, "Vexing")
            castFangStaffVexing(player)
            player.setTag("evokerstaffcounter", 0)
            player.setTag("evokerstaffused", false)
            CustomEffects.playSound(player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 20F, 0.9F)
            player.setTag("evokerstaffused", false)
            player.setTag("evokerstafftime", 20)
        }
        if (player.getTag<Boolean>("evokerstaffused") == true) {
            player.setTag("evokerstaffcounter", (player.getTag<Int>("evokerstaffcounter") ?: 0) + 1)
            CustomEffects.playSound(player.location, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1F, 1.2F)
        } else {
            if (player.getTag<Int>("evokerstaffcounter") != 0 && player.getTag<Int>("evokerstaffcounter") != null) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
            }
            player.setTag("evokerstaffcounter", 0)
        }
        player.setTag("evokerstaffused", false)
    }
    private fun decrementTenTicks(player: Player) {
        if ((player.getTag<Int>("evokerstafftime") ?: 0) > 0) {
            castFangStaffVexing(player)
            player.setTag("evokerstafftime", player.getTag<Int>("evokerstafftime")!! - 1)
        }
    }
    private fun decrementOneSecond(player: Player) {
        if (player.getTag<Int>("enderbladecrittime") != 0 && player.getTag<Int>("enderbladecrittime") != null) {
            player.setTag("enderbladecrittime", player.getTag<Int>("enderbladecrittime")!! - 1)
        }
        if (player.getTag<Int>("elytradisabled") != 0 && player.getTag<Int>("elytradisabled") != null) {
            player.setTag("elytradisabled", player.getTag<Int>("elytradisabled")!! - 1)
        }
    }
    private fun villagerIdolApply(player: Player) {
        for (entity in player.getNearbyEntities(50.0, 128.0, 50.0)) {
            if (entity.getTag<Int>("id") == CustomEntity.JERRY_IDOL.id) {
                //player.sendMessage("found")
                val amp = (entity.getTag<Int>("emeraldstacks") ?: 0)
                player.addPotionEffect(PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 340, amp))
            }
        }
    }
    private fun castFangStaffVexing(p: Player) {
        val centerLoc = p.location.clone().add(Vector(0.0, 1.6, 0.0))
        CustomEffects.particleSphere(Particle.ENCHANTED_HIT.builder(), centerLoc, 5.5, 60)
        for (entity in p.getNearbyEntities(7.0, 10.0, 7.0)) {
            if (entity !is LivingEntity) continue
            if (entity.location.subtract(centerLoc).length() <= 5.5) {
                entity.damage(13.0, DamageSource.builder(DamageType.PLAYER_ATTACK).withDirectEntity(p).withCausingEntity(p).build())
                entity.noDamageTicks = 0
            }
        }
    }
}