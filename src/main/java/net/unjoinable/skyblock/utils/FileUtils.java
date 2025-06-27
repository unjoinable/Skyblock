package net.unjoinable.skyblock.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    private static final Gson gson = new Gson();

    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * Loads a resource file from the classpath and returns it as an InputStream.
     *
     * @param resourcePath The relative path to the resource file (e.g., "config/file.json")
     * @return InputStream for the resource file
     * @throws IllegalArgumentException if resourcePath is empty
     * @throws IOException if the resource file cannot be found
     */
    public static InputStream loadResourceAsStream(String resourcePath) throws IOException {
        if (resourcePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource path cannot be null or empty");
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = FileUtils.class.getClassLoader();
        }

        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        return inputStream;
    }

    /**
     * Loads a JSON file from the classpath resources and parses it into a JsonObject.
     *
     * @param resourcePath The relative path to the resource file (e.g., "config/file.json")
     * @return JsonObject containing the parsed JSON content
     * @throws IllegalArgumentException if resourcePath is empty
     * @throws IOException if the resource file cannot be found or JSON parsing fails
     */
    public static JsonObject loadJsonFromResource(String resourcePath) throws IOException {
        try (InputStream inputStream = loadResourceAsStream(resourcePath);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            try {
                return gson.fromJson(reader, JsonObject.class);
            } catch (JsonSyntaxException e) {
                throw new IOException("Failed to parse JSON from resource: " + resourcePath, e);
            }
        }
    }
}