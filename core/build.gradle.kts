plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    api(libs.bundles.kotlinxEcosystem)
    api(libs.bundles.utils)
}