plugins {
    id("kotlin-jvm")
    id("library")
}

dependencies {
    api(project(":core"))
    api(libs.bundles.minecraft)
    compileOnly(libs.velocity)
    implementation(libs.bundles.commandapi.velocity)
    annotationProcessor(libs.bundles.commandapi.velocity)
}
