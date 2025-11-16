plugins {
  kotlin("multiplatform") version "2.2.21"
  id("org.jetbrains.dokka") version "2.1.0"
  id("maven-publish")
  id("signing")
}

group = "com.asyncant.crypto"
version = "1.1-SNAPSHOT"

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

publishing {
  repositories {
    maven {
      name = "sonatype"
      val releaseUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
      val snapshotUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
      setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

      credentials(PasswordCredentials::class)
    }
  }

  publications.withType<MavenPublication> {
    artifact(javadocJar)
    pom {
      name.set("sha256-kt")
      description.set("Pure Kotlin SHA-256 hash algorithm implementation.")
      url.set("https://github.com/asyncant/sha256-kt")

      licenses {
        license {
          name.set("Public Domain")
          distribution.set("repo")
        }
      }

      developers {
        developer {
          id.set("asyncant")
          name.set("asyncant")
          url.set("http://www.asyncant.com")
        }
      }

      scm {
        connection.set("scm:git:git://github.com/asyncant/sha256-kt.git")
        developerConnection.set("scm:git:ssh://github.com/asyncant/sha256-kt.git")
        url.set("https://github.com/asyncant/sha256-kt")
      }
    }
  }
}

signing {
  useGpgCmd()
  sign(publishing.publications)
}
