import java.net.HttpURLConnection
import java.net.URL

plugins {
    id("java")
}

group = "io.github.unjoinable"
version = "1.0-SNAPSHOT"
var api = "https://api.github.com/repos/Minestom/Minestom/commits"
var minestomVersion = getLatestCommitHash(api)

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    implementation("net.minestom:minestom-snapshots:1_21_5-aa17002536")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("org.reflections:reflections:0.10.2")
}

//auto updater
fun fetchFromUrl(url: String): String {
    val result = StringBuilder()
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    connection.inputStream.bufferedReader().use {
        it.lines().forEach { line ->
            result.append(line)
        }
    }
    return result.toString()
}

fun getLatestCommitHash(url: String): String {
    val commits = fetchFromUrl(url)
    return commits.split("sha")[1].substring(3,13)
}
