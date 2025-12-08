import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val appVersion = "2.3.3"

group = "com.njpg"
version = appVersion

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation(compose.material3)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            modules("java.base", "java.desktop", "jdk.unsupported")
            targetFormats(TargetFormat.Msi, TargetFormat.Exe)
            packageName = "Exam"
            packageVersion = appVersion
        }
    }
}
