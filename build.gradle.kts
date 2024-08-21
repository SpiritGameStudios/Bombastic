plugins {
    java
    id("fabric-loom") version "1.7-SNAPSHOT"
}

class ModInfo {
    val id = property("mod.id").toString()
    val group = property("mod.group").toString()
    val version = property("mod.version").toString()
}

val mod = ModInfo()
val loaderVersion = property("deps.fabric_loader").toString()

loom {
    accessWidenerPath = file("src/main/resources/bombastic.accesswidener")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.callmeecho.dev/snapshots/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings("net.fabricmc:yarn:${property("deps.yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    fun specterModule(name: String) {
        include("dev.spiritstudios.specter:specter-$name:${property("deps.specter")}")
        modImplementation("dev.spiritstudios.specter:specter-$name:${property("deps.specter")}")
    }

    specterModule("api")
    specterModule("core")
    specterModule("config")
    specterModule("item")
    specterModule("block")
    specterModule("registry")
    specterModule("render")
    specterModule("biome")}


tasks.processResources {
    inputs.property("id", mod.id)
    inputs.property("version", mod.version)

    val map = mapOf(
        "id" to mod.id,
        "version" to mod.version
    )

    filesMatching("fabric.mod.json") { expand(map) }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
