package me.newburyminer.customItems.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.*
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.TagKey
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.BLUE
import me.newburyminer.customItems.Utils.Companion.GRAY
import me.newburyminer.customItems.Utils.Companion.consumable
import me.newburyminer.customItems.Utils.Companion.food
import me.newburyminer.customItems.Utils.Companion.noNoiseEquippable
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.util.TriState
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.BlockType
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import java.util.*

class CustomItemBuilder(material: Material, custom: CustomItem) {

    private val item: ItemStack = ItemStack(material)

    init {
        // Set the item to actually be detectable as a custom, set texture data, set use cooldown so it can be applied later
        item.setTag("id", custom.id)
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(custom.name.toString().lowercase()))
        item.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.05F).cooldownGroup(Key.key("customitems", custom.name.lowercase())))
    }

    fun setName(component: Component): CustomItemBuilder {
        val styledComponent = component.style(Style.style(component.color(), TextDecoration.BOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
        item.setData(DataComponentTypes.CUSTOM_NAME, styledComponent)
        return this
    }

    fun setLore(lore: MutableList<Component>?): CustomItemBuilder {
        item.lore(lore ?: mutableListOf())
        return this
    }

    fun setAttributes(vararg modifiers: SimpleModifier): CustomItemBuilder {
        val attributeData = ItemAttributeModifiers.itemAttributes()
        for (modifier in modifiers) {
            attributeData.addModifier(
                modifier.attribute,
                AttributeModifier(
                    NamespacedKey(CustomItems.plugin, UUID.randomUUID().toString()),
                    modifier.amount,
                    modifier.operation,
                    modifier.slot
                )
            )
        }
        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeData)
        return this
    }

    fun setTag(key: String, value: Any): CustomItemBuilder {
        item.setTag(key, value)
        return this
    }

    fun setArmorSet(armorSet: ArmorSet): CustomItemBuilder {
        item.setTag("armorset", armorSet.name)
        return this
    }

    fun setMaxDurability(durability: Int): CustomItemBuilder {
        item.setData(DataComponentTypes.MAX_DAMAGE, durability)
        return this
    }

    fun setUnbreakable(): CustomItemBuilder {
        item.setData(DataComponentTypes.UNBREAKABLE)
        return this
    }

    fun enchant(vararg enchants: Pair<Enchantment, Int>): CustomItemBuilder {
        enchants.forEach {
            item.addUnsafeEnchantment(it.first, it.second)
        }
        return this
    }

    fun tool(duraBroken: Int, baseSpeed: Float, vararg mats: Pair<TagKey<BlockType>, Float>): CustomItemBuilder {
        val toolMeta = Tool.tool().defaultMiningSpeed(baseSpeed)
        for (mat in mats) {
            toolMeta.addRule(
                Tool.rule(
                RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK).getTag(mat.first),
                mat.second, TriState.TRUE))
        }
        toolMeta.damagePerBlock(duraBroken)
        item.setData(DataComponentTypes.TOOL, toolMeta.build())
        return this
    }

    fun food(food: Int, sat: Float, canAlwaysEat: Boolean = false): CustomItemBuilder {
        val foodMeta = FoodProperties.food().nutrition(food).saturation(sat).canAlwaysEat(canAlwaysEat)
        item.setData(DataComponentTypes.FOOD, foodMeta.build())
        return this
    }

    fun consumable(eff: Array<ConsumeEffect> = arrayOf(), eatSeconds: Float = 1.61F): CustomItemBuilder {
        val consumable = Consumable.consumable().consumeSeconds(eatSeconds).addEffects(eff.toMutableList())
        item.setData(DataComponentTypes.CONSUMABLE, consumable.build())
        return this
    }

    fun noNoiseEquippable(slot: EquipmentSlot): CustomItemBuilder {
        val equippable = Equippable.equippable(slot).equipSound(Key.key("intentionally_empty"))
        item.setData(DataComponentTypes.EQUIPPABLE, equippable)
        return this
    }

    fun build(): ItemStack {
        val description = item.lore()?.toMutableList() ?: mutableListOf()
        // If it has modifiers, and it is not equippable (equippable attributes work fine, just attack speed that doesnt)
        if (item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.size !in arrayOf(0, null) && !item.hasData(DataComponentTypes.EQUIPPABLE)) {
            val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)

            // Only change if they are all mainhand modifiers (ats), others work fine
            if (!modifiers!!.modifiers().any { it.modifier().slotGroup != EquipmentSlotGroup.MAINHAND }) {
                val newLines = mutableListOf<Component>()

                // Initial line, again only for attack speed items
                newLines.add(text("When in Main Hand: ", GRAY))
                for (modifier in modifiers.modifiers()) {

                    val attribute = modifier.attribute()
                    val amount = modifier.modifier().amount
                    val roundedAmount =
                        if (attribute == Attribute.ATTACK_SPEED) (4 + amount).round(3)
                        else amount.round(3)
                    val decimals = roundedAmount.getDecimalPlaces()

                    val trimmedAmount = "%.${decimals}f".format(roundedAmount)
                    //val trimmedAmount = if (attribute == Attribute.ATTACK_SPEED) (amount + 4).trimToString() else amount.trimToString()
                    val modification = modifier.modifier().operation
                    val sign = if (modifier.modifier().amount > 0 || attribute == Attribute.ATTACK_SPEED) "+" else ""
                    val attrString = "$sign$trimmedAmount${if (modification != AttributeModifier.Operation.ADD_NUMBER) "%" else ""} ${attribute.readName()}"
                    newLines.add(text(attrString, BLUE))

                }

                description.add(text(""))
                description.addAll(newLines)
                item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS).build())
                item.lore(description)
            } else {
                item.lore(description)
            }
        } else {
            item.lore(description)
        }

        return item.clone()
    }
    private fun Double.getDecimalPlaces(): Int {
        if (this % 1.0 < 0.001) return 0
        val string = this.toString()
        val decimalIndex = string.indexOf(".") + 1
        return string.length - decimalIndex
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

}