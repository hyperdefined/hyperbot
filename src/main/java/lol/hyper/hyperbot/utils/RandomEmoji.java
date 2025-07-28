package lol.hyper.hyperbot.utils;

import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.util.Random;

public class RandomEmoji {

    private static final String[] emojis = new String[]{ "\uD83D\uDC3A", "\uD83D\uDCA6", "\uD83C\uDF46", "\uD83E\uDE72", "\uD83E\uDD8A", "\uD83D\uDD2A", "\uD83C\uDF7D", "\uD83C\uDF7D" };

    public static Emoji random() {
        Random rand = new Random();
        int index = rand.nextInt(emojis.length);
        return Emoji.fromUnicode(emojis[index]);
    }
}
