plugins {
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent
}
