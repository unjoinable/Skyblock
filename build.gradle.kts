plugins {
    id("java")
}

group = "io.github.unjoinable"
version = "1.0-SNAPSHOT"
var minestomVersion = "6877277b60"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:$minestomVersion")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
}
