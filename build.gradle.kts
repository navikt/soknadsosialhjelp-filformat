plugins {
    java
    idea
    `maven-publish`
    signing
    id("org.jsonschema2pojo") version "1.2.2"
    id("com.gradleup.shadow") version "9.2.2"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

buildscript {
    configurations {
        classpath {
            resolutionStrategy {
                // Force pga. vulnerabilities i org.jsonschema2pojo-plugin
                force("org.yaml:snakeyaml:2.5")
                force("commons-io:commons-io:2.21.0")
                force("com.fasterxml.jackson.core:jackson-core:2.21.0")
                force("org.apache.commons:commons-lang3:3.20.0")
            }
        }
    }
}

group = "no.nav.sbl.dialogarena"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.java-json-tools:json-schema-validator:2.2.14")
    implementation("com.github.java-json-tools:json-schema-core:1.2.14")
    implementation("com.github.java-json-tools:jackson-coreutils:2.0")
    implementation("com.github.java-json-tools:msg-simple:1.2")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("org.mozilla:rhino:1.9.0")

    implementation("tools.jackson.core:jackson-databind:3.0.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.21")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
    testImplementation("org.assertj:assertj-core:3.27.7")
}

tasks.test {
    useJUnitPlatform()
}

// Task to copy JSON resources to build directory
val copyJsonResources by tasks.registering(Copy::class) {
    from("json")
    into(layout.buildDirectory.dir("json"))
}

// Task to replace tokens in JSON files
val replaceTokensInJson by tasks.registering {
    dependsOn(copyJsonResources)
    doLast {
        val buildDir = layout.buildDirectory.get().asFile
        fileTree("$buildDir/json") {
            include("**/*.json")
        }.forEach { file ->
            var content = file.readText()
            content = content.replace("ONLY_CODEGEN\$ref", "\$ref")
            file.writeText(content)
        }
    }
}

// Configure jsonschema2pojo
configure<org.jsonschema2pojo.gradle.JsonSchemaExtension> {
    setSource(files(layout.buildDirectory.dir("json")))
    targetDirectory = layout.buildDirectory.dir("generated-sources/jsonschema2pojo").get().asFile
    targetPackage = "no.nav.sbl.soknadsosialhjelp"
    classNamePrefix = "Json"
    generateBuilders = true
    includeAdditionalProperties = true
    isSerializable = true
}

// Make sure JSON is processed before code generation
tasks.named("generateJsonSchema2Pojo") {
    dependsOn(replaceTokensInJson)
}

// Add generated sources to the main source set
sourceSets {
    main {
        java {
            srcDir("src/main/java")
            srcDir(layout.buildDirectory.dir("generated-sources/jsonschema2pojo"))
        }
        resources {
            srcDir("xsd")
        }
    }
}

// Task to copy JSON schemas to resources with proper structure
val copyJsonToResources by tasks.registering(Copy::class) {
    dependsOn(replaceTokensInJson)
    from(layout.buildDirectory.dir("json"))
    into(layout.buildDirectory.dir("resources/main/json"))
}

// Make processResources depend on copying JSON files
tasks.named("processResources") {
    dependsOn(copyJsonToResources)
}

// Configure IDEA to recognize generated sources
idea {
    module {
        val generatedSourcesDir = layout.buildDirectory.dir("generated-sources/jsonschema2pojo").get().asFile
        sourceDirs.add(generatedSourcesDir)
        generatedSourceDirs.add(generatedSourcesDir)
    }
}

// Convenient task to generate sources and update IDE config
val generateAndConfigureIDE by tasks.registering {
    group = "IDE"
    description = "Generates sources from JSON schemas and updates IntelliJ IDEA configuration"
    dependsOn(tasks.named("generateJsonSchema2Pojo"), tasks.named("ideaModule"))
    doLast {
        println("")
        println("═══════════════════════════════════════════════════════════════")
        println("  Sources generated successfully!")
        println("")
        println("  Next step: Refresh Gradle project in IntelliJ IDEA")
        println("  → Right-click project → Gradle → Refresh Gradle Project")
        println("  → Or press: Ctrl+Shift+O (Windows) / Cmd+Shift+I (Mac)")
        println("═══════════════════════════════════════════════════════════════")
        println("")
    }
}

// Configure shadow jar (shaded jar with dependencies)
tasks.shadowJar {
    archiveClassifier.set("shaded")
    manifest {
        attributes["Main-Class"] = "no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator"
    }
}

// Configure javadoc
tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
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
        create<MavenPublication>("myPublicationName") {
            from(components["java"])

            pom {
                name.set("sosialhjelp-filformat")
                description.set("Json Schema for søknad om økonomisk sosialhjelp")
                url.set("https://github.com/navikt/sosialhjelp-filformat")
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

// Build task dependencies
tasks.compileJava {
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
}
tasks.processResources {
    dependsOn(copyJsonResources)
}
tasks.named("sourcesJar") {
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
}
tasks.named("javadocJar") {
    dependsOn(tasks.named("generateJsonSchema2Pojo"))
}

