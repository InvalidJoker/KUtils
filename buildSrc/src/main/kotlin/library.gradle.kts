plugins {
    `java-library`
    `maven-publish`
    signing
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

            groupId = "dev.invalidjoker.kutils"
            artifactId = project.name
            version = project.version.toString()

            println("Publishing $groupId:$artifactId:$version")

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
        val repoUrl = if (project.version.toString().endsWith("SNAPSHOT")) {
            "https://central.sonatype.com/repository/maven-snapshots/"
        } else {
            "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
        }
        maven {
            name = "sonatype"
            url = uri(repoUrl)
            credentials {
                username = findProperty("sonatypeUsername") as String?
                password = findProperty("sonatypePassword") as String?
            }
        }
    }
}

signing {
    sign(publishing.publications)
}