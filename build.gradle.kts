plugins {
    kotlin("jvm") version "2.1.20-Beta2"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

group = "pl.syntaxdevteam"
version = "1.0-SNAPSHOT"
description = "TagsX"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.19.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.19.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.19.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version, "description" to description)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}