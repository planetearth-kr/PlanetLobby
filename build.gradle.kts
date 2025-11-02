plugins {
    java
    id("com.gradleup.shadow") version "9.2.2"
}

group = providers.gradleProperty("group").get()
version = providers.gradleProperty("version").get()
description = providers.gradleProperty("description").get()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.viaversion.com/")
}

configurations.all {
    exclude(group = "org.bukkit", module = "bukkit")

    resolutionStrategy {
        force("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.viaversion:viaversion-api:5.5.1")
    compileOnly("net.luckperms:api:5.5")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.11.5")
}

tasks {
    processResources {
        val projectVersion = version
        val projectDescription = description
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(
                mapOf(
                    "version" to projectVersion,
                    "description" to projectDescription
                )
            )
        }
    }

    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    shadowJar {
        relocate("com.github.stefvanschie.inventoryframework", "kr.planetearth.plugins.inventoryframework")

        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }

        archiveClassifier.set("")
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    build {
        dependsOn(shadowJar)
    }
}