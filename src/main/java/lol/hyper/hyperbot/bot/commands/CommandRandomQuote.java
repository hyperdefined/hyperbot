package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.storage.Database;
import lol.hyper.hyperbot.storage.SQLQueries;
import lol.hyper.hyperbot.utils.StringUtils;
import lol.hyper.hyperbot.utils.TimeUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandRandomQuote implements HyperCommand {

    private final SlashCommandInteractionEvent event;
    private final String author;
    private final Logger logger = LogManager.getLogger(this);

    public CommandRandomQuote(SlashCommandInteractionEvent event, String author) {
        this.event = event;
        this.author = author;
    }

    public void run() {
        // check to see if the user passed a name to check
        // if they did not, then grab a random quote
        if (author == null) {
            String author = null;
            String text = null;
            long date = -1;
            Database database = new Database();
            try (Connection connection = database.connect()) {
                try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_RANDOM)) {
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        author = resultSet.getString("QuoteAuthor");
                        text = resultSet.getString("QuoteText");
                        date = resultSet.getLong("QuoteDate");
                    }
                }
            } catch (SQLException exception) {
                logger.error(exception);
                event.reply("An SQL error was thrown!").queue();
                return;
            }
            if (author == null || text == null || date == -1) {
                event.reply("An SQL error was thrown!").queue();
                return;
            }

            String formattedTime = TimeUtils.formatDate(date);
            event.reply("As " + author + " once said, \"" + text + "\" on " + formattedTime).queue();
            return;
        }
        String nameNormalized = author.toLowerCase();
        String text;
        long date;
        Database database = new Database();
        try (Connection connection = database.connect()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_BY_NAME)) {
                statement.setString(1, nameNormalized);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    text = resultSet.getString("QuoteText");
                    date = resultSet.getLong("QuoteDate");
                } else {
                    logger.warn("No quotes found for {}", nameNormalized);
                    event.reply("That name is not in my database.").queue();
                    return;
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
            event.reply(StringUtils.getException(exception)).queue();
            return;
        }
        String formattedTime = TimeUtils.formatDate(date);
        event.reply("As " + author + " once said, \"" + text + "\" on " + formattedTime).queue();
    }
}
