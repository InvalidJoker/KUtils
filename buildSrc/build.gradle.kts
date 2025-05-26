plugins {
    `kotlin-dsl`
}

group = "de.joker.kutils"

subprojects {
    // Set the group ID for all projects in the buildSrc module.
    group = "de.joker.kutils"
    version = libs.versions.minecraft.get() + "0.0.1"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Add a dependency on the Kotlin Gradle plugin, so that convention plugins can apply it.
    implementation(libs.kotlinGradlePlugin)
}