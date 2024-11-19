package io.github.unjoinable.skyblock.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.unjoinable.skyblock.statistics.Statistic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Set;

public class Utils {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#");
    private static final Set<Material> LEATHER_MATERIAL = ImmutableSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);

    public static <T> T getRandomElement(@NotNull T[] ar) {
        Random rnd = new Random();
        return ar[rnd.nextInt(ar.length)];
    }

    public static JsonObject getJsonfromURL(String str) {
        try {
            URL url = new URI(str).toURL();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(in, JsonObject.class);
            in.close();
            return json;
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean probabilityCheck(int percentage) {
        Random rand = new Random();
        return rand.nextInt(100) < percentage;
    }

    public static int[] strArtoIntArray(String str) {
        String[] strs = str.split(",");
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.parseInt(strs[i]);
        }
        return ints;
    }

    public static boolean isLeather(Material material) {
        return LEATHER_MATERIAL.contains(material);
    }

    public static Component generateStatisticLore(Statistic statistic, double value) {
        String plus = statistic.getPercentage() ? "" : "+";
        String percentage = statistic.getPercentage() ? "%" : "";
        Component line = Component.text(statistic.getDisplayName() + ": ", NamedTextColor.GRAY)
                .append(Component.text(plus + decimalFormat.format(value) + percentage ,
                        statistic.getLoreColor()));

        return line.decoration(TextDecoration.ITALIC, false);
    }

    public static DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }
}
