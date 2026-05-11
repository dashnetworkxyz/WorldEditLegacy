plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "worldedit"
include("worldedit-core")
include("worldedit-bukkit")
include("worldedit-forge")
//include("worldedit-sponge") TODO: No sponge for now
