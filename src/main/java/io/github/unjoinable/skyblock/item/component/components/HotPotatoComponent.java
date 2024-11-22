package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.ItemCategory;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.*;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.statistics.holders.StatModifier;
import io.github.unjoinable.skyblock.statistics.holders.StatModifiers;
import io.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import io.github.unjoinable.skyblock.statistics.holders.StatValueType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record HotPotatoComponent(int potatoes) implements BasicComponent, ModifierComponent, StatComponent {
    private static final Tag<Integer> POTATO_COUNT = Tag.Integer("potato_count");
    private static final int HEALTH_MODIFIER_ARMOR = 4;
    private static final int DEFENSE_MODIFIER_ARMOR = 2;
    private static final int STRENGTH_MODIFIER_NON_ARMOR = 2;
    private static final int DAMAGE_MODIFIER_NON_ARMOR = 2;

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.setTag(POTATO_COUNT, potatoes);
    }

    @Override
    public @Nullable Component retrieve(ItemStack.@NotNull Builder builder) {
        ItemStack item = builder.build();
        if (item.hasTag(POTATO_COUNT)) {
            return new HotPotatoComponent(item.getTag(POTATO_COUNT));
        }
        return null;
    }

    @Override
    public @NotNull StatModifiersMap statModifiers(@NotNull ComponentContainer container) {
        StatModifiersMap modifiersMap = new StatModifiersMap();
        ItemCategory category = container.getComponent(ItemCategoryComponent.class).category();

        if (category.isArmor()) {
            addArmorModifiers(modifiersMap);
        } else {
            addNonArmorModifiers(modifiersMap);
        }

        return modifiersMap;
    }

    private void addArmorModifiers(StatModifiersMap modifiersMap) {
        StatModifiers healthModifier = new StatModifiers();
        healthModifier.addModifier(new StatModifier(StatValueType.BASE, HEALTH_MODIFIER_ARMOR * potatoes));

        StatModifiers defenseModifier = new StatModifiers();
        defenseModifier.addModifier(new StatModifier(StatValueType.BASE, DEFENSE_MODIFIER_ARMOR * potatoes));

        modifiersMap.put(Statistic.HEALTH, healthModifier);
        modifiersMap.put(Statistic.DEFENSE, defenseModifier);
    }

    private void addNonArmorModifiers(StatModifiersMap modifiersMap) {
        StatModifiers strengthModifier = new StatModifiers();
        strengthModifier.addModifier(new StatModifier(StatValueType.BASE, STRENGTH_MODIFIER_NON_ARMOR * potatoes));

        StatModifiers damageModifier = new StatModifiers();
        damageModifier.addModifier(new StatModifier(StatValueType.BASE, DAMAGE_MODIFIER_NON_ARMOR * potatoes));

        modifiersMap.put(Statistic.STRENGTH, strengthModifier);
        modifiersMap.put(Statistic.DEFENSE, damageModifier);
    }
}
