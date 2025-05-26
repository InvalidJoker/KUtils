plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

repositories {
    mavenCentral()
    maven("https://repo.fruxz.dev/releases/") {
        content {
            includeGroup("dev.fruxz")
        }
    }
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    api(libs.bundles.kotlinxEcosystem)
    api(libs.bundles.utils)
}