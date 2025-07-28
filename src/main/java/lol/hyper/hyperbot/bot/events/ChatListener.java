package lol.hyper.hyperbot.bot.events;

import lol.hyper.hyperbot.bot.DiscordHyperBot;
import lol.hyper.hyperbot.bot.commands.CommandOwO;
import lol.hyper.hyperbot.bot.commands.CommandQuote;
import lol.hyper.hyperbot.utils.RandomEmoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class ChatListener extends ListenerAdapter {

    private final DiscordHyperBot discordQuoteBot;
    private final Logger logger = LogManager.getLogger(this);

    public ChatListener(DiscordHyperBot discordQuoteBot) {
        this.discordQuoteBot = discordQuoteBot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        ChannelType type = event.getChannelType();
        User user = event.getAuthor();
        // ignore self
        if (user == discordQuoteBot.bot().getSelfUser()) {
            return;
        }

        // only listen for text chats
        if (type != ChannelType.TEXT) {
            return;
        }

        Message message = event.getMessage();
        String rawMessage = message.getContentRaw();

        // hardcore the ino quote
        if (rawMessage.equalsIgnoreCase("ino quote")) {
            new CommandQuote(message).run();
            return;
        }

        // ignore messages that don't start with the prefix
        if (!rawMessage.startsWith("barky")) {
            // Message is not a command
            // Roll for random emoji
            Random random = new Random();
            int roll = random.nextInt(100);
            logger.info("Rolled a {} for random emoji", roll);
            if (roll < 5) {
                message.addReaction(RandomEmoji.random()).queue();
            }
            return;
        }

        String arguments = rawMessage.substring("barky".length()).trim();
        String[] args = arguments.split("\\s+");
        logger.info("Command sent with args: {} by {}", Arrays.toString(args), user.getId());

        // handle command
        if (args[0].equals("quote")) {
            new CommandQuote(message).run();
            return;
        }
        if (args[0].equals("owo")) {
            new CommandOwO(message).run();
        }
    }
}
