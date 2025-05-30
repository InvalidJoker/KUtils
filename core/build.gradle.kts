plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    api(libs.bundles.kotlinxEcosystem)
    api(libs.bundles.utils)
}