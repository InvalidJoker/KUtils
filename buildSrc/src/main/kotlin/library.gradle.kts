plugins {
    `java-library`
    `maven-publish`
}

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
        maven("https://maven.fsqrt.org/releases") {
            name = "fsqrt.releases"
            credentials {
                username = System.getenv("FSQRT_REPO_USER")
                password = System.getenv("FSQRT_REPO_PW")
            }
        }
    }
}