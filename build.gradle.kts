plugins {
    `java-library`
    kotlin("jvm") version "2.4.10"
    kotlin("multiplatform") version "2.4.10" apply false
    kotlin("plugin.serialization") version "2.4.10" apply false
    idea
    `maven-publish`
    id("com.gradleup.shadow") version "9.6.1"
}

group = "no.nav.sbl.dialogarena"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":filformat-jackson"))

    implementation("com.github.java-json-tools:json-schema-validator:2.2.14")
    implementation("com.github.java-json-tools:json-schema-core:1.2.14")
    implementation("com.github.java-json-tools:jackson-coreutils:2.0")
    implementation("com.github.java-json-tools:msg-simple:1.2")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("com.google.guava:guava:33.6.0-jre")
    implementation("org.mozilla:rhino:1.9.1")

    implementation(platform("tools.jackson:jackson-bom:3.2.2"))
    implementation("tools.jackson.core:jackson-databind")
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.22.1"))

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.3")
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

sourceSets {
    main {
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

tasks.processResources {
    dependsOn(copyJsonResources)
}
