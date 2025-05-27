plugins {
    id("kotlin-jvm")
    id("library")
    alias(libs.plugins.paperweight)
}

val minecraftVersion = libs.versions.minecraft.get()

dependencies {
    paperweight.paperDevBundle("$minecraftVersion-R0.1-SNAPSHOT")
    api(project(":paper"))
    implementation(libs.fuel)
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev
        .ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
