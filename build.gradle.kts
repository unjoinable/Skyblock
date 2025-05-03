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
    implementation("net.minestom:minestom-snapshots:1_21_5-aa17002536")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("org.yaml:snakeyaml:2.4")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.tinylog:slf4j-tinylog:2.7.0")
}
