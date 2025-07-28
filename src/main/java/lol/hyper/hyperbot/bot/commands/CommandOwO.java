package lol.hyper.hyperbot.bot.commands;

import com.github.supern64.owoify.OwoLevel;
import com.github.supern64.owoify.Owoify;
import net.dv8tion.jda.api.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CommandOwO implements HyperCommand {

    private final Message message;
    private final Logger logger = LogManager.getLogger(this);

    public CommandOwO(Message message) {
        this.message = message;
    }

    public void run() {
        Message reply = message.getReferencedMessage();
        // if they are replying, assume they are quoting
        if (reply == null) {
            message.reply("You must be replying to a message to owoify.").queue();
            return;
        }

        String replyText = reply.getContentRaw();
        if (replyText.isEmpty()) {
            message.reply("Message must have text.").queue();
            return;
        }
        OwoLevel level = OwoLevel.values()[new Random().nextInt(OwoLevel.values().length)];
        logger.info("Rolling level: {}", level.toString());
        message.reply(Owoify.owoify(replyText, level)).queue();
    }
}
