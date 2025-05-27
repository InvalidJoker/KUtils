plugins {
    `kotlin-dsl`
}

group = "de.joker.kutils"

val projectVersion: String by project

subprojects {
    print(projectVersion)
    // Set the group ID for all projects in the buildSrc module.
    group = "de.joker.kutils"
    version = libs.versions.minecraft.get() + "-$projectVersion"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Add a dependency on the Kotlin Gradle plugin, so that convention plugins can apply it.
    implementation(libs.kotlinGradlePlugin)
}