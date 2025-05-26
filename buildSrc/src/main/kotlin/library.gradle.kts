plugins {
    `java-library`
    `maven-publish`
}

// debug print version
println("KUtils version: ${project.version}")
println("KUtils group: ${project.group}")

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components.findByName("java"))

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            groupId = "de.joker.kutils"
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set("The ${project.name} project provides various utilities and extensions")
                url.set("https://github.com/InvalidJoker/KUtils")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        id.set("invalidjoker")
                        name.set("InvalidJoker")
                    }
                }
            }
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/InvalidJoker/KUtils")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

}