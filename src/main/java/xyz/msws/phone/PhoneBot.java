package xyz.msws.phone;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import xyz.msws.phone.commands.Command;
import xyz.msws.phone.commands.PingCommand;
import xyz.msws.phone.commands.TextCommand;

import java.nio.channels.Channel;
import java.util.*;

public class PhoneBot extends ListenerAdapter implements IPhoneBot {
    private final JDA jda;
    private final Map<String, Command> commands = new HashMap<>();
    private Messenger messenger;

    private final EnumSet<GatewayIntent> intents = EnumSet.of(
            // Enables MessageReceivedEvent for guild (also known as servers)
            GatewayIntent.GUILD_MESSAGES,
            // Enables access to message.getContentRaw()
            GatewayIntent.MESSAGE_CONTENT,
            // Enables MessageReactionAddEvent for guild
            GatewayIntent.GUILD_MESSAGE_REACTIONS);

    public PhoneBot(String token) {
        this.jda = JDABuilder.createLight(token, intents).addEventListeners(this).build();

        messenger = new TwilioMessenger();

        updateCommands();
    }

    private void updateCommands() {
        CommandListUpdateAction updates = this.jda.updateCommands();

        commands.put("ping", new PingCommand());
        commands.put("text", new TextCommand());

        commands.values().forEach(command -> updates.addCommands(command.getCommandData()));
        updates.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getFullCommandName());
        if (command == null) {
            event.getInteraction().reply("Command `" + event.getFullCommandName() + "` not found, let my creator know!").queue();
            return;
        }
        command.execute(event);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return;
        MessageChannelUnion channel = event.getChannel();
        if (!(channel instanceof TextChannel textChannel)) return;
        if (textChannel.getTopic() == null || !NumberSanitizer.isValidNumber(textChannel.getTopic())) return;

        String number = textChannel.getTopic();
        String message = event.getMessage().getContentRaw();
        getMessenger().sendMessage(number, message);
        // Add checkmark reaction to message
        event.getMessage().addReaction(Emoji.fromUnicode("U+2705")).queue();
    }

    public JDA getJDA() {
        return this.jda;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public void relayMessage(String number, String message) {
        TextChannel channel = getChannel(number);
        channel.sendMessage(message).queue();
    }

    private TextChannel getChannel(String number) {
        number = NumberSanitizer.formatToE164(number);
        for (TextChannel channel : jda.getTextChannels()) {
            if (channel.getTopic() == null) continue;
            if (channel.getTopic().equals(number)) return channel;
        }
        jda.getGuilds().get(0).createTextChannel(number).setTopic(number).queue();
        return getChannel(number);
    }
}
