// NB: rootProject.name MUST be set explicitly here.
//
// Previously this was an empty settings.gradle, so Gradle derived the root project
// name from the directory name -- which happened to match. The published artifactId
// comes from project.name (build.gradle.kts does not set artifactId on the publication),
// so if this line is removed or changed, the artifactId silently changes and every
// consumer of no.nav.sbl.dialogarena:soknadsosialhjelp-filformat breaks.
//
// The `rootProject.name` entry in gradle.properties is a no-op -- Gradle cannot set
// the project name from gradle.properties. Do not rely on it.
rootProject.name = "soknadsosialhjelp-filformat"

include(":filformat-kmp")
