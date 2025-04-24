package net.skyblock.item.component.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minestom.server.item.Material;

import java.io.IOException;

/**
 * Custom {@link TypeAdapter} for serializing and deserializing {@link Material} objects.
 * <p>
 * This adapter converts a {@link Material} to its string representation when writing to JSON,
 * and converts a string back into the corresponding {@link Material} when reading from JSON.
 * If an invalid string is provided during deserialization, the {@link Material#AIR} is returned as a fallback.
 */
public class MaterialAdapter extends TypeAdapter<Material> {

    /**
     * Writes the {@link Material} object as a string to JSON.
     *
     * @param out the {@link JsonWriter} used to write to the output
     * @param value the {@link Material} object to be serialized
     * @throws IOException if an I/O error occurs during writing
     */
    @Override
    public void write(JsonWriter out, Material value) throws IOException {
        out.value(value.name());
    }

    /**
     * Reads a {@link Material} from a string in JSON format.
     * If the string does not match a valid {@link Material} key, {@link Material#AIR} is returned as a fallback.
     *
     * @param in the {@link JsonReader} used to read the input
     * @return the corresponding {@link Material} object, or {@link Material#AIR} if invalid
     * @throws IOException if an I/O error occurs during reading
     */
    @Override
    public Material read(JsonReader in) throws IOException {
        String str = in.nextString();
        try {
            return Material.fromKey(str.toLowerCase());
        } catch (IllegalArgumentException e) {
            return Material.AIR; // Default fallback
        }
    }
}
