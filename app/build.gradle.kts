plugins {
    alias(libs.plugins.kotlin.jvm)

    application
}

repositories { mavenCentral() }

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.3")
    // implementation("com.github.ajalt.mordant:mordant:3.0.2")
    // implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(17) } }

application { mainClass = "com.gitlab.notscripter.yah.MainKt" }
