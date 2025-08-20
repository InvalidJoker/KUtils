pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":core")
include(":core-i18n")
include(":redis")

include(":paper")
include(":paper-inventory")
include(":paper-ux")
include(":paper-commands")
include(":paper-mineskin")
include(":paper-luckperms")

include(":adventure")

include(":velocity")

rootProject.name = "KUtils"