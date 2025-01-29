import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlin.kover)
  id("maven-publish")
}

group = "com.sherlockblue.kmpble"
version = "0.1.0"

publishing {
  repositories {
    mavenLocal()
  }
}

kotlin {

  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  compilerOptions {
    // Common compiler options applied to all Kotlin source sets
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }

  androidTarget {
    publishLibraryVariants("release")
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_1_8)
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
  }
  iosX64()
  iosArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test) // org.junit imports only work if this is in commonTest
      implementation(libs.kotlinx.coroutines.test)
    }
    androidMain.dependencies {
      // Production
      implementation(libs.androidx.activity.compose)
      implementation(libs.kotlinx.coroutines.core)

      // Unit Testing
      implementation(libs.kotlinx.coroutines.test)
      implementation(libs.mockk)
    }
  }
}

android {
  namespace = "com.sherlockblue.KMPBLE"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  // //// THIS SOLVES META-INF CONFLICT ERRORS IN KMP //////////
  packagingOptions {
    exclude("META-INF/DEPENDENCIES.md")
    exclude("META-INF/LICENSE.md")
    exclude("META-INF/LICENSE-notice.md")
    exclude("META-INF/NOTICE.md")
    exclude("META-INF/DEPENDENCIES.txt")
    exclude("META-INF/NOTICE.txt")
    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/LICENSE-notice.txt")
    exclude("META-INF/androidx.vectordrawable_vectordrawable.version")
    exclude("META-INF/androidx.loader_loader.version")
    exclude("META-INF/androidx.lifecycle_lifecycle-livedata.version")
    exclude("META-INF/androidx.lifecycle_lifecycle-livedata.version")
    exclude("kotlin/annotation/annotation.kotlin_builtins")
    exclude("META-INF/androidx.lifecycle_lifecycle-runtime.version")
  }
}

kover {
  reports {
    verify {
      rule {
        minBound(100)
      }
    }
    filters {
      excludes {
        packages("*.generated.*")
        packages("*.extensions")
      }
    }
  }
}
