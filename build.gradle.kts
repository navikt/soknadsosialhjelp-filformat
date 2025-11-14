plugins {
    java
    idea
    `maven-publish`
    signing
    id("org.jsonschema2pojo") version "1.2.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("org.mozilla:rhino:1.7.14")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

// Task to copy JSON resources to build directory
val copyJsonResources by tasks.registering(Copy::class) {
    from("json")
    into(layout.buildDirectory.dir("json"))
    exclude("**/*.java")  // Exclude any Java files that shouldn't be there
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
    targetPackage = "no.nav.sosialhjelp.soknad"
    classNamePrefix = "Json"
    generateBuilders = true
    includeAdditionalProperties = false
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
            srcDir("json")
        }
    }
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
        attributes["Main-Class"] = "no.nav.sosialhjelp.soknad.json.JsonSosialhjelpValidator"
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

