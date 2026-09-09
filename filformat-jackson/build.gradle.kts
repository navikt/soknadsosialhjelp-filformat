import filformat.codegen.JacksonEmitter
import filformat.codegen.SchemaParser

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.sbl.dialogarena"
version = rootProject.version

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

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

val generatedDir = layout.buildDirectory.dir("generated/sources/filformat/main/kotlin")

fun regenerate(targetDir: File) {
    val jsonDir = rootProject.file("json")
    val parser = SchemaParser(jsonDir)
    parser.parseAll()
    JacksonEmitter(parser.model, targetDir).emit()
}

val generateJacksonModel = tasks.register("generateJacksonModel") {
    group = "codegen"
    description = "Generates the Jackson model from json/."
    val jsonDir = rootProject.file("json")
    inputs.dir(jsonDir)
    outputs.dir(generatedDir)
    doLast { regenerate(generatedDir.get().asFile) }
}

sourceSets.main {
    kotlin.srcDir(generateJacksonModel)
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
