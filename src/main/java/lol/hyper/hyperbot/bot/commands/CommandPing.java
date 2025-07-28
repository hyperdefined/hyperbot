package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.bot.DiscordHyperBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Date;

public class CommandPing implements HyperCommand {

    private final SlashCommandInteractionEvent event;
    private final DiscordHyperBot bot;

    public CommandPing(SlashCommandInteractionEvent event, DiscordHyperBot bot) {
        this.event = event;
        this.bot = bot;
    }

    public void run() {
        Date received = new Date(System.currentTimeMillis());
        String builder = "Pong! (Received: " + received.getTime() + ")\n" +
                "Last gateway ping: " + bot.bot().getGatewayPing() + "ms (bot -> Discord)";
        event.reply(builder).queue();
    }
}
