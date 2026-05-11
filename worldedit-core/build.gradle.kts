plugins {
    id("eclipse")
    id("idea")
}

dependencies {
    implementation("de.schlichtherle:truezip:6.8.3")
    implementation("rhino:js:1.7R2")
    implementation("org.yaml:snakeyaml:1.9")
    implementation("com.google.guava:guava:21.0")
    implementation("com.sk89q:jchronic:0.2.4a")
    implementation("com.google.code.findbugs:jsr305:1.3.9")
    implementation("com.thoughtworks.paranamer:paranamer:2.6")
    implementation("com.google.code.gson:gson:2.2.4")
    implementation("com.sk89q.lib:jlibnoise:1.0.0")
    testImplementation("org.mockito:mockito-core:5.23.0")
}

sourceSets {
    named("main") {
        java {
            setSrcDirs(listOf("src/main/java", "src/legacy/java"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}
