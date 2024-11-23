package com.github.unjoinable.skyblock.util;

import com.google.common.collect.ImmutableSet;
import com.github.unjoinable.skyblock.statistics.Statistic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.Material;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Set;

public class Utils {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#");
    private static final Set<Material> LEATHER_MATERIAL = ImmutableSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);

    
    public static boolean probabilityCheck(int percentage) {
        Random rand = new Random();
        return rand.nextInt(100) < percentage;
    }

    public static int[] convertStringArrayToIntArray(String str) {
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
