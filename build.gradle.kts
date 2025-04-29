plugins {
    id("java")
    id("io.freefair.lombok") version "8.6" // Auto-configures Lombok
}

group = "me.rejomy"
version = "1.0-SNAPSHOT"

configurations.all {
    resolutionStrategy {
        force("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://jitpack.io") } // Add this line for Vault
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")

    // Add flat directory for local libs
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Add all JAR files from libs directory as dependencies
    fileTree("libs").forEach { file ->
        if (file.name.endsWith(".jar")) {
            implementation(files(file.path))
        }
    }

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultUnlockedAPI:2.9") // Vault API
    compileOnly("com.github.retrooper:packetevents-spigot:2.7.0")
    compileOnly("org.projectlombok:lombok:1.18.30") // Compile-time only
    annotationProcessor("org.projectlombok:lombok:1.18.30") // Annotation processing
}