package net.unjoinable.skyblock.ui.actionbar;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.kyori.adventure.text.Component.text;

/**
 * Manages the display content for different sections of the action bar.
 * <p>
 * This class handles temporary content replacements with priority and duration management,
 * building the complete action bar display by assembling content from all sections.
 */
public class ActionBar {
    private final Map<ActionBarSection, List<ActionBarDisplay>> replacements;
    private final Map<ActionBarSection, Component> defaultDisplays;

    protected static final Component SECTION_SEPARATOR = text("     ");

    /**
     * Creates a new action bar manager with empty sections.
     */
    public ActionBar() {
        this.replacements = new EnumMap<>(ActionBarSection.class);
        this.defaultDisplays = new EnumMap<>(ActionBarSection.class);

        // Initialize section replacement lists
        for (ActionBarSection section : ActionBarSection.getValues()) {
            replacements.put(section, new CopyOnWriteArrayList<>());
        }
    }

    /**
     * Sets the default display component for a section, shown when no replacements are active.
     *
     * @param section The section to set the default display for
     * @param display The component to display when no replacements are active
     */
    public void setDefaultDisplay(ActionBarSection section, Component display) {
        defaultDisplays.put(section, display);
    }

    /**
     * Adds a temporary replacement for a section of the action bar.
     * <p>
     * If the replacement has a duration > 0, it will be automatically removed after that duration.
     * Replacements are sorted by priority, with higher priority values taking precedence.
     *
     * @param section The section to add the replacement to
     * @param replacement The replacement to add
     */
    public void addReplacement(ActionBarSection section, ActionBarDisplay replacement) {
        List<ActionBarDisplay> sectionReplacements = replacements.get(section);
        sectionReplacements.add(replacement);

        // Sort by priority (descending)
        sectionReplacements.sort(Comparator.comparingInt(ActionBarDisplay::priority).reversed());

        // Schedule automatic removal if duration is specified
        if (replacement.duration() > 0) {
            scheduleRemoval(section, replacement);
        }
    }

    /**
     * Schedules automatic removal of a replacement after its duration expires.
     *
     * @param section The section containing the replacement
     * @param replacement The replacement to remove after its duration
     */
    private void scheduleRemoval(ActionBarSection section, ActionBarDisplay replacement) {
        MinecraftServer.getSchedulerManager().scheduleTask(
                () -> replacements.get(section).remove(replacement),
                TaskSchedule.tick(replacement.duration()),
                TaskSchedule.stop()
        );
    }

    /**
     * Builds the complete action bar display by combining all active sections.
     * <p>
     * For each section, uses the highest priority replacement if available,
     * otherwise falls back to the default display.
     *
     * @return The complete action bar component
     */
    public Component build() {
        Component content = Component.empty();
        boolean isFirst = true;

        for (ActionBarSection section : ActionBarSection.getValues()) {
            Component sectionDisplay = getDisplayForSection(section);

            if (sectionDisplay != Component.empty()) {
                if (!isFirst) {
                    content = content.append(SECTION_SEPARATOR);
                } else {
                    isFirst = false;
                }
                content = content.append(sectionDisplay);
            }
        }

        return content;
    }

    /**
     * Gets the current display for a specific section.
     * <p>
     * Returns the highest priority active replacement if available,
     * otherwise returns the default display for that section.
     *
     * @param section The section to get the display for
     * @return The component to display for the section
     */
    private Component getDisplayForSection(ActionBarSection section) {
        List<ActionBarDisplay> sectionReplacements = replacements.get(section);

        if (!sectionReplacements.isEmpty()) {
            return sectionReplacements.get(0).display();
        }

        return defaultDisplays.getOrDefault(section, Component.empty());
    }
}
