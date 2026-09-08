package no.nav.sosialhjelp.filformat.klage

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.Soknadsmottaker
import no.nav.sosialhjelp.filformat.soknad.personalia.PersonIdentifikator
import no.nav.sosialhjelp.filformat.soknad.personalia.Sokernavn

/**
 * Json-struktur for Klage på Vedtak
 *
 * Encoding er UTF-8.
 */
@Serializable
public data class Klage(
  /**
   * Unik identifikator for klagen (UUID)
   */
  public val klageId: String,
  /**
   * Referanse til vedtak som det klages på (UUID)
   */
  public val vedtakId: String,
  /**
   * Referanse til original søknad (UUID)
   */
  public val digisosId: String,
  public val personIdentifikator: PersonIdentifikator,
  public val navn: Sokernavn,
  public val mottaker: Soknadsmottaker,
  /**
   * Autentiseringsinformasjon
   */
  public val autentisering: Autentisering,
  public val innsendingstidspunkt: String,
  /**
   * Begrunnelse for klagen
   */
  public val begrunnelse: Begrunnelse? = null,
)
