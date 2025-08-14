plugins {
    idea
}

val projectVersion: String by project

repositories {
    mavenCentral()
    maven("https://nexus.fruxz.dev/repository/public/") {
        content {
            includeGroup("dev.fruxz")
        }
    }
    maven("https://repo.papermc.io/repository/maven-public/")
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://nexus.fruxz.dev/repository/public/") {
            content {
                includeGroup("dev.fruxz")
            }
        }
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    // Set the group ID for all projects in the buildSrc module.
    group = "de.joker"
    version = "1.21.6-${projectVersion}"
}