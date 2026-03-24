plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.12.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.3"
}

group = "de.mithnar"
version = System.getenv("GITHUB_REF_NAME")?.removePrefix("v") ?: "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Required at compile time: IntelliJ test base classes extend junit.framework.TestCase
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")

    intellijPlatform {
        intellijIdea("2025.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

intellijPlatform {
    buildSearchableOptions = false
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
            untilBuild = provider { null }
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    generateParser {
        sourceFile.set(file("src/main/kotlin/de/mithnar/plugin/asarasm/asar.bnf"))
        pathToParser.set("src/main/java/de/mithnar/plugin/asarasm/parser/AsarParser.java")
        pathToPsiRoot.set("src/main/java/de/mithnar/plugin/asarasm/psi")
        targetRootOutputDir.set(file("build/generated-src"))
        purgeOldFiles.set(true)
    }
    generateLexer {
        sourceFile.set(file("src/main/kotlin/de/mithnar/plugin/asarasm/asar.flex"))
        targetOutputDir.set(file("build/generated-src/de/mithnar/plugin/asarasm"))
        targetFile("AsarLexer")
        purgeOldFiles.set(false)

        dependsOn(generateParser)
    }
    compileKotlin {
        dependsOn(generateLexer)
    }
    test {
        useJUnitPlatform()
        outputs.cacheIf { false }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

sourceSets {
    named("main") {
        java.srcDir("build/generated-src")
    }
}