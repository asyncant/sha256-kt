plugins {
  kotlin("multiplatform") version "1.5.0"
  id("org.jetbrains.dokka") version "1.4.32"
  id("maven-publish")
  id("signing")
}

group = "com.asyncant.crypto"
version = "1.1-SNAPSHOT"

repositories {
  mavenCentral()
}
kotlin {
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "15"
    }
  }
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux" -> linuxX64("native")
    isMingwX64 -> mingwX64("native")
    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  sourceSets {
    val commonMain by getting
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val jvmMain by getting
    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation("org.junit.jupiter:junit-jupiter:5.7.0")
      }
    }
    val nativeMain by getting
    val nativeTest by getting
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val javadocJar by tasks.registering(Jar::class) {
  dependsOn(tasks.dokkaHtml)
  archiveClassifier.set("javadoc")
  from(tasks.dokkaHtml.get().outputDirectory)
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
