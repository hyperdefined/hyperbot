package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.storage.Database;
import lol.hyper.hyperbot.storage.SQLQueries;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTopQuotes {

    private final SlashCommandInteractionEvent event;
    private final Logger logger = LogManager.getLogger(this);

    public CommandTopQuotes(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void run() {
        Map<String, Integer> authorQuoteCounts = new HashMap<>();
        Database database = new Database();
        try (Connection connection = database.connect()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_TOP)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String author = resultSet.getString("NormalizedAuthor");
                    int quoteCount = resultSet.getInt("QuoteCount");
                    authorQuoteCounts.put(author, quoteCount);
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
            event.reply("An SQL error was thrown.").queue();
            return;
        }

        if (authorQuoteCounts.isEmpty()) {
            event.reply("No quotes have been saved.").queue();
            return;
        }
        StringBuilder finalList = new StringBuilder("Top quoted people (against their will):\n");

        List<Map.Entry<String, Integer>> sortedEntries = authorQuoteCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .toList();

        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            finalList.append(i + 1)
                    .append(". ")
                    .append(entry.getKey()) // Author
                    .append(" - ")
                    .append(entry.getValue()) // Quote count
                    .append(" quotes\n");
        }

        event.reply(finalList.toString()).queue();
    }
}
