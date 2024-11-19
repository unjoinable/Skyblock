package io.github.unjoinable.skyblock.island;

import io.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.coordinate.Pos;

interface Islands {

     Island PRIVATE_ISLAND = new IslandImpl(new NamespacedId("island", "private_island"), "Private Island", new Pos(0,0,0));

     Island HUB = new IslandImpl(new NamespacedId("island", "hub"), "Hub", new Pos(-2, 71, -68).withYaw(-180F));

}
