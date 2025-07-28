package lol.hyper.hyperbot.utils;


import lol.hyper.hyperbot.HyperBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JSONUtils {

    /**
     * The JSONUtils logger.
     */
    private static final Logger logger = LogManager.getLogger(JSONUtils.class);
    /**
     * HttpClient for requests.
     */
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Read contents of a file.
     *
     * @param file The file to read.
     * @return The data from the file.
     */
    public static String readFile(File file) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(file.toPath());
        } catch (IOException exception) {
            logger.error("Unable to read file {}", file, exception);
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Get a JSONObject from a URL.
     *
     * @param url The URL to get JSON from.
     * @return The response JSONObject. Returns null if there was some issue.
     */
    public static JSONObject fetchJSON(String url) {
        logger.info("Fetching JSONObject from {}", url);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("User-Agent", HyperBot.getUserAgent())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONObject(response.body());
            } else {
                logger.error("HTTP status code {} for {} in getting JSONObject", response.statusCode(), url);
                return null;
            }
        } catch (Exception exception) {
            logger.error("Unable to request JSONObject", exception);
            return null;
        }
    }
}
