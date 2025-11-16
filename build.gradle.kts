plugins {
  kotlin("multiplatform") version "2.2.21"
  id("org.jetbrains.dokka") version "2.1.0"
  id("com.vanniktech.maven.publish") version "0.35.0"
  id("signing")
}

group = "com.asyncant.crypto"
version = "1.1"

repositories {
  mavenCentral()
}
kotlin {
  jvm()
  linuxX64()
  macosX64()
  mingwX64()

  sourceSets {
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    jvmTest {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("org.junit.jupiter:junit-jupiter:6.0.1")
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val javadocJar by tasks.registering(Jar::class) {
  dependsOn(tasks.dokkaGenerateHtml)
  archiveClassifier.set("javadoc")
  from(dokka.dokkaPublications.html.get().outputDirectory)
}

mavenPublishing {
  publishToMavenCentral()

  signAllPublications()

  coordinates(group.toString(), project.name, version.toString())

  pom {
    name = "sha256-kt"
    description = "Pure Kotlin SHA-256 hash algorithm implementation."
    url = "https://github.com/asyncant/sha256-kt"
    licenses {
      license {
        name = "Public Domain"
        distribution = "repo"
      }
    }
    developers {
      developer {
        id = "asyncant"
        name = "asyncant"
        url = "http://www.asyncant.com"
      }
    }
    scm {
      connection = "scm:git:git://github.com/asyncant/sha256-kt.git"
      developerConnection = "scm:git:ssh://github.com/asyncant/sha256-kt.git"
      url = "https://github.com/asyncant/sha256-kt"
    }
  }
}

signing {
  useGpgCmd()
  sign(publishing.publications)
}
