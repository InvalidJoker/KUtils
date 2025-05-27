plugins {
    idea
}

val projectVersion: String by project

subprojects {

    repositories {
        mavenCentral()
        maven("https://repo.fruxz.dev/releases/") {
            content {
                includeGroup("dev.fruxz")
            }
        }
    }

    // Set the group ID for all projects in the buildSrc module.
    group = "de.joker.kutils"
    version = "1.21.5-${projectVersion}"
}