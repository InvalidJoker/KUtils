dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.fruxz.dev/releases/") {
            content {
                includeGroup("dev.fruxz")
            }
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":core")
include(":paper")

rootProject.name = "KUtils"