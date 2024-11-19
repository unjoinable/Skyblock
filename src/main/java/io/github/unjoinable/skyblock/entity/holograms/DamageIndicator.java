package io.github.unjoinable.skyblock.entity.holograms;

import io.github.unjoinable.skyblock.entity.Hologram;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.Random;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class DamageIndicator {
    private static final TextColor[] COLORS = {WHITE, WHITE, YELLOW, GOLD, RED, RED};
    private static final Component CRIT = Component.text('✧', WHITE);

    private final double damage;
    private final boolean criticalDamage;

    public DamageIndicator(double damage, boolean criticalDamage) {
        this.damage = damage;
        this.criticalDamage = criticalDamage;
    }

    public void spawn(Pos pos, Instance instance) {
        String text = StringUtils.makeRepresentable(damage);
        Component display;

        if (!criticalDamage) {
            display = Component.text(text, GRAY);
        } else {
            display = CRIT.append(getRainbowEffect(text)).append(CRIT);
        }

        Hologram hologram = new Hologram(display);
        hologram.scheduleRemoval(1000);
        hologram.setInstance(instance, random(pos));
    }

    private Component getRainbowEffect(String string) {
        Component coloured = Component.empty();
        int i = 0;
        for (String c : string.split("")) {
            if (c.equals(",")) {
                coloured = coloured.append(Component.text(c, WHITE));
                continue;
            }
            if (i > COLORS.length - 1) i = 0;
            coloured = coloured.append(Component.text(c, COLORS[i]));
            i++;
        }
        return coloured;
    }

    private Pos random(Pos pos) {
        Random random = new Random();
        double dI = random.nextDouble(-1,1);
        return pos.add(dI, 0, dI);
    }
}
