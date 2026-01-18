plugins {
    id("java-library")
    id("com.vanniktech.maven.publish") version "0.34.0"
    kotlin("jvm")
}

group = "dev.goerner.geozen"
version = project.findProperty("version") as String? ?: "0.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    implementation(platform("tools.jackson:jackson-bom:3.0.0"))
    implementation("tools.jackson.core:jackson-databind")
    implementation("net.sf.geographiclib:GeographicLib-Java:2.1")
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "GeoZen Core"
        description = "A library for working with geospatial data"
        inceptionYear = "2024"
        url = "https://github.com/fgoerner/geozen"
        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/fgoerner/geozen/blob/main/LICENSE"
            }
        }
        developers {
            developer {
                id = "fgoerner"
                name = "Felix Görner"
                email = "felix@goerner.dev"
            }
        }
        scm {
            url = "https://github.com/fgoerner/geozen"
            connection = "scm:git:git://github.com/fgoerner/geozen.git"
            developerConnection = "scm:git:ssh://git@github.com/fgoerner/geozen.git"
        }
    }
}
