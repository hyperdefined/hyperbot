package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.HyperBot;
import lol.hyper.hyperbot.storage.Database;
import lol.hyper.hyperbot.storage.SQLQueries;
import net.dv8tion.jda.api.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandQuote implements HyperCommand {

    private final Message message;
    private final Pattern QUOTE_MATCHER = Pattern.compile("\"?([^\"]+)\"?\\s-\\s(.+)");
    private final Logger logger = LogManager.getLogger(this);

    public CommandQuote(Message message) {
        this.message = message;
    }

    public void run() {
        if (HyperBot.isDebug()) {
            message.reply("Bot is in debug mode, cannot get quotes.").queue();
            return;
        }
        Message reply = message.getReferencedMessage();
        // if they are replying, assume they are quoting
        if (reply != null) {
            String quoteAuthor;
            String quoteText;
            String replyText = reply.getContentRaw();
            if (replyText.isEmpty()) {
                message.reply("Message must have text.").queue();
                return;
            }
            Matcher matcher = QUOTE_MATCHER.matcher(replyText);
            if (matcher.find()) {
                quoteText = matcher.group(1);
                quoteAuthor = matcher.group(2);
            } else {
                // If the original message is not a quote format, just use that as the text
                // and whoever sent for the author.
                quoteAuthor = reply.getAuthor().getName();
                quoteText = replyText;
            }

            if (quoteText.length() > 200) {
                message.reply("Quote is too long!").queue();
                return;
            }

            String quoteID = reply.getId();
            long date = reply.getTimeCreated().toEpochSecond();
            Database database = new Database();
            try (Connection connection = database.connect()) {
                // see if the quote is in the database already
                try (PreparedStatement checkStatement = connection.prepareStatement(SQLQueries.SELECT_ID)) {
                    checkStatement.setString(1, quoteID);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next()) {
                            message.reply("This quote is already saved in the database.").queue();
                            return;
                        }
                    }
                }

                try (PreparedStatement statement = connection.prepareStatement(SQLQueries.ADD_QUOTE)) {
                    statement.setString(1, quoteID);
                    statement.setString(2, quoteAuthor);
                    statement.setString(3, quoteText);
                    statement.setLong(4, date);
                    logger.info("Running {}", statement);
                    statement.executeUpdate();
					message.reply("Saved quote with ID " + quoteID).queue();
                    logger.info("Adding quote '{}' from {}, [ID={}] [Time={}]", quoteText, quoteAuthor, quoteID, date);
                }
            } catch (SQLException exception) {
                logger.error(exception);
                message.reply("Unable to save quote as there was a SQL error.").queue();
            }
        } else {
            // user tried to quote without replying
            message.reply("For me to save a quote, reply to the message and repeat the command. You can either manually format the quote as: `\"Quote text\" - name` or just reply to any message.").queue();
        }
    }
}
