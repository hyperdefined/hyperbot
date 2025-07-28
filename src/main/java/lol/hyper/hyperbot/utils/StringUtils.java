package lol.hyper.hyperbot.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {

    public static String getException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
