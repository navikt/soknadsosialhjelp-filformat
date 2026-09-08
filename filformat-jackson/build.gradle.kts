import filformat.codegen.JacksonEmitter
import filformat.codegen.SchemaParser

plugins {
    kotlin("jvm") version "2.4.10"
    `maven-publish`
}

group = "no.nav.sbl.dialogarena"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.22.1"))
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation(platform("tools.jackson:jackson-bom:3.2.2"))
    implementation("tools.jackson.core:jackson-databind")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.3")
    testImplementation("org.assertj:assertj-core:3.27.7")
}

tasks.test {
    useJUnitPlatform()
}

val generatedDir = layout.projectDirectory.dir("src/main/kotlin")

fun regenerate(targetDir: java.io.File) {
    val jsonDir = rootProject.file("json")
    val parser = SchemaParser(jsonDir)
    parser.parseAll()
    JacksonEmitter(parser.model, targetDir).emit()
}

val generateJacksonModel by tasks.registering {
    group = "codegen"
    description = "Regenerates the Jackson model from json/ into src/main/kotlin. Committed to git."
    val jsonDir = rootProject.file("json")
    inputs.dir(jsonDir)
    outputs.dir(generatedDir)
    doLast { regenerate(generatedDir.asFile) }
}

val verifyGeneratedSources by tasks.registering {
    group = "verification"
    description = "Fails if the committed generated sources are out of date with json/."
    val jsonDir = rootProject.file("json")
    inputs.dir(jsonDir)
    doLast {
        val freshDir = layout.buildDirectory.dir("verify-generated-jackson").get().asFile
        freshDir.deleteRecursively()
        regenerate(freshDir)
        val diff = freshDir.walkTopDown().filter { it.isFile }.map { it.relativeTo(freshDir) }.toSet() !=
            generatedDir.asFile.walkTopDown().filter { it.isFile }.map { it.relativeTo(generatedDir.asFile) }.toSet()
        val contentDiff = freshDir.walkTopDown().filter { it.isFile }.any { fresh ->
            val committed = generatedDir.asFile.resolve(fresh.relativeTo(freshDir))
            !committed.exists() || committed.readText() != fresh.readText()
        }
        if (diff || contentDiff) {
            throw GradleException(
                "Generated Jackson sources are out of date. Run ./gradlew :filformat-jackson:generateJacksonModel",
            )
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/soknadsosialhjelp-filformat")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "soknadsosialhjelp-filformat-jackson"
            pom {
                name.set("sosialhjelp-filformat-jackson")
                description.set("Jackson-annotert Kotlin-modell generert fra JSON Schema for søknad om økonomisk sosialhjelp")
                url.set("https://github.com/navikt/soknadsosialhjelp-filformat")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/navikt/soknadsosialhjelp-filformat.git")
                    developerConnection.set("scm:git:https://github.com/navikt/soknadsosialhjelp-filformat.git")
                    url.set("https://github.com/navikt/sosialhjelp-filformat")
                }
            }
        }
    }
}
