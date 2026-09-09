plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup:kotlinpoet:2.2.0")
    implementation(platform("tools.jackson:jackson-bom:3.2.2"))
    implementation("tools.jackson.core:jackson-databind")
}
