plugins {
  kotlin("multiplatform") version "1.5.0-M2"
}
group = "com.asyncant"
version = "1.0-SNAPSHOT"

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

  nativeTarget.apply {
    binaries {
      executable {
        entryPoint = "main"
      }
    }
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
