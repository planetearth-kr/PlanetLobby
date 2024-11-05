plugins {
    java
}

group = "kr.planetearth.plugins"
version = "1.0-SNAPSHOT"
description = "PlanetLobby plugin for planetearth.kr"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    //compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.purpurmc.purpur:purpur-api:1.20.1-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(
                "version" to project.version,
                "description" to project.description
            )
        }
        filteringCharset = "UTF-8"
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}