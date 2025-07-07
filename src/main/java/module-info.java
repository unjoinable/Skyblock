import org.jspecify.annotations.NullMarked;

@NullMarked
module net.unjoinable.skyblock {
    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure.nbt;
    requires net.kyori.examination.api;
    requires net.minestom.server;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires org.jspecify;
    requires net.kyori.adventure.text.serializer.legacy;
    requires net.kyori.adventure.text.serializer.plain;
    requires com.google.gson;
    requires net.kyori.adventure.key;
    requires it.unimi.dsi.fastutil;
}