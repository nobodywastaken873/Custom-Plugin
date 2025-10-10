package me.newburyminer.customItems

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.*
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.TagKey
import me.newburyminer.customItems.Utils.Companion.compassCooldown
import me.newburyminer.customItems.Utils.Companion.isTracking
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.remainingCompassTime
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.useStoredEnch
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.persistent.PersistentCustomType
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.util.TriState
import org.bukkit.*
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.BlockType
import org.bukkit.block.Chest
import org.bukkit.block.data.BlockData
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.meta.MusicInstrumentMeta
import org.bukkit.inventory.meta.OminousBottleMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.nio.ByteBuffer
import java.util.UUID
import kotlin.math.*

class Utils {
    companion object {
        val FAILED_COLOR = arrayOf(227, 57, 27)
        val SUCCESS_COLOR = arrayOf(41, 222, 0)
        val GRAY = arrayOf(NamedTextColor.GRAY.value())
        val GREEN = arrayOf(NamedTextColor.DARK_GREEN.value())
        val BLUE = arrayOf(NamedTextColor.BLUE.value())

        fun Player.timeSinceCombatTimeStamp(): Int {
            val combatStart = this.getTag<Long>("combattimestamp") ?: 0
            val currentTime = System.currentTimeMillis()
            if (combatStart == 0L) return 0
            return ((currentTime - combatStart).toDouble() / 50).toInt()
        }
        fun Player.graveTeleportCooldown(): Int {
            val teleportExpiration = this.getTag<Long>("gravetpcooldown") ?: 0
            val currentTime = System.currentTimeMillis()
            if (currentTime < teleportExpiration) {
                return ((teleportExpiration - currentTime).toDouble() / 50).toInt()
            }
            return 0
        }
        fun Player.graveTeleportOnCooldown(): Boolean {
            return this.graveTeleportCooldown() != 0
        }
        fun Player.isTracking(): Boolean {
            return this.compassCooldown() != 0
        }
        fun Player.compassCooldown(): Int {
            val compassExpiration = this.getTag<Long>("compasscooldown") ?: 0
            val currentTime = System.currentTimeMillis()
            if (currentTime < compassExpiration) {
                return ((compassExpiration - currentTime).toDouble() / 50).toInt()
            }
            return 0
        }
        fun Player.isBeingTracked(): Boolean {
            return this.remainingCompassTime() != 0
        }
        fun Player.remainingCompassTime(): Int {
            val compassExpiration = this.getTag<Long>("compassend") ?: 0
            val currentTime = System.currentTimeMillis()
            if (currentTime < compassExpiration) {
                return ((compassExpiration - currentTime).toDouble() / 50).toInt()
            }
            return 0
        }
        fun Player.isAfk(): Boolean {
            return this.getTag<Boolean>("isafk") ?: false
        }
        fun Player.isInCombat(): Boolean {
            return this.combatTime() > 0
        }
        fun Player.afkTime(): Int {
            return this.getTag<Int>("afktime") ?: 0
        }
        fun Player.combatTime(): Int {
            return this.getTag<Int>("combattime") ?: 0
        }

        fun Double.randomToInt(): Int {
            if (this < 0) {
                var initial = this.toInt()
                if (Math.random() < abs(this - initial)) initial -= 1
                return initial
            } else {
                var initial = this.toInt()
                if (Math.random() < this - initial) initial += 1
                return initial
            }
        }
        fun randomRange(start: Double, end: Double): Double {
            return Math.random() * (end-start) + start
        }
        fun randomPercent(success: Double): Boolean {
            // success in x/100
            return Math.random() * 100 < success
        }
        fun World.getDifficultyIndex(location: Location): Double {
            // caps out at 100.0 * moonIndex (0.5 at new, 2 at full)? 100.0 * 4 during bloodmoon?
            //CustomItems.plugin.logger.info(gameTime.toString())
            //CustomItems.plugin.logger.info(location.chunk.inhabitedTime.toString())
            //val moonIndex = 0.75 * cos(((gameTime + 18000) % 192000) / 192000.0 * 2 * Math.PI) + 1.25
            //CustomItems.plugin.logger.info(moonIndex.toString())
            //val localIndex = 10.0 * (/* hours */ location.chunk.inhabitedTime / 20.0 / 60.0 / 60.0) / 80.0
            //CustomItems.plugin.logger.info(localIndex.toString())
            //val worldIndex = 10.0 * (/* days */ gameTime / 20.0 / 60.0 / 60.0 / 24.0) / 40.0
            //CustomItems.plugin.logger.info(worldIndex.toString())
            //return (1.0 * localIndex * worldIndex).coerceAtMost(100.0) * moonIndex
            return location.length() / 200.0
        }
        fun World.niceName(): String {
            return when (this) {
                Bukkit.getWorlds()[0] -> "Overworld"
                Bukkit.getWorlds()[1] -> "Nether"
                Bukkit.getWorlds()[2] -> "End"
                CustomItems.aridWorld -> "Arid Lands"
                CustomItems.bossWorld -> ""
                else -> ""
            }
        }

        fun LivingEntity.applyDamage(settings: DamageSettings) {
            val damageType = settings.damageType ?: DamageType.GENERIC
            val builder = DamageSource.builder(damageType)
            if (settings.damager != null) {
                builder.withDirectEntity(settings.damager).withCausingEntity(settings.damager)
            }
            this.damage(settings.damage, builder.build())
            if (settings.iframes != 10) this.noDamageTicks = settings.iframes
            //Bukkit.getLogger().info(damageType.toString())
            //Bukkit.getLogger().info(settings.damage.toString())
            this.velocity = this.velocity.add(settings.knockback?.getKnockback(this) ?: Vector())
        }
        fun Vector.rotateToAxis(centerAxis: Vector): Vector {
            //finds offset of direction from xz plane
            val phiOffset = atan2(centerAxis.y, sqrt(centerAxis.x.pow(2) + centerAxis.z.pow(2)))
            //offset of direction from x-axis on xz plane
            val thetaOffset = atan2(centerAxis.z, centerAxis.x)

            //phi += phiOffset
            //theta += thetaOffset
            val radius = this.length()

            return this.rotateAroundZ(phiOffset).rotateAroundY(-thetaOffset)
        }
        private fun Player.getHitboxHeight(): Double {
            return if (this.isGliding || this.isSwimming) 0.6 else if (this.isSneaking) 1.5 else 1.8
        }
        fun Player.getHitboxCorners(bottom: Boolean = false): MutableList<Location> {
            val height = this.getHitboxHeight()
            val corners = mutableListOf<Location>()
            for (offset in listOf(
                Vector(0.3, 0.0, 0.3), Vector(-0.3, 0.0, 0.3), Vector(0.3, 0.0, -0.3), Vector(-0.3, 0.0, -0.3),
                Vector(0.3, height, 0.3), Vector(-0.3, height, 0.3), Vector(0.3, height, -0.3), Vector(-0.3, height, -0.3),
            )) {
                if (bottom && offset.y == height) continue
                corners += this.location.add(offset)
            }
            return corners
        }
        fun BoundingBox.containsLoc(loc: Location, entityWorld: World): Boolean {
            if (loc.world != entityWorld) return false
            val vect = loc.toVector()
            return this.contains(vect)
        }
        fun BoundingBox.lineIntersects(start: Location, end: Location, entityWorld: World): Boolean {
            if (start.world != entityWorld) return false
            if (!BoundingBox(start.x, start.y, start.z, end.x, end.y, end.z).contains(this)) return false

            val direction = end.clone().subtract(start)
            val length = direction.length()
            val unit = direction.toVector().normalize().multiply(0.05)
            val newLoc = start.clone()
            for (i in 0..(length/0.05).toInt()) {
                if (this.containsLoc(newLoc, entityWorld)) return true
                newLoc.add(unit)
            }
            return false
        }
        fun BoundingBox.getCorners(world: World): MutableList<Location> {
            val corners = mutableListOf<Location>()
            for (x in arrayOf(this.maxX, this.minX)) for (y in arrayOf(this.maxY, this.minY)) for (z in arrayOf(this.maxZ, this.minZ)) {
                corners.add(Location(world, x, y, z))
            }
            return corners
        }
        fun Player.hasCustom(custom: CustomItem): Boolean {
            for (item in this.inventory) {
                if (item == null) continue
                if (item.isItem(custom)) return true
            }
             return false
        }
        fun Player.addItemorDrop(item: ItemStack) {
            if (this.inventory.firstEmpty() == -1) {
                val itemEntity = this.world.spawn(this.location, Item::class.java)
                itemEntity.itemStack = item
            } else {
                this.inventory.addItem(item)
            }
        }
        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }
        fun Location.pushOut(width: Double) {
            if (abs(this.y-this.y.round(0)) < 0.01) this.y += 0.05
            val asVector = Vector(this.x - this.blockX, 0.0, this.z - this.blockZ)
            if ((this.solid(Vector(1, 0, 0)) || this.solid(Vector(1, 0, -1)) || this.solid(Vector(1, 0, 1)) ||
                this.solid(Vector(1, 1, 0)) || this.solid(Vector(1, 1, -1)) || this.solid(Vector(1, 1, 1))) && asVector.x > 1-(width/2)) asVector.x = 1-(width/2)
            if ((this.solid(Vector(-1, 0, 0)) || this.solid(Vector(-1, 0, -1)) || this.solid(Vector(-1, 0, 1)) ||
                this.solid(Vector(-1, 1, 0)) || this.solid(Vector(-1, 1, -1)) || this.solid(Vector(-1, 1, 1))) && asVector.x < width/2) asVector.x = width/2
            if ((this.solid(Vector(0, 0, 1)) || this.solid(Vector(1, 0, 1)) || this.solid(Vector(-1, 0, 1)) ||
                this.solid(Vector(0, 0, 1)) || this.solid(Vector(1, 0, 1)) || this.solid(Vector(-1, 0, 1))) && asVector.z > 1-(width/2)) asVector.z = 1-(width/2)
            if ((this.solid(Vector(0, 0, -1)) || this.solid(Vector(1, 0, -1)) || this.solid(Vector(-1, 0, -1)) ||
                this.solid(Vector(0, 0, -1)) || this.solid(Vector(1, 0, -1)) || this.solid(Vector(-1, 0, -1))) && asVector.z < width/2) asVector.z = width/2
            if (this.solid(Vector(-1, 0, -1)) && asVector.z < width/2) asVector.z = width/2
            this.x = this.blockX.toDouble()
            this.z = this.blockZ.toDouble()
            this.add(asVector)
        }
        fun Location.solid(direction: Vector): Boolean {
            val newLoc = this.clone().add(direction)
            return !newLoc.block.isPassable
        }
        fun Entity.setAttr(attr: Attribute, value: Double) {
            (this as Attributable).getAttribute(attr)!!.baseValue = value
        }

        fun ByteArray.decodeToDouble(): Double {
            return ByteBuffer.wrap(this).getDouble()
        }
        fun Double.toByteArray(): ByteArray {
            return ByteBuffer.allocate(java.lang.Double.BYTES).putDouble(this).array()
        }
        fun DoubleArray.toByteArray(): ByteArray {
            val newBytes = mutableListOf<Byte>()
            for (i in this) {
                for (byte in i.toByteArray()) {
                    newBytes.add(byte)
                }
            }
            return newBytes.toByteArray()
        }
        fun ByteArray.decodeToDoubleArray(): DoubleArray {
            if (this.size % 8 != 0) return doubleArrayOf()
            val newDoubles = mutableListOf<Double>()
            for (i in 1..this.size/8) {
                newDoubles.add(this.slice((i-1)*8..<i*8).toByteArray().decodeToDouble())
            }
            return newDoubles.toDoubleArray()
        }
        fun Location.serializeAsBytes(): ByteArray {
            val doubleArray = doubleArrayOf(Bukkit.getServer().worlds.indexOf(this.world).toDouble(), this.x, this.y, this.z, this.pitch.toDouble(), this.yaw.toDouble())
            return doubleArray.toByteArray()
        }
        fun deserializeLocationBytes(bytes: ByteArray): Location {
            val doubleArray = bytes.decodeToDoubleArray()
            return Location(Bukkit.getServer().worlds[doubleArray[0].toInt()], doubleArray[1], doubleArray[2], doubleArray[3], doubleArray[4].toFloat(), doubleArray[5].toFloat())
        }

        fun ItemStack?.isItem(item: CustomItem): Boolean {
            if (this?.itemMeta == null) return false
            if (this.type == Material.AIR) return false
            return this.getTag<Int>("id") == item.id
        }
        fun ItemStack.offCooldown(p: Player, order: String = ""): Boolean {
            // if an item does not have a custom then it probably doesnt have a cooldown, check anyways in case epearl
            val custom = this.getCustom() ?: return !p.hasCooldown(this)
            return if (order != "") {
                p.getTag<Double>(custom.getCooldown(order))!! < 0.1
            } else {
                p.getCooldown(Key.key("customitems", this.getCustom()!!.name.lowercase())) < 0.1
            }
        }
        fun ItemStack.setCooldown(p: Player, time: Double, order: String = "") {
            if (order != "") {
                p.setTag(this.getCustom()!!.getCooldown(order), time)
            } else {
                //p.sendMessage((time * 20).toInt().toString())
                p.setCooldown(Key.key("customitems", this.getCustom()!!.name.lowercase()), (time * 20).toInt())
            }
        }
        fun Player.setCooldown(item: CustomItem, time: Double, order: String = "") {
            if (order != "") {
                this.setTag(item.getCooldown(order), time)
            } else {
                //this.sendMessage((time * 20).toInt().toString())
                this.setCooldown(Key.key("customitems", item.name.lowercase()), (time * 20).toInt())
            }
        }
        fun Player.offCooldown(item: CustomItem, order: String = ""): Boolean {
            return if (order != "") {
                this.getTag<Double>(item.getCooldown(order))!! < 0.1
            } else {
                this.getCooldown(Key.key("customitems", item.name.lowercase())) < 0.1
            }
        }

        fun color(array: Array<Int>): TextColor {
            return TextColor.color(array[0], array[1], array[2])
        }
        fun text(text: String): Component {
            return Component.text(text).style(Style.style().decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
        }
        fun text(text: String, rgb: Array<Int>, italics: Boolean = false, bold: Boolean = false, underline: Boolean = false): Component {
            val color = if (rgb.size == 1) TextColor.color(rgb[0]) else TextColor.color(rgb[0], rgb[1], rgb[2])
            return Component.text(text).style(Style.style(color)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.byBoolean(italics))
                .decoration(TextDecoration.BOLD, TextDecoration.State.byBoolean(bold))
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.byBoolean(underline))
            )
        }
        fun toExpLevel(total: Int): Pair<Int, Int> {
            if (total in 0..352) {
                val levels = sqrt((total + 9).toDouble()) - 3
                return Pair(levels.toInt(), (total - toExpAmount(levels.toInt())).toInt())
            } else if (total in 353..1507) {
                val levels = sqrt(2.0/5 * (total - 7839.0/40)) + 81.0/10
                return Pair(levels.toInt(), (total - toExpAmount(levels.toInt())).toInt())
            } else if (total >= 1508) {
                val levels = sqrt(2.0/9 * (total - 54215.0/72)) + 325.0/18
                return Pair(levels.toInt(), (total - toExpAmount(levels.toInt())).toInt())
            }
            return Pair(0, 0)
        }
        fun toExpAmount(level: Int, extra: Int = 0): Int {
            if (level in 0..16) {
                return (level.toDouble().pow(2) + level * 6).toInt() + extra
            } else if (level in 17..31) {
                return (2.5 * level.toDouble().pow(2) - 40.5 * level + 360).toInt() + extra
            } else if (level >= 32) {
                return (4.5 * level.toDouble().pow(2) - 162.5 * level + 2220).toInt() + extra
            }
            return 0
        }

        fun flipPotion(type: PotionEffectType): PotionEffectType {
            return when (type) {
                PotionEffectType.SLOWNESS -> PotionEffectType.SPEED
                PotionEffectType.MINING_FATIGUE -> PotionEffectType.HASTE
                PotionEffectType.NAUSEA -> PotionEffectType.FIRE_RESISTANCE
                PotionEffectType.BLINDNESS -> PotionEffectType.INVISIBILITY
                PotionEffectType.HUNGER -> PotionEffectType.SATURATION
                PotionEffectType.WEAKNESS -> PotionEffectType.STRENGTH
                PotionEffectType.POISON -> PotionEffectType.REGENERATION
                PotionEffectType.WITHER -> PotionEffectType.REGENERATION
                PotionEffectType.LEVITATION -> PotionEffectType.RESISTANCE
                PotionEffectType.SLOW_FALLING -> PotionEffectType.HEALTH_BOOST
                PotionEffectType.DARKNESS -> PotionEffectType.RESISTANCE
                else -> PotionEffectType.UNLUCK
            }
        }
        private fun convertEnch(ench: String): Pair<Enchantment, Int> {
            val lvl = ench.substring(2).toInt()
            when (ench.substring(0,2)) {
                "AA" -> return Pair(Enchantment.AQUA_AFFINITY, lvl)
                "BA" -> return Pair(Enchantment.BANE_OF_ARTHROPODS, lvl)
                "BP" -> return Pair(Enchantment.BLAST_PROTECTION, lvl)
                "BR" -> return Pair(Enchantment.BREACH, lvl)
                "CH" -> return Pair(Enchantment.CHANNELING, lvl)
                "CB" -> return Pair(Enchantment.BINDING_CURSE, lvl)
                "CV" -> return Pair(Enchantment.VANISHING_CURSE, lvl)
                "DN" -> return Pair(Enchantment.DENSITY, lvl)
                "DS" -> return Pair(Enchantment.DEPTH_STRIDER, lvl)
                "EF" -> return Pair(Enchantment.EFFICIENCY, lvl)
                "FF" -> return Pair(Enchantment.FEATHER_FALLING, lvl)
                "FA" -> return Pair(Enchantment.FIRE_ASPECT, lvl)
                "FP" -> return Pair(Enchantment.FIRE_PROTECTION, lvl)
                "FL" -> return Pair(Enchantment.FLAME, lvl)
                "FT" -> return Pair(Enchantment.FORTUNE, lvl)
                "FW" -> return Pair(Enchantment.FROST_WALKER, lvl)
                "IM" -> return Pair(Enchantment.IMPALING, lvl)
                "IN" -> return Pair(Enchantment.INFINITY, lvl)
                "KB" -> return Pair(Enchantment.KNOCKBACK, lvl)
                "LT" -> return Pair(Enchantment.LOOTING, lvl)
                "LY" -> return Pair(Enchantment.LOYALTY, lvl)
                "LS" -> return Pair(Enchantment.LUCK_OF_THE_SEA, lvl)
                "LR" -> return Pair(Enchantment.LURE, lvl)
                "MN" -> return Pair(Enchantment.MENDING, lvl)
                "MS" -> return Pair(Enchantment.MULTISHOT, lvl)
                "PR" -> return Pair(Enchantment.PIERCING, lvl)
                "PW" -> return Pair(Enchantment.POWER, lvl)
                "PP" -> return Pair(Enchantment.PROJECTILE_PROTECTION, lvl)
                "PT" -> return Pair(Enchantment.PROTECTION, lvl)
                "PU" -> return Pair(Enchantment.PUNCH, lvl)
                "QC" -> return Pair(Enchantment.QUICK_CHARGE, lvl)
                "RS" -> return Pair(Enchantment.RESPIRATION, lvl)
                "RP" -> return Pair(Enchantment.RIPTIDE, lvl)
                "SH" -> return Pair(Enchantment.SHARPNESS, lvl)
                "ST" -> return Pair(Enchantment.SILK_TOUCH, lvl)
                "SM" -> return Pair(Enchantment.SMITE, lvl)
                "SP" -> return Pair(Enchantment.SOUL_SPEED, lvl)
                "SW" -> return Pair(Enchantment.SWEEPING_EDGE, lvl)
                "SN" -> return Pair(Enchantment.SWIFT_SNEAK, lvl)
                "TH" -> return Pair(Enchantment.THORNS, lvl)
                "UN" -> return Pair(Enchantment.UNBREAKING, lvl)
                "WB" -> return Pair(Enchantment.WIND_BURST, lvl)
                "DU" -> return Pair(CustomEnchantments.DUPLICATE, lvl)
            }
            return Pair(Enchantment.UNBREAKING, lvl)
        }
        private fun convertPotion(potion: String): PotionEffect {
            val duration = (potion.substring(potion.indexOf(":")+1).toDouble()*20).toInt()
            val amplifier = potion.substring(3,potion.indexOf(":")).toInt() - 1
            when (potion.substring(0,3)) {
                "ABS" -> return PotionEffect(PotionEffectType.ABSORPTION, duration, amplifier)
                "BAL" -> return PotionEffect(PotionEffectType.UNLUCK, duration, amplifier)
                "BAO" -> return PotionEffect(PotionEffectType.BAD_OMEN, duration, amplifier)
                "BLI" -> return PotionEffect(PotionEffectType.BLINDNESS, duration, amplifier)
                "COP" -> return PotionEffect(PotionEffectType.CONDUIT_POWER, duration, amplifier)
                "DAR" -> return PotionEffect(PotionEffectType.DARKNESS, duration, amplifier)
                "DOG" -> return PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, amplifier)
                "FIR" -> return PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, amplifier)
                "GLO" -> return PotionEffect(PotionEffectType.GLOWING, duration, amplifier)
                "HAS" -> return PotionEffect(PotionEffectType.HASTE, duration, amplifier)
                "HEB" -> return PotionEffect(PotionEffectType.HEALTH_BOOST, duration, amplifier)
                "HEV" -> return PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, duration, amplifier)
                "HUN" -> return PotionEffect(PotionEffectType.HUNGER, duration, amplifier)
                "INF" -> return PotionEffect(PotionEffectType.INFESTED, duration, amplifier)
                "IND" -> return PotionEffect(PotionEffectType.INSTANT_DAMAGE, duration, amplifier)
                "INH" -> return PotionEffect(PotionEffectType.INSTANT_HEALTH, duration, amplifier)
                "INV" -> return PotionEffect(PotionEffectType.INVISIBILITY, duration, amplifier)
                "JUB" -> return PotionEffect(PotionEffectType.JUMP_BOOST, duration, amplifier)
                "LEV" -> return PotionEffect(PotionEffectType.LEVITATION, duration, amplifier)
                "LUC" -> return PotionEffect(PotionEffectType.LUCK, duration, amplifier)
                "MIF" -> return PotionEffect(PotionEffectType.MINING_FATIGUE, duration, amplifier)
                "NAU" -> return PotionEffect(PotionEffectType.NAUSEA, duration, amplifier)
                "NIV" -> return PotionEffect(PotionEffectType.NIGHT_VISION, duration, amplifier)
                "OOZ" -> return PotionEffect(PotionEffectType.OOZING, duration, amplifier)
                "POI" -> return PotionEffect(PotionEffectType.POISON, duration, amplifier)
                "RAO" -> return PotionEffect(PotionEffectType.RAID_OMEN, duration, amplifier)
                "REG" -> return PotionEffect(PotionEffectType.REGENERATION, duration, amplifier)
                "RES" -> return PotionEffect(PotionEffectType.RESISTANCE, duration, amplifier)
                "SAT" -> return PotionEffect(PotionEffectType.SATURATION, duration, amplifier)
                "SLF" -> return PotionEffect(PotionEffectType.SLOW_FALLING, duration, amplifier)
                "SLO" -> return PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier)
                "SPE" -> return PotionEffect(PotionEffectType.SPEED, duration, amplifier)
                "STR" -> return PotionEffect(PotionEffectType.STRENGTH, duration, amplifier)
                "TRO" -> return PotionEffect(PotionEffectType.TRIAL_OMEN, duration, amplifier)
                "WAB" -> return PotionEffect(PotionEffectType.WATER_BREATHING, duration, amplifier)
                "WEA" -> return PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier)
                "WEV" -> return PotionEffect(PotionEffectType.WEAVING, duration, amplifier)
                "WIC" -> return PotionEffect(PotionEffectType.WIND_CHARGED, duration, amplifier)
                "WIT" -> return PotionEffect(PotionEffectType.WITHER, duration, amplifier)
                else -> return PotionEffect(PotionEffectType.UNLUCK, duration, amplifier)
            }
        }
        fun convertVillagerLevel(level: Int): String {
            return when (level) {
                1 -> "Novice"
                2 -> "Apprentice"
                3 -> "Journeyman"
                4 -> "Expert"
                5 -> "Master"
                else -> "None"
            }
        }
        fun getMaterials(item: Material): Array<ItemStack> {
            return when (item) {
                Material.STONE_BUTTON -> arrayOf(ItemStack(Material.STONE, 1))
                Material.OAK_BUTTON -> arrayOf(ItemStack(Material.OAK_PLANKS, 1))
                Material.LEVER -> arrayOf(ItemStack(Material.COBBLESTONE, 1), ItemStack(Material.STICK, 1))
                Material.OAK_PRESSURE_PLATE -> arrayOf(ItemStack(Material.OAK_PLANKS, 2))
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE -> arrayOf(ItemStack(Material.IRON_INGOT, 2))
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE -> arrayOf(ItemStack(Material.GOLD_INGOT, 2))
                Material.STONE_PRESSURE_PLATE -> arrayOf(ItemStack(Material.STONE, 2))
                Material.RAIL -> arrayOf(ItemStack(Material.IRON_INGOT, 1), ItemStack(Material.STICK, 1))
                Material.POWERED_RAIL -> arrayOf(ItemStack(Material.REDSTONE, 1), ItemStack(Material.GOLD_INGOT, 1), ItemStack(Material.STICK, 1))
                Material.DETECTOR_RAIL -> arrayOf(ItemStack(Material.IRON_INGOT, 1), ItemStack(Material.STONE, 1), ItemStack(Material.REDSTONE, 1))
                Material.ACTIVATOR_RAIL -> arrayOf(ItemStack(Material.IRON_INGOT, 1), ItemStack(Material.STICK, 1))
                Material.MINECART -> arrayOf(ItemStack(Material.IRON_INGOT, 5))
                Material.HOPPER_MINECART -> arrayOf(ItemStack(Material.IRON_INGOT, 10), ItemStack(Material.OAK_PLANKS, 8))
                Material.CHEST_MINECART -> arrayOf(ItemStack(Material.IRON_INGOT, 5), ItemStack(Material.OAK_PLANKS, 8))
                Material.FURNACE_MINECART -> arrayOf(ItemStack(Material.IRON_INGOT, 5), ItemStack(Material.COBBLESTONE, 8))
                Material.TNT_MINECART -> arrayOf(ItemStack(Material.TNT, 1), ItemStack(Material.IRON_INGOT, 5))
                Material.REDSTONE -> arrayOf(ItemStack(Material.REDSTONE, 1))
                Material.REDSTONE_BLOCK -> arrayOf(ItemStack(Material.REDSTONE, 9))
                Material.REPEATER -> arrayOf(ItemStack(Material.STONE, 3), ItemStack(Material.STICK, 2), ItemStack(Material.REDSTONE, 3))
                Material.COMPARATOR -> arrayOf(ItemStack(Material.STONE, 3), ItemStack(Material.STICK, 3), ItemStack(Material.REDSTONE, 3), ItemStack(Material.QUARTZ, 1))
                Material.REDSTONE_TORCH -> arrayOf(ItemStack(Material.STICK, 1), ItemStack(Material.REDSTONE, 1))
                Material.OBSERVER -> arrayOf(ItemStack(Material.COBBLESTONE, 6), ItemStack(Material.REDSTONE, 2), ItemStack(Material.QUARTZ, 1))
                Material.HOPPER -> arrayOf(ItemStack(Material.IRON_INGOT, 5), ItemStack(Material.OAK_PLANKS, 8))
                Material.BARREL -> arrayOf(ItemStack(Material.OAK_PLANKS, 7))
                Material.CHEST -> arrayOf(ItemStack(Material.OAK_PLANKS, 8))
                Material.CRAFTER -> arrayOf(ItemStack(Material.COBBLESTONE, 7), ItemStack(Material.REDSTONE, 3), ItemStack(Material.IRON_INGOT, 5), ItemStack(Material.OAK_PLANKS, 4))
                Material.DISPENSER -> arrayOf(ItemStack(Material.COBBLESTONE, 7), ItemStack(Material.REDSTONE, 1), ItemStack(Material.STICK, 3), ItemStack(Material.STRING, 3))
                Material.DROPPER -> arrayOf(ItemStack(Material.COBBLESTONE, 7), ItemStack(Material.REDSTONE, 1))
                Material.NOTE_BLOCK -> arrayOf(ItemStack(Material.OAK_PLANKS, 8), ItemStack(Material.REDSTONE, 1))
                Material.PISTON -> arrayOf(ItemStack(Material.COBBLESTONE, 4), ItemStack(Material.OAK_PLANKS, 3), ItemStack(Material.REDSTONE, 1), ItemStack(Material.IRON_INGOT, 1))
                Material.STICKY_PISTON -> arrayOf(ItemStack(Material.COBBLESTONE, 4), ItemStack(Material.OAK_PLANKS, 3), ItemStack(Material.REDSTONE, 1), ItemStack(Material.IRON_INGOT, 1), ItemStack(Material.SLIME_BALL, 1))
                Material.SLIME_BLOCK -> arrayOf(ItemStack(Material.SLIME_BALL, 9))
                else -> arrayOf()
            }
        }

        fun ItemStack.decrementTag(tag: String) {
            if (this.getTag<Int>(tag) != 0) {
                this.setTag(tag, this.getTag<Int>(tag)?.minus(1) ?: 0)
            }
        }
        fun ItemStack.incrementTag(tag: String) {
            this.setTag(tag, this.getTag<Int>(tag)?.plus(1) ?: 0)
        }
        inline fun <reified T> ItemStack.setTag(tag: String, value: T): ItemStack {
            val newMeta = this.itemMeta
            when (T::class) {
                Int::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER, value as Int)
                String::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.STRING, value as String)
                Boolean::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BOOLEAN, value as Boolean)
                Float::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.FLOAT, value as Float)
                Double::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.DOUBLE, value as Double)
                ByteArray::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BYTE_ARRAY, value as ByteArray)
                IntArray::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER_ARRAY, value as IntArray)
                ItemStack::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.ITEMSTACK, value as ItemStack)
                Location::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.LOCATION, value as Location)
                UUID::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.UUID, value as UUID)
            }
            this.itemMeta = newMeta
            return this
        }
        inline fun <reified T> ItemStack.setListTag(tag: String, value: MutableList<T>): ItemStack {
            val newMeta = this.itemMeta
            when (T::class) {
                ItemStack::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.ITEMSTACK), value as MutableList<ItemStack>)
                Location::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.LOCATION), value as MutableList<Location>)
                Int::class -> newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentDataType.INTEGER), value as MutableList<Int>)
            }
            this.itemMeta = newMeta
            return this
        }
        inline fun <reified T> ItemStack.getTag(tag: String): T? {
            if (this.itemMeta == null) return null
            when (T::class) {
                Int::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER) as T?
                String::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.STRING) as T?
                Boolean::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BOOLEAN) as T?
                Float::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.FLOAT) as T?
                Double::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.DOUBLE) as T?
                ByteArray::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BYTE_ARRAY) as T?
                IntArray::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER_ARRAY) as T?
                ItemStack::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.ITEMSTACK) as T?
                Location::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.LOCATION) as T?
                UUID::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.UUID) as T?
            }
            return null
        }
        inline fun <reified T> ItemStack.getListTag(tag: String): MutableList<T>? {
            if (this.itemMeta == null) return null
            when (T::class) {
                ItemStack::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.ITEMSTACK)) as MutableList<T>?
                Location::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.LOCATION)) as MutableList<T>?
                Int::class -> return this.itemMeta.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentDataType.INTEGER)) as MutableList<T>?
            }
            return null
        }
        fun ItemStack.removeTag(tag: String) {
            if (this.itemMeta == null) return
            this.itemMeta.persistentDataContainer.remove(NamespacedKey(CustomItems.plugin, tag))
        }
        fun Entity.decrementTag(tag: String) {
            if ((this.getTag<Int>(tag) ?: 1) > 0) {
                this.setTag(tag, this.getTag<Int>(tag)?.minus(1) ?: 0)
            }
        }
        fun Entity.incrementTag(tag: String) {
            this.setTag(tag, this.getTag<Int>(tag)?.plus(1) ?: 0)
        }
        inline fun <reified T> Entity.setTag(tag: String, value: T): Entity {
            when (T::class) {
                Int::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER, value as Int)
                String::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.STRING, value as String)
                Boolean::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BOOLEAN, value as Boolean)
                Float::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.FLOAT, value as Float)
                Long::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LONG, value as Long)
                Double::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.DOUBLE, value as Double)
                ByteArray::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BYTE_ARRAY, value as ByteArray)
                IntArray::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER_ARRAY, value as IntArray)
                ItemStack::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.ITEMSTACK, value as ItemStack)
                Location::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.LOCATION, value as Location)
                UUID::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.UUID, value as UUID)
            }
            return this
        }
        inline fun <reified T> Entity.setListTag(tag: String, value: MutableList<T>): Entity {
            when (T::class) {
                Location::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.LOCATION), value as MutableList<Location>)
                ItemStack::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.ITEMSTACK), value as MutableList<ItemStack>)
                String::class -> this.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), value as MutableList<String>)
            }
            return this
        }
        inline fun <reified T> Entity.getTag(tag: String): T? {
            when (T::class) {
                Int::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER) as T?
                String::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.STRING) as T?
                Boolean::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BOOLEAN) as T?
                Float::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.FLOAT) as T?
                Long::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LONG) as T?
                Double::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.DOUBLE) as T?
                ByteArray::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.BYTE_ARRAY) as T?
                IntArray::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.INTEGER_ARRAY) as T?
                ItemStack::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.ITEMSTACK) as T?
                Location::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.LOCATION) as T?
                UUID::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentCustomType.UUID) as T?
            }
            return 0 as T?
        }
        inline fun <reified T> Entity.getListTag(tag: String): MutableList<T>? {
            when (T::class) {
                ItemStack::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.ITEMSTACK)) as MutableList<T>?
                Location::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentCustomType.LOCATION)) as MutableList<T>?
                String::class -> return this.persistentDataContainer.get(NamespacedKey(CustomItems.plugin, tag), PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)) as MutableList<T>?
            }
            return null
        }
        fun Entity.removeTag(tag: String) {
            this.persistentDataContainer.remove(NamespacedKey(CustomItems.plugin, tag))
        }

        fun ItemStack.loreList(loreList: MutableList<Component>): ItemStack {
            this.lore(loreList)
            return this
        }
        private fun Attribute.readName(): String {
            val value = this.key.value()
            val words = value.split("_").toMutableList()
            var totalAttr = ""
            words.forEach {
                totalAttr += it.capitalize() + " "
            }
            return totalAttr
        }
        private fun Double.trimToString(): String {
            val initial = this.toString()
            var endIndex = (initial.length - 1).coerceAtMost(6)
            while (initial[endIndex] == "0".first() || initial[endIndex] == ".".first()) {
                if (endIndex == 0) break
                endIndex--
                if (initial[endIndex] == ".".first()) break
            }
            return initial.substring(0, endIndex + 1)
        }
        fun ItemStack.cleanAttributeLore(): ItemStack {
            val description = this.lore()?.toMutableList() ?: mutableListOf()
            if (this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.size !in arrayOf(0, null) && !this.hasData(DataComponentTypes.EQUIPPABLE)) {
                val modifiers = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                if (!modifiers!!.modifiers().any { it.modifier().slotGroup != EquipmentSlotGroup.MAINHAND }) {
                    val newLines = mutableListOf<Component>()
                    //newLines.add(Utils.text(""))
                    newLines.add(text("When in Main Hand: ", Utils.GRAY))
                    for (modifier in modifiers.modifiers()) {
                        val attribute = modifier.attribute()
                        val amount = modifier.modifier().amount.round(3)
                        val trimmedAmount = if (attribute == Attribute.ATTACK_SPEED) (amount + 4).trimToString() else amount.trimToString()
                        val modification = modifier.modifier().operation
                        val sign = if (modifier.modifier().amount > 0 || attribute == Attribute.ATTACK_SPEED) "+" else ""
                        val attrString = "$sign$trimmedAmount${if (modification != AttributeModifier.Operation.ADD_NUMBER) "%" else ""} ${attribute.readName()}"
                        newLines.add(text(attrString, Utils.BLUE))
                    }
                    description.add(text(""))
                    description.addAll(newLines)
                    this.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS).build())
                    this.lore(description)
                } else {
                    this.lore(description)
                }
            } else {
                this.lore(description)
            }

            return this
        }
        fun ItemStack.setCustomData(custom: CustomItem): ItemStack {
            this.setTag("id", custom.id)
            this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(custom.id.toString()))
            this.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.05F).cooldownGroup(Key.key("customitems", custom.name.lowercase())))
            return this
        }
        fun ItemStack.smeltIf(vararg tags: Tag<Material>): ItemStack {
            for (tag in tags) {
                if (tag.isTagged(this.type)) {
                    this.smelt()
                }
            }
            return this
        }
        fun ItemStack.smelt(): ItemStack {
            for (recipe in Bukkit.recipeIterator()) {
                if (recipe is FurnaceRecipe && recipe.inputChoice.test(this)) {
                    this.type = recipe.result.type
                    break
                }
            }
            return this
        }
        fun ItemStack.lock(): ItemStack{
            val newMeta = this.itemMeta
            newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "locked"), PersistentDataType.BOOLEAN, true)
            val newItem = ItemStack(this)
            newItem.itemMeta = newMeta
            return newItem
        }
        fun ItemStack.getCustom(): CustomItem? {
            val id = this.getTag<Int>("id") ?: return null
            return CustomItem.entries[id]
        }
        fun ItemStack.customName(component: Component): ItemStack {
            this.name(
                component.style(Style.style(component.color(), TextDecoration.BOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            )
            return this
        }
        fun ItemStack.name(component: Component): ItemStack {
            this.setData(DataComponentTypes.CUSTOM_NAME, component)
            return this
        }
        fun ItemStack.name(string: String): ItemStack {
            this.setData(DataComponentTypes.CUSTOM_NAME, text(string))
            return this
        }
        fun ItemStack.customModel(id: Int): ItemStack {
            val newMeta = this.itemMeta
            newMeta.setCustomModelData(id)
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.maxDura(max: Int): ItemStack {
            this.setData(DataComponentTypes.MAX_DAMAGE, max)
            return this
        }
        fun ItemStack.unb(shownInTooltip: Boolean = true): ItemStack {
            this.setData(DataComponentTypes.UNBREAKABLE)
            return this
        }
        fun ItemStack.maxStack(cap: Int): ItemStack {
            this.setData(DataComponentTypes.MAX_STACK_SIZE, cap)
            return this
        }
        fun ItemStack.ench(vararg enchs: String): ItemStack {
            for (ench in enchs) {
                val enchantment = convertEnch(ench)
                this.addUnsafeEnchantment(enchantment.first, enchantment.second)
            }
            return this
        }
        fun ItemStack.food(food: Int, sat: Float, canAlwaysEat: Boolean = false): ItemStack {
            val foodMeta = FoodProperties.food().nutrition(food).saturation(sat).canAlwaysEat(canAlwaysEat)
            this.setData(DataComponentTypes.FOOD, foodMeta)
            return this
        }
        fun ItemStack.consumable(eff: Array<ConsumeEffect> = arrayOf(), eatSeconds: Float = 1.61F): ItemStack {
            val consumable = Consumable.consumable().consumeSeconds(eatSeconds).addEffects(eff.toMutableList())
            this.setData(DataComponentTypes.CONSUMABLE, consumable)
            return this
        }
        fun ItemStack.attr(vararg attrs: String): ItemStack {
            val newMeta = this.itemMeta
            for (attr in attrs) {
                val attrType: Attribute = when (attr.substring(0, 3)) {
                    "ARM" -> Attribute.ARMOR
                    "ART" -> Attribute.ARMOR_TOUGHNESS
                    "ATD" -> Attribute.ATTACK_DAMAGE
                    "ATS" -> Attribute.ATTACK_SPEED
                    "KNR" -> Attribute.KNOCKBACK_RESISTANCE
                    "LUC" -> Attribute.LUCK
                    "MAH" -> Attribute.MAX_HEALTH
                    "MOS" -> Attribute.MOVEMENT_SPEED
                    "SCA" -> Attribute.SCALE
                    "STH" -> Attribute.STEP_HEIGHT
                    "JUS" -> Attribute.JUMP_STRENGTH
                    "BLI" -> Attribute.BLOCK_INTERACTION_RANGE
                    "ENI" -> Attribute.ENTITY_INTERACTION_RANGE
                    "BLB" -> Attribute.BLOCK_BREAK_SPEED
                    "GRA" -> Attribute.GRAVITY
                    "SAF" -> Attribute.SAFE_FALL_DISTANCE
                    "FAD" -> Attribute.FALL_DAMAGE_MULTIPLIER
                    "BUT" -> Attribute.BURNING_TIME
                    "EXK" -> Attribute.EXPLOSION_KNOCKBACK_RESISTANCE
                    "MIE" -> Attribute.MINING_EFFICIENCY
                    "MOE" -> Attribute.MOVEMENT_EFFICIENCY
                    "OXB" -> Attribute.OXYGEN_BONUS
                    "SNS" -> Attribute.SNEAKING_SPEED
                    "SUM" -> Attribute.SUBMERGED_MINING_SPEED
                    "SWD" -> Attribute.SWEEPING_DAMAGE_RATIO
                    "WAM" -> Attribute.WATER_MOVEMENT_EFFICIENCY
                    else -> Attribute.LUCK
                }
                val slot: EquipmentSlotGroup = when (attr.substring(attr.length-2, attr.length)) {
                    "AR" -> EquipmentSlotGroup.ARMOR
                    "CH" -> EquipmentSlotGroup.CHEST
                    "AN" -> EquipmentSlotGroup.ANY
                    "BO" -> EquipmentSlotGroup.BODY
                    "FE" -> EquipmentSlotGroup.FEET
                    "HA" -> EquipmentSlotGroup.HAND
                    "HE" -> EquipmentSlotGroup.HEAD
                    "LE" -> EquipmentSlotGroup.LEGS
                    "MA" -> EquipmentSlotGroup.MAINHAND
                    "OF" -> EquipmentSlotGroup.OFFHAND
                    else -> EquipmentSlotGroup.HAND
                }
                if (attr[attr.lastIndex-2].toString() == "%") {

                    newMeta.addAttributeModifier(attrType,
                        AttributeModifier(NamespacedKey(CustomItems.plugin, UUID.randomUUID().toString()),
                            attr.substring(3, attr.lastIndex-2).toDouble(),
                            AttributeModifier.Operation.ADD_SCALAR,
                            slot
                        ))
                } else if (attr[attr.lastIndex-2].isDigit()) {
                    newMeta.addAttributeModifier(attrType,
                        AttributeModifier(NamespacedKey(CustomItems.plugin, UUID.randomUUID().toString()),
                            attr.substring(3, attr.lastIndex-1).toDouble(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            slot
                        ))
                }
            }
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.removeAttr(): ItemStack {
            val newMeta = this.itemMeta
            newMeta.removeAttributeModifier(EquipmentSlot.CHEST)
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.tool(baseSpeed: Float, vararg mats: Pair<TagKey<BlockType>, Float>): ItemStack {
            val toolMeta = Tool.tool().defaultMiningSpeed(baseSpeed)
            for (mat in mats) {
                toolMeta.addRule(Tool.rule(
                    RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK).getTag(mat.first),
                    mat.second, TriState.TRUE))
            }
            this.setData(DataComponentTypes.TOOL, toolMeta)
            return this
        }
        fun ItemStack.tagTool(tag: Tag<Material>, speed: Float): ItemStack {
            val newMeta = this.itemMeta
            val toolMeta = newMeta.tool
            toolMeta.addRule(tag, speed, true)
            newMeta.setTool(toolMeta)
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.duraBroken(dura: Int): ItemStack {
            val newMeta = this.itemMeta
            val toolMeta = newMeta.tool
            toolMeta.damagePerBlock = dura
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.resist(type: TagKey<DamageType>): ItemStack {
            this.setData(DataComponentTypes.DAMAGE_RESISTANT, DamageResistant.damageResistant(type))
            return this
        }
        fun ItemStack.lore(vararg lines: Component): ItemStack {
            this.lore(lines.asList())
            return this
        }
        fun ItemStack.loreBlock(vararg blocks: Component): ItemStack {
            val totalLines = mutableListOf<Component>()
            for (block in blocks){
                val words = (block as TextComponent).content().split(" ")
                val lines = mutableListOf<Component>()
                var chars = 0
                var currentLine = ""
                for (word in words) {
                    if (chars > 50) {
                        lines.add(Component.text(currentLine).style(block.style()))
                        chars = 0
                        currentLine = ""
                    }
                    chars += word.length + 1
                    currentLine += "$word "
                }
                lines.add(Component.text(currentLine).style(block.style()))
                totalLines.addAll(lines)
            }
            this.lore(totalLines)
            return this
        }
        fun loreBlockToList(vararg blocks: Component): MutableList<Component> {
            val totalLines = mutableListOf<Component>()
            for (block in blocks){
                val words = (block as TextComponent).content().split(" ")
                val lines = mutableListOf<Component>()
                var chars = 0
                var currentLine = ""
                for (word in words) {
                    if (chars > 50) {
                        lines.add(Component.text(currentLine).style(block.style()))
                        chars = 0
                        currentLine = ""
                    }
                    chars += word.length + 1
                    currentLine += "$word "
                }
                lines.add(Component.text(currentLine).style(block.style()))
                totalLines.addAll(lines)
            }
            return totalLines
        }
        fun ItemStack.trim(trim: ArmorTrim): ItemStack {
            val newMeta = this.itemMeta as ArmorMeta
            newMeta.trim = trim
            this.itemMeta = newMeta
            return this
        }
        fun ItemStack.storeEnch(vararg enchs: String): ItemStack {
            val enchMeta = this.itemMeta as EnchantmentStorageMeta
            for (ench in enchs) {
                val enchantment = convertEnch(ench)
                enchMeta.addStoredEnchant(enchantment.first, enchantment.second, true)
            }
            this.itemMeta = enchMeta
            return this
        }
        fun ItemStack.firework(power: Int, vararg effects: FireworkEffect): ItemStack {
            val fireworkMeta = this.itemMeta as FireworkMeta
            fireworkMeta.power = power
            for (effect in effects) {
                fireworkMeta.addEffect(effect)
            }
            this.itemMeta = fireworkMeta
            return this
        }
        fun ItemStack.fireworkBooster(flightDuration: Int): ItemStack {
            this.setData(DataComponentTypes.FIREWORKS, Fireworks.fireworks(mutableListOf(), flightDuration))
            return this
        }
        fun ItemStack.omimous(level: Int): ItemStack {
            val ominousBottleMeta = this.itemMeta as OminousBottleMeta
            ominousBottleMeta.amplifier = level
            this.itemMeta = ominousBottleMeta
            return this
        }
        fun ItemStack.basePotion(potionType: PotionType): ItemStack {
            val potionMeta = this.itemMeta as PotionMeta
            potionMeta.basePotionType = potionType
            this.itemMeta = potionMeta
            return this
        }
        fun ItemStack.potion(color: Color, vararg effects: String): ItemStack {
            val potionMeta = this.itemMeta as PotionMeta
            potionMeta.color = color
            for (effect in effects) {
                potionMeta.addCustomEffect(convertPotion(effect), false)
            }
            this.itemMeta = potionMeta
            return this
        }
        fun ItemStack.potionColor(color: Color): ItemStack {
            val potionMeta = this.itemMeta as PotionMeta
            potionMeta.color = color
            this.itemMeta = potionMeta
            return this
        }
        fun ItemStack.horn(horn: MusicInstrument): ItemStack {
            val goatMeta = this.itemMeta as MusicInstrumentMeta
            goatMeta.instrument = horn
            this.itemMeta = goatMeta
            return this
        }
        fun ItemStack.returnAmount(amt: Int): ItemStack {
            this.amount = amt
            return this
        }
        fun ItemStack.noNoiseEquippable(slot: EquipmentSlot): ItemStack {
            val equippable = Equippable.equippable(slot).equipSound(Key.key("intentionally_empty"))
            this.setData(DataComponentTypes.EQUIPPABLE, equippable)
            return this
        }
        fun blockData(material: Material, inv: Array<ItemStack?>): BlockData {
            val blockData = material.createBlockData()
            (blockData as Chest).inventory.contents = inv
            return blockData
        }
        fun ItemStack.addElytraComponent(): ItemStack {
            this.setData(DataComponentTypes.GLIDER)
            return this
        }
        /*fun ItemStack.blockData(blockData: BlockData): ItemStack {
            val blockDataMeta = this.itemMeta as BlockDataMeta
            blockDataMeta.setBlockData(blockData)
            this.itemMeta = blockDataMeta
            return this
        }*/
        /*fun ItemStack.blockState(blockState: BlockState): ItemStack {
            val blockStateMeta = this.itemMeta as BlockStateMeta
            blockStateMeta.blockState = blockState
            this.itemMeta = blockStateMeta
            return this
        }*/

        fun ItemStack.useOriginal(): ItemStack {
            this.setTag("checkoriginal", true)
            return this
        }
        fun ItemStack.useStoredEnch(): ItemStack {
            this.setTag("checkstoredenchant", true)
            return this
        }
        fun ItemStack.useEnch(): ItemStack {
            this.setTag("checkenchant", true)
            return this
        }
        fun ItemStack.useTrim(): ItemStack {
            this.setTag("checktrim", true)
            return this
        }
        fun ItemStack.useOminous(): ItemStack {
            this.setTag("checkominous", true)
            return this
        }
        fun ItemStack.usePotion(): ItemStack {
            this.setTag("checkpotion", true)
            return this
        }
        fun ItemStack.useHorn(): ItemStack {
            this.setTag("checkhorn", true)
            return this
        }
        fun ItemStack.crossbowProj(arrow: ItemStack, count: Int = 1): ItemStack {
            val crossbowMeta = this.itemMeta as CrossbowMeta
            for (i in 1..count) crossbowMeta.addChargedProjectile(arrow)
            this.itemMeta = crossbowMeta
            return this
        }
        fun ItemStack.clearCrossbowProj(): ItemStack {
            val crossbowMeta = this.itemMeta as CrossbowMeta
            crossbowMeta.setChargedProjectiles(null)
            this.itemMeta = crossbowMeta
            return this
        }
        fun ItemStack.reduceDura(amount: Int): ItemStack {
            val newMeta = this.itemMeta as Damageable
            newMeta.damage += amount
            this.itemMeta = newMeta
            return this
        }
    }
}