import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object TestDataFiles {
    fun list(config: Config): List<Array<Any>> = allFilesRecursively(Paths.get(config.testDataDirectory).toFile())
        .filter { it.name.endsWith(".json") && !it.name.startsWith("_") }
        .map { file -> arrayOf(file.name, TestData(file, determineSchemaUri(config, file.toPath()), !file.name.startsWith("feil-"))) }

    private fun allFilesRecursively(parent: File): List<File> = if (parent.isDirectory) parent.listFiles()!!.flatMap(::allFilesRecursively) else listOf(parent)

    private fun determineSchemaUri(config: Config, testdataFile: Path): String {
        val path = Paths.get(config.testDataDirectory).relativize(testdataFile).parent
        if (path == null) return Paths.get(config.toplevelSchemaFile).toUri().toString()
        val schema = Paths.get(config.schemaDirectory, path.parent?.toString() ?: "", "${path.fileName}.json")
        check(Files.exists(schema)) { "Kunne ikke finne skjemafil med navn: $schema" }
        return schema.toUri().toString()
    }

    class Config(val schemaDirectory: String, val toplevelSchemaFilename: String?, val testDataDirectory: String) {
        val toplevelSchemaFile get() = "$schemaDirectory/$toplevelSchemaFilename"
    }
}
