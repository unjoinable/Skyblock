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
    implementation("net.minestom:minestom-snapshots:1_21_6-a40d7115d4")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    compileOnly("org.jspecify:jspecify:1.0.0")
}
