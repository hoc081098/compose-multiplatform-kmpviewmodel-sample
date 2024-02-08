plugins {
  alias(libs.plugins.kotlin.multiplatform)
}

@OptIn(
  org
    .jetbrains
    .kotlin
    .gradle
    .ExperimentalKotlinGradlePluginApi::class,
)
kotlin {
  jvmToolchain {
    languageVersion.set(
      JavaLanguageVersion.of(
        libs
          .versions
          .java
          .toolchain
          .get(),
      ),
    )
    vendor.set(JvmVendorSpec.AZUL)
  }

  applyDefaultHierarchyTemplate()

  jvm()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        // Koin
        api(libs.koin.core)

        // Coroutines utils
        api(projects.libraries.coroutinesUtils)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}
