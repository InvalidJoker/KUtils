plugins {
    id("kotlin-jvm")
    id("library")
}


dependencies {
    api(project(":core"))
    implementation(libs.caffeine)
}
