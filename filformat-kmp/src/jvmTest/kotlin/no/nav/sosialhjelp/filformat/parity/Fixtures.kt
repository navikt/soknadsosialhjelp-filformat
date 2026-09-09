package no.nav.sosialhjelp.filformat.parity

import java.io.File

internal object Fixtures {

    val root: File by lazy {
        val path = System.getProperty("filformat.fixtures")
            ?: error(
                "System property 'filformat.fixtures' is not set. It is configured on the " +
                    "jvmTest task in filformat-kmp/build.gradle.kts.",
            )
        File(path).also {
            check(it.isDirectory) { "Fixture directory does not exist: $it" }
        }
    }
}
