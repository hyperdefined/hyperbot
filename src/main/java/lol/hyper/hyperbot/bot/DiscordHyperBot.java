package lol.hyper.hyperbot.bot;

import lol.hyper.hyperbot.bot.events.ChatListener;
import lol.hyper.hyperbot.bot.events.GatewayPingListener;
import lol.hyper.hyperbot.bot.events.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DiscordHyperBot {

    private final ZonedDateTime startTime;
    private final JDA DISCORD_BOT;

    public DiscordHyperBot(JSONObject config) {
        startTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

        String DISCORD_TOKEN = config.getString("token");
        Logger logger = LogManager.getLogger(this);
        logger.info("Starting bot!");
        DISCORD_BOT = JDABuilder.createDefault(DISCORD_TOKEN).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        logger.info("Invite: {}", DISCORD_BOT.getInviteUrl());

        logger.info("Creating event listeners...");
        ChatListener chatListener = new ChatListener(this);
        DISCORD_BOT.addEventListener(chatListener);
        SlashCommandListener slashCommandListener = new SlashCommandListener(this);
        DISCORD_BOT.addEventListener(slashCommandListener);
        GatewayPingListener gatewayPingListener = new GatewayPingListener();
        DISCORD_BOT.addEventListener(gatewayPingListener);

        logger.info("Adding slash commands...");
        CommandListUpdateAction commands = DISCORD_BOT.updateCommands();
        commands.addCommands(
                Commands.slash("roll", "Roll a number between 1 and the input.").addOption(OptionType.INTEGER, "number", "Number to roll 1 and between.", true),
                Commands.slash("awoo", "Awoo!"),
                Commands.slash("bark", "Bark!"),
                Commands.slash("joke", "Tell a joke!").addOption(OptionType.STRING, "type", "Type of joke (random, programming, dark, or pun).", true),
                Commands.slash("ping", "Pong!"),
                Commands.slash("randomquote", "Get a random quote.").addOption(OptionType.STRING, "author", "The author to get a quote from.", false),
                Commands.slash("topquotes", "Get the top users quoted from the bot."),
                Commands.slash("status", "Get the status of the bot."),
                Commands.slash("animal", "Get a random picture of an animal.").addOption(OptionType.STRING, "animal", "The animal for a photo.", true)
        ).queue();

        logger.info("Bot is now online!");
        DISCORD_BOT.getPresence().setPresence(OnlineStatus.ONLINE, Activity.customStatus("Preparing for world domination!"));
    }

    public JDA bot() {
        return DISCORD_BOT;
    }

    public ZonedDateTime startTime() {
        return startTime;
    }
}
