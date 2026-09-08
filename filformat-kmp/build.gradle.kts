import filformat.codegen.KotlinxEmitter
import filformat.codegen.SchemaParser

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
    // Kotlin/JS has no built-in npm publish task. Verified working on Gradle 9.6.1 +
    // Kotlin 2.4.10 despite the plugin predating both.
    id("dev.petuska.npm.publish") version "3.5.3"
}

// GOTCHA: the release workflow (.github/workflows/releaseGithub.yml) sets the
// version by running `sed` on the ROOT build.gradle.kts only. Inheriting group/version
// from the root project is what makes this subproject pick up the released version.
// Do not hardcode a version here.
group = "no.nav.sosialhjelp.filformat"
version = rootProject.version

repositories {
    mavenCentral()
}

val kotlinxGeneratedDir = layout.buildDirectory.dir("generated/sources/filformat/commonMain/kotlin")

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    // Node only, not browser: the JS consumer is sosialhjelp-adminpanel, which folds the
    // hendelse stream server-side in Next.js.
    js {
        nodejs()
        binaries.library()
        generateTypeScriptDefinitions()
    }

    // The model is public API for two ecosystems, so accidental API changes should be a
    // compile error rather than a review miss.
    explicitApi()

    sourceSets {
        commonMain {
            kotlin.srcDir(kotlinxGeneratedDir)
        }
        commonMain.dependencies {
            api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmTest.dependencies {
            implementation("org.assertj:assertj-core:3.27.7")
            implementation("org.junit.jupiter:junit-jupiter:6.1.2")
            runtimeOnly("org.junit.platform:junit-platform-launcher:6.1.2")
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
    // Fixtures live in the ROOT project. Pass their location explicitly rather than
    // relying on the working directory, which differs between Gradle and IDE runs.
    systemProperty("filformat.fixtures", rootProject.file("src/test/resources/json").absolutePath)
}

fun regenerateKotlinx(targetDir: java.io.File) {
    val jsonDir = rootProject.file("json")
    val parser = SchemaParser(jsonDir)
    parser.parseAll()
    KotlinxEmitter(parser.model, targetDir).emit()
}

val generateKotlinxModel by tasks.registering {
    group = "codegen"
    description = "Generates the kotlinx.serialization model from json/."
    val jsonDir = rootProject.file("json")
    inputs.dir(jsonDir)
    outputs.dir(kotlinxGeneratedDir)
    doLast { regenerateKotlinx(kotlinxGeneratedDir.get().asFile) }
}

tasks.matching { it.name.contains("Kotlin") && it.name.startsWith("compile") }.configureEach {
    dependsOn(generateKotlinxModel)
}

// --- Maven (JVM + Gradle module metadata) --------------------------------------------
//
// Consumed by sosialhjelp-innsyn-api and sosialhjelp-modia-api. Publishes to the same
// GitHub Packages Maven repository as the root project.
//
// GOTCHA: Gradle repositories are declared PER PROJECT and are not inherited from the
// root build. This block is not a duplicate that can be deleted -- without it this
// module has no Maven repository to publish to.
//
// The repository name must stay "GitHubPackages" to match the root project, so that
// `./gradlew publishAllPublicationsToGitHubPackagesRepository` publishes both modules
// in one invocation. See .github/workflows/releaseGithub.yml.
//
// No signing: GitHub Packages does not require GPG signatures. (Publishing to Maven
// Central, which would, was removed -- it had been broken since 2025-03-26.)
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

    // The Kotlin Multiplatform plugin creates the publications ("kotlinMultiplatform",
    // "jvm", "js") and derives their artifactIds from the Gradle project name, which is
    // the directory name "filformat-kmp". That is too generic for a coordinate published
    // alongside soknadsosialhjelp-filformat, so rebase it here.
    //
    // Rewriting the artifactId rather than assigning it preserves the per-target suffixes
    // the KMP plugin appends:
    //   soknadsosialhjelp-filformat-kmp        (metadata / common)
    //   soknadsosialhjelp-filformat-kmp-jvm
    //   soknadsosialhjelp-filformat-kmp-js
    //
    // Same trap as `rootProject.name` in settings.gradle.kts: this is load-bearing for
    // consumers. Once released, changing it breaks every dependency declaration.
    publications.withType<MavenPublication>().configureEach {
        artifactId = artifactId.replace(project.name, "soknadsosialhjelp-filformat-kmp")

        pom {
            name.set("sosialhjelp-filformat-kmp")
            description.set(
                "Kotlin Multiplatform-modell for digisos/soker-hendelser i søknad om " +
                    "økonomisk sosialhjelp",
            )
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
                url.set("https://github.com/navikt/soknadsosialhjelp-filformat")
            }
        }
    }
}

// --- npm -----------------------------------------------------------------------------
//
// Publishes the Kotlin/JS library to the GitHub Packages npm registry for
// sosialhjelp-adminpanel (Next.js, server-side).
//
// Credentials come from the environment, same as the Maven publication above.
// In GitHub Actions, NPM_AUTH_TOKEN should be secrets.GITHUB_TOKEN.
//
// GOTCHA: this plugin wires `publishJsPackageToGithubPackagesRegistry` into the standard
// `publish` lifecycle task. So plain `./gradlew publish` attempts an npm publish and
// fails if NPM_AUTH_TOKEN is unset. releaseGithub.yml therefore invokes the Maven and npm
// publish tasks by name in separate steps instead of using `publish`, so that a failure
// in the npm registry cannot block the Java artifact that existing consumers depend on.
npmPublish {
    packages {
        named("js") {
            packageName.set("soknadsosialhjelp-filformat")
            // GitHub Packages requires the npm scope to match the owning organisation.
            scope.set("navikt")
        }
    }
    registries {
        register("githubPackages") {
            uri.set("https://npm.pkg.github.com")
            authToken.set(providers.environmentVariable("NPM_AUTH_TOKEN"))
        }
    }
}
