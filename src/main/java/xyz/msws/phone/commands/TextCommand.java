package xyz.msws.phone.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import xyz.msws.phone.NumberSanitizer;

import java.util.List;

public class TextCommand implements Command {
    @Override
    public CommandData getCommandData() {
        return Commands.slash("text", "Starts a text conversation")
                .addOption(OptionType.STRING, "number", "The number to text", true)
                .addOption(OptionType.STRING, "alias", "The name to associate with the number")
                .setGuildOnly(true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            event.getInteraction().reply("This command can only be used in a server.").queue();
            return;
        }
        event.getInteraction().deferReply().queue();
        final String number = NumberSanitizer.formatToE164(event.getOption("number").getAsString());
        Guild guild = event.getGuild();
        List<TextChannel> channels = guild.getTextChannels();

        for (TextChannel channel : channels) {
            if (number.equals(channel.getTopic())) {
                event.getHook().editOriginal("That number is already being used in <#" + channel.getId() + ">").queue();
                return;
            }
        }

        String channelName = number.substring(1);
        if (event.getOption("alias") != null) channelName = event.getOption("alias").getAsString();

        TextChannel channel = guild.createTextChannel(channelName).complete();
        channel.getManager().setTopic(number).queue();
        event.getHook().editOriginal("Created text channel <#" + channel.getId() + ">").queue();
    }
}
