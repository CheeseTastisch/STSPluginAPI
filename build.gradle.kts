plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

group = "me.lian"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.86.1")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.86.1")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

kotlin {
    jvmToolchain(17)
}