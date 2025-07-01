package net.unjoinable.skyblock;

public class Skyblock {

    private Skyblock() {
        throw new AssertionError();
    }

    public static void main(String[] args) {
        ServerBootstrapper.bootstrap("0.0.0.0", 25565);
    }
}