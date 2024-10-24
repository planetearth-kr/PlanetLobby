plugins {
    java
}

group = "kr.planetearth.plugins"
version = "1.0-SNAPSHOT"
description = "PlanetLobby plugin"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://nexus.scarsz.me/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.discordsrv:discordsrv:1.28.0")
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version)
        }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}