package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.HyperBot;
import lol.hyper.hyperbot.bot.DiscordHyperBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CommandStatus {

    private final SlashCommandInteractionEvent event;
    private final DiscordHyperBot bot;

    public CommandStatus(SlashCommandInteractionEvent event, DiscordHyperBot bot) {
        this.event = event;
        this.bot = bot;
    }

    public void run() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        String uptime = getDurationString(bot.startTime(), now);
        int guilds = bot.bot().getGuilds().size();
        StringBuilder builder = new StringBuilder();
        builder.append("Awoo! I am hyperbot, a custom bot written in Java using the JDA library.\n");
        builder.append("I've been online for ").append(uptime).append("\n");
        builder.append("I am currently in ").append(guilds).append(" server(s).");
        if (HyperBot.isDebug()) {
            builder.append("\nI am running in debug mode!");
        }
        event.reply(builder.toString()).queue();
    }

    private String getDurationString(ZonedDateTime start, ZonedDateTime end) {
        // Calculate the Period (years, months, days)
        Period period = Period.between(start.toLocalDate(), end.toLocalDate());

        // Calculate the Duration (hours, minutes, seconds)
        ZonedDateTime tempDateTime = start.plusYears(period.getYears())
                .plusMonths(period.getMonths())
                .plusDays(period.getDays());
        Duration duration = Duration.between(tempDateTime, end);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        // Build the result string
        StringBuilder result = new StringBuilder();
        if (period.getYears() > 0) {
            result.append(period.getYears()).append(" year").append(period.getYears() > 1 ? "s" : "").append(", ");
        }
        if (period.getMonths() > 0) {
            result.append(period.getMonths()).append(" month").append(period.getMonths() > 1 ? "s" : "").append(", ");
        }
        if (period.getDays() > 0) {
            result.append(period.getDays()).append(" day").append(period.getDays() > 1 ? "s" : "").append(", ");
        }
        if (hours > 0) {
            result.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        }
        if (minutes > 0) {
            result.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        }
        if (seconds > 0 || result.isEmpty()) { // Include seconds if it's the only unit
            result.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        } else {
            // Remove the trailing comma and space
            result.setLength(result.length() - 2);
        }

        return result.toString();
    }
}
