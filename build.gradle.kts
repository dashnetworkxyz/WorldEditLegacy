import org.ajoberstar.grgit.Grgit
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar

plugins {
    java
    id("com.gradleup.shadow") version "9.4.1"
    id("org.ajoberstar.grgit") version "5.3.3"
}

println(
    """
    *******************************************
     You are building WorldEdit!
    
     If you encounter trouble:
     1) Read COMPILING.md if you haven't yet
     2) Try running 'build' in a separate Gradle run
     3) Use gradlew and not gradle
    
     Output files will be in [subproject]/build/libs
    *******************************************
    """.trimIndent()
)

allprojects {
    group = "com.sk89q.worldedit"
    version = "6.1.9-SNAPSHOT"
}

if (!project.hasProperty("gitCommitHash")) {
    val repo = Grgit.open(mapOf("dir" to project.projectDir))

    try {
        project.extensions.extraProperties.set("gitCommitHash", repo.head().abbreviatedId)
    } catch (e: Exception) {
        println("Error getting commit hash: ${e.message}")
    } finally {
        repo.close()
    }
}

if (!project.hasProperty("gitCommitHash")) {
    project.extensions.extraProperties.set("gitCommitHash", "no_git_id")
}

project.extensions.extraProperties.set(
    "internalVersion",
    "${project.version};${project.extensions.extraProperties["gitCommitHash"]}"
)

subprojects {
    plugins.apply("java")
    plugins.apply("com.gradleup.shadow")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.enginehub.org/artifactory/ext-release-local") }
        maven { url = uri("https://maven.enginehub.org/artifactory/libs-release-local") }
    }

    tasks.withType<Javadoc>().configureEach {
        (options as? StandardJavadocDocletOptions)?.addStringOption("Xdoclint:none", "-quiet")
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
        description = "Add javadoc jar"
        archiveClassifier.set("javadoc")
        from(tasks.named<Javadoc>("javadoc").get().destinationDir)
    }

    tasks.withType<Copy>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    artifacts {
        add("archives", tasks.named("jar"))
        add("archives", javadocJar)
    }

    if (!(project.name == "worldedit-forge" || project.name == "worldedit-sponge")) {
        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            description = "Add source jar"
            archiveClassifier.set("sources")
            from(sourceSets["main"].allSource)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        artifacts {
            add("archives", sourcesJar)
        }

        tasks.named("build") {
            dependsOn(sourcesJar)
        }
    }

    tasks.named("build") {
        dependsOn(javadocJar)
    }

    tasks.withType<ShadowJar>().configureEach {
        archiveClassifier.set("dist")

        dependencies {
            include(dependency("com.sk89q:jchronic:0.2.4a"))
            include(dependency("com.thoughtworks.paranamer:paranamer:2.6"))
            include(dependency("com.sk89q.lib:jlibnoise:1.0.0"))
        }

        exclude("GradleStart**")
        exclude(".cache")
        exclude("LICENSE*")
    }
}
