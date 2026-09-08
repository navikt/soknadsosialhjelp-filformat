package no.nav.sbl.soknadsosialhjelp.json

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jackson.NodeType
import com.github.fge.jackson.jsonpointer.JsonPointer
import com.github.fge.jsonschema.cfg.ValidationConfiguration
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.core.keyword.syntax.checkers.SyntaxChecker
import com.github.fge.jsonschema.core.report.LogLevel
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.core.tree.SchemaTree
import com.github.fge.jsonschema.library.*
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.msgsimple.bundle.MessageBundle
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.EnumSet

object JsonSosialhjelpValidator {
    @JvmStatic fun main(args: Array<String>) {
        if (args.size != 2) {
            println("Bruk: java -jar soknadsosialhjelp-filformat-...-shaded.jar [ --soknad SOKNAD_JSON | --vedlegg VEDLEGG_JSON | SCHEMA JSON ]")
            println("\n--soknad     - Valider med soknad-skjemaet.\n--vedlegg    - Valider med vedlegg-skjemaet.\n\nSOKNAD_JSON  - En JSON-fil med som skal følge soknad-formatet.\nVEDLEGG_JSON - En JSON-fil med som skal følge vedlegg-formatet.\nSCHEMA       - Et av skjemaene som følger med. F.eks.: \"/json/soknad/parts/version.json\".\nJSON         - En JSON-fil med som skal følge angitt format.")
            System.exit(1)
        }
        try {
            val schemaUri = when (args[0]) { "--soknad" -> toSkjemaUri("/json/soknad/soknad.json"); "--vedlegg" -> toSkjemaUri("/json/vedlegg/vedleggSpesifikasjon.json"); "--internal" -> toSkjemaUri("/json/internal/internalSoknad.json"); else -> toSkjemaUri(args[0]) }
            ensureValid(Files.readAllLines(Paths.get(args[1])).joinToString(""), schemaUri)
        } catch (e: Exception) { System.err.println("Exception: $e"); System.exit(1) }
    }
    @JvmStatic fun ensureValidSoknad(json: String) = ensureValid(json, toSkjemaUri("/json/soknad/soknad.json"))
    @JvmStatic fun ensureValidInternalSoknad(json: String) = ensureValid(json, toSkjemaUri("/json/internal/internalSoknad.json"))
    @JvmStatic fun ensureValidVedlegg(json: String) = ensureValid(json, toSkjemaUri("/json/vedlegg/vedleggSpesifikasjon.json"))
    @JvmStatic fun ensureValidInnsyn(json: String) = ensureValid(json, toSkjemaUri("/json/digisos/soker/digisos-soker.json"))
    @JvmStatic fun ensureValid(json: String, schemaUri: String) {
        try { validate(JsonLoader.fromString(json), schemaUri).also { if (!it.isSuccess || hasWarnings(it)) throw JsonSosialhjelpValidationException(it) } } catch (e: IOException) { throw RuntimeException(e) }
    }
    @JvmStatic fun validateFile(testfile: File, schemaUri: String): ProcessingReport = try { validate(JsonLoader.fromFile(testfile), schemaUri) } catch (e: IOException) { throw RuntimeException(e) }
    @JvmStatic fun hasWarnings(report: ProcessingReport): Boolean = report.any { it.logLevel == LogLevel.WARNING }
    private fun validate(json: JsonNode, schemaUri: String): ProcessingReport = try { createValidator(schemaUri).validate(json) } catch (e: ProcessingException) { throw RuntimeException(e) }
    private fun toSkjemaUri(skjemaFil: String): String = try { JsonSosialhjelpValidator::class.java.getResource(skjemaFil).toURI().toString() } catch (e: Exception) { throw RuntimeException(e) }
    private fun createValidator(schemaUri: String): JsonSchema = try { JsonSchemaFactory.newBuilder().setValidationConfiguration(createValidationConfiguration()).freeze().getJsonSchema(schemaUri) } catch (e: ProcessingException) { throw RuntimeException(e) }
    private fun createValidationConfiguration(): ValidationConfiguration = ValidationConfiguration.newBuilder().setDefaultLibrary("http://json-schema.org/draft-06/schema", withIgnoredKeywords(DraftV4Library.get(), listOf("javaType", "extends"))).freeze()
    private fun withIgnoredKeywords(library: Library, keywords: List<String>): Library = library.thaw().also { builder -> keywords.forEach { keyword -> builder.addKeyword(Keyword.newBuilder(keyword).withSyntaxChecker(IgnoreSyntaxCheck()).freeze()) } }.freeze()
    private class IgnoreSyntaxCheck : SyntaxChecker {
        override fun getValidTypes(): EnumSet<NodeType>? = null
        override fun checkSyntax(pointers: Collection<JsonPointer>, bundle: MessageBundle, report: ProcessingReport, tree: SchemaTree) = Unit
    }
}
