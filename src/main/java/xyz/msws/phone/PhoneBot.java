package xyz.msws.phone;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import xyz.msws.phone.commands.Command;
import xyz.msws.phone.commands.PingCommand;

import java.util.ArrayList;
import java.util.List;

public class PhoneBot extends ListenerAdapter {
    private final JDA jda;

    public PhoneBot(String token) {
        this.jda = JDABuilder.createLight(token).build();
    }

    private void updateCommands() {
        CommandListUpdateAction updates = this.jda.updateCommands();

        List<Command> commands = new ArrayList<>();
        commands.add(new PingCommand());

        commands.forEach(command -> updates.addCommands(command.getCommandData()));
        updates.queue();
    }

    public JDA getJDA() {
        return this.jda;
    }


}
