plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.kotlinPluginSerialization)
}


dependencies {
    implementation(project(":core"))
    implementation(libs.redis)
    implementation(libs.bundles.kotlinxEcosystem)
}
