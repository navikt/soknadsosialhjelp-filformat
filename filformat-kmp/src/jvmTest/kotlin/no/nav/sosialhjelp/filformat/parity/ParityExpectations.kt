package no.nav.sosialhjelp.filformat.parity

import no.nav.sosialhjelp.filformat.parity.Expectation.MUST_THROW
import no.nav.sosialhjelp.filformat.parity.Expectation.PARITY
import no.nav.sosialhjelp.filformat.parity.Expectation.TOLERATED

/**
 * Per-fixture expectations for the KMP model.
 *
 * =====================================================================================
 * READ THIS BEFORE CHANGING THE TABLE
 * =====================================================================================
 * Every entry here is a judgement call encoded as data. The root project only asserts
 * "this file passes/fails JSON-schema validation"; this table asserts the stricter and
 * different question of what the KMP *deserializer* does with it.
 *
 * The outcomes:
 *
 *   PARITY      valid file. Must deserialize, agree with the Java model, and round-trip.
 *   PARITY_UKJENTE_FELTER_FORKASTES
 *               as PARITY, but the file deliberately carries fields outside the schema, so
 *               the round-trip is only asserted to be a subset of the input.
 *   MUST_THROW  invalid in a way the deserializer can see: a missing required field, or a
 *               value of the wrong JSON type.
 *   TOLERATED   invalid only in a way the deserializer cannot see: `pattern` violations,
 *               and unknown enum / hendelse-type values that we deliberately absorb.
 *
 * Moving a file from MUST_THROW to TOLERATED weakens the guarantee. Do not do it to make a
 * red build green; work out why the deserializer stopped rejecting it first.
 *
 * Any fixture not listed here defaults to PARITY, and
 * `ParityTest.triage-tabellen refererer bare til fixtures som finnes` fails if the table
 * references a file that no longer exists -- so a renamed or deleted fixture surfaces
 * as a test failure rather than as silently dropped coverage.
 */
internal object ParityExpectations {

    val byPath: Map<String, Expected> = buildMap {
        // ---------------------------------------------------------------------------
        // digisos/soker -- root documents (DigisosSoker)
        // ---------------------------------------------------------------------------
        throws("digisos/soker/feil-mangler-avsender.json", "required 'avsender' absent")
        throws("digisos/soker/feil-mangler-hendelser.json", "required 'hendelser' absent")
        throws("digisos/soker/feil-mangler-version.json", "required 'version' absent")

        // ---------------------------------------------------------------------------
        // parts/avsender (Avsender)
        // ---------------------------------------------------------------------------
        throws("digisos/soker/parts/avsender/feil-mangler-systemnavn.json", "required 'systemnavn' absent")
        throws("digisos/soker/parts/avsender/feil-mangler-systemversjon.json", "required 'systemversjon' absent")

        // ---------------------------------------------------------------------------
        // parts/hendelse (Hendelse)
        // ---------------------------------------------------------------------------
        tolerated(
            "digisos/soker/parts/hendelse/feil-hendelse-eksisterer-ikke.json",
            "type 'foobar' is unknown -> UkjentHendelse. This is the whole point of the " +
                "fallback: a new municipal hendelse type must not take down a case view. " +
                "Asserted in detail by ToleranseTest.'ukjent hendelsestype gir " +
                "UkjentHendelse i stedet for aa kaste'.",
        )
        throws("digisos/soker/parts/hendelse/feil-hendelse-er-tom.json", "empty object: 'type' and 'hendelsestidspunkt' both absent")
        throws("digisos/soker/parts/hendelse/feil-hendelse-mangler-hendelsestidspunkt.json", "required 'hendelsestidspunkt' absent")

        // parts/hendelse/soknadsStatus
        tolerated(
            "digisos/soker/parts/hendelse/soknadsStatus/feil-status-eksisterer-ikke.json",
            "status 'FOOBAR' is not in the schema enum -> SoknadsStatus.Status.UKJENT. " +
                "soknadsStatus.json states new statuses may be added at any time.",
        )
        throws("digisos/soker/parts/hendelse/soknadsStatus/feil-status-mangler.json", "required 'status' absent")

        // parts/hendelse/tildeltNavKontor
        //
        // navKontor is `"pattern": "^[0-9]{4}$"`. kotlinx.serialization does not evaluate
        // JSON-schema patterns, and neither does the Java model -- only
        // JsonSosialhjelpValidator does, and the root project already covers that. These
        // three files are therefore valid *documents* with an out-of-spec string value.
        tolerated("digisos/soker/parts/hendelse/tildeltNavKontor/feil-navKontor-har-tre-siffer.json", "pattern-only violation, not visible to a deserializer")
        tolerated("digisos/soker/parts/hendelse/tildeltNavKontor/feil-navKontor-har-fem-siffer.json", "pattern-only violation, not visible to a deserializer")
        tolerated("digisos/soker/parts/hendelse/tildeltNavKontor/feil-navKontor-har-seks-siffer.json", "pattern-only violation, not visible to a deserializer")
        throws("digisos/soker/parts/hendelse/tildeltNavKontor/feil-navKontor-mangler.json", "required 'navKontor' absent")

        // parts/hendelse/vedtakFattet
        throws("digisos/soker/parts/hendelse/vedtakFattet/feil-mangler-vedtaksfil.json", "required 'vedtaksfil' absent")
        throws("digisos/soker/parts/hendelse/vedtakFattet/feil-mangler-vedtaksfil-referanse.json", "required 'vedtaksfil.referanse' absent")
        throws("digisos/soker/parts/hendelse/vedtakFattet/feil-mangler-vedlegg-referanse.json", "required 'vedlegg[].referanse' absent")
        throws("digisos/soker/parts/hendelse/vedtakFattet/feil-mangler-vedlegg-tittel.json", "required 'vedlegg[].tittel' absent")

        // parts/hendelse/dokumentasjonEtterspurt
        throws("digisos/soker/parts/hendelse/dokumentasjonEtterspurt/feil-dokument-mangler-dokumenttype.json", "required 'dokumenter[].dokumenttype' absent")
        throws("digisos/soker/parts/hendelse/dokumentasjonEtterspurt/feil-dokument-mangler-innsendelsesfrist.json", "required 'dokumenter[].innsendelsesfrist' absent")
        throws(
            "digisos/soker/parts/hendelse/dokumentasjonEtterspurt/feil-dokument-har-feil-dokumentreferanse.json",
            "'dokumentreferanse' is the number 34 where the schema says string -- a JSON " +
                "type mismatch, which a deserializer does see",
        )
        throws("digisos/soker/parts/hendelse/dokumentasjonEtterspurt/feil-mangler-forvaltningsbrev-referanse.json", "required 'forvaltningsbrev.referanse' absent")
        throws("digisos/soker/parts/hendelse/dokumentasjonEtterspurt/feil-mangler-vedlegg-tittel.json", "required 'vedlegg[].tittel' absent")

        // parts/hendelse/forelopigSvar
        throws("digisos/soker/parts/hendelse/forelopigSvar/feil-mangler-forvaltningsbrev.json", "required 'forvaltningsbrev' absent")
        throws("digisos/soker/parts/hendelse/forelopigSvar/feil-mangler-forvaltningsbrev-referanse.json", "required 'forvaltningsbrev.referanse' absent")
        throws("digisos/soker/parts/hendelse/forelopigSvar/feil-mangler-vedlegg-tittel.json", "required 'vedlegg[].tittel' absent")

        // ---------------------------------------------------------------------------
        // vedlegg (VedleggSpesifikasjon)
        //
        // Note the different naming convention: `ikkegyldig_`, not `feil-`.
        // ---------------------------------------------------------------------------
        throws("vedlegg/ikkegyldig_ugyldigvedlegg.json", "'vedlegg' is an array of strings where the schema says objects")
        throws("vedlegg/ikkegyldig_ugyldigfil.json", "'filer' is an array of strings where the schema says objects")
        tolerated(
            "vedlegg/ikkegyldig_ugyldigHendelseType.json",
            "hendelseType 'annet' is not in the schema enum -> Vedlegg.HendelseType.UKJENT",
        )

        // ---------------------------------------------------------------------------
        // Forward-compatibility fixtures
        //
        // These two exist specifically to carry fields that are not in the schema. The KMP
        // model discards unknown keys, so they cannot round-trip exactly. Parity against the
        // Java model is still asserted in full. Asserted in detail by
        // `ToleranseTest.nye felter tolereres`.
        // ---------------------------------------------------------------------------
        unknownFieldsDropped("digisos/soker/kan-ha-nye-felter.json", "carries 'tullenavn' and 'tullenavn2'")
        unknownFieldsDropped("vedlegg/gyldig_ekstrafelter.json", "carries 'garbage*' keys at three nesting levels")
    }

    fun forPath(path: String): Expected = byPath[path] ?: Expected(PARITY)

    private fun MutableMap<String, Expected>.throws(path: String, reason: String) {
        put(path, Expected(MUST_THROW, reason))
    }

    private fun MutableMap<String, Expected>.tolerated(path: String, reason: String) {
        put(path, Expected(TOLERATED, reason))
    }

    private fun MutableMap<String, Expected>.unknownFieldsDropped(path: String, reason: String) {
        put(path, Expected(Expectation.PARITY_UKJENTE_FELTER_FORKASTES, reason))
    }
}
