package me.newburyminer.customItems.commands

import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import me.newburyminer.customItems.items.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.concurrent.CompletableFuture


class CustomItemArgument: CustomArgumentType.Converted<CustomItem, String> {

    @Throws(CommandSyntaxException::class)
    override fun convert(nativeType: String): CustomItem {
        try {
            return CustomItem.valueOf(nativeType.uppercase())
        } catch (ignored: IllegalArgumentException) {
            val message: Message = MessageComponentSerializer.message()
                .serialize(Component.text("Invalid item!", NamedTextColor.RED))

            throw CommandSyntaxException(SimpleCommandExceptionType(message), message)
        }
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }

    override fun <S : Any> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val input = builder.remaining.lowercase().replace("_", " ")
        for (customItem in CustomItem.entries) {
            val formatted = customItem.name.lowercase().replace("_", " ")
            if (formatted.indexOf(input) != -1) builder.suggest(customItem.name)
        }
        return builder.buildFuture()
    }
}