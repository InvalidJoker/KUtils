plugins {
    id("kotlin-jvm")
    id("library")
}


dependencies {
    implementation(project(":core"))
    implementation(libs.caffeine)
}
