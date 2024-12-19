plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "dev.goerner.geozen"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name = "GeoZen"
                description = "A Java library for working with geospatial data."
                url = "https://github.com/fgoerner/geozen"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://github.com/fgoerner/geozen/blob/main/LICENSE"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/fgoerner/geozen.git"
                    developerConnection = "scm:git:ssh://github.com/fgoerner/geozen.git"
                    url = "https://github.com/fgoerner/geozen"
                }

                developers {
                    developer {
                        id = "fgoerner"
                        name = "Felix Görner"
                        email = "felix@goerner.dev"
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        findProperty("signingKey") as String?,
        findProperty("signingPassword") as String?
    )
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}
