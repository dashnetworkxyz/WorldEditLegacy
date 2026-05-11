import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("eclipse")
    id("idea")
}

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public") }
}

dependencies {
    implementation(project(":worldedit-core"))
    implementation("com.sk89q:dummypermscompat:1.8")
    implementation("org.bukkit:bukkit:1.9.4-R0.1-SNAPSHOT")
    implementation("com.google.code.findbugs:annotations:3.0.1")
    testImplementation("org.mockito:mockito-core:5.23.0")
}

tasks.processResources {
    from(sourceSets["main"].resources.srcDirs) {
        expand(mapOf("internalVersion" to rootProject.extra["internalVersion"]))
        include("plugin.yml")
    }

    from(sourceSets["main"].resources.srcDirs) {
        exclude("plugin.yml")
    }
}

tasks.jar {
    manifest {
        attributes(
            "Class-Path" to "truezip.jar WorldEdit/truezip.jar js.jar WorldEdit/js.jar",
            "WorldEdit-Version" to project.version
        )
    }
}

tasks.withType<ShadowJar>().configureEach {
    dependencies {
        include(dependency(":worldedit-core"))
        include(dependency("com.google.code.gson:gson:2.2.4"))
    }

    relocate("com.google.gson", "com.sk89q.worldedit.internal.gson")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}
