pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":core")
include(":core-i18n")
include(":paper")
include(":paper-inventory")
include(":paper-ux")
include(":paper-commands")
include(":paper-mineskin")

rootProject.name = "KUtils"