package no.nav.sosialhjelp.filformat.soknad

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * JSON-formatert søknad om sosialhjelp.
 *
 * Encoding er UTF-8.
 */
@Serializable
public data class Soknad(
  public val version: String,
  /**
   * Inneholder søknadsdataene uten meta- og kompatibilitetsdata.
   */
  public val `data`: Data,
  public val mottaker: Soknadsmottaker,
  /**
   * Inneholder informasjon om status for henting av opplysninger fra andre tjenester.
   */
  public val driftsinformasjon: Driftsinformasjon,
  /**
   * Liste med kompatibilitetstekster
   *
   * Det er et MÅ-krav å vise saksbehandler alle kompatibilitetstekster for versjonen man parser soknads-JSON-en med. Det anbefales å ha et avsnitt (eller tilsvarende) mellom hver enkelt kompatibilitetstekst.
   */
  public val kompatibilitet: List<Kompatibilitet>,
  public val innsendingstidspunkt: String? = null,
)
