plugins {
    id("java")
}

group = "net.skyblock"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    // Libs
    implementation("net.minestom:minestom-snapshots:1_21_6-a40d7115d4")
    implementation("com.google.guava:guava:33.2.1-jre")

    // Data
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.yaml:snakeyaml:2.4")

    // Annotations
    implementation("org.jspecify:jspecify:1.0.0")
}
