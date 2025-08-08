plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    api(project(":core"))
    api(libs.stacked)
    api(libs.bundles.adventure)
}
