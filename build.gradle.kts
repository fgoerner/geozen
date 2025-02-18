import com.vanniktech.maven.publish.SonatypeHost

plugins {
	id("java-library")
	id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "dev.goerner.geozen"
version = project.findProperty("version") as String? ?: "0.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
	implementation("net.sf.geographiclib:GeographicLib-Java:2.0")
}

tasks.test {
	useJUnitPlatform()
}

mavenPublishing {
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

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
