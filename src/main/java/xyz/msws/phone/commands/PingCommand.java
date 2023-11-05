package xyz.msws.phone.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class PingCommand implements Command {
    @Override
    public CommandData getCommandData() {
        return Commands.slash("ping", "Pong!");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getInteraction().reply("Pong!").queue();
    }
}
