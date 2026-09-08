package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.adresse.Adresse

/**
 * Personalia
 */
@Serializable
public data class Personalia(
  public val personIdentifikator: PersonIdentifikator,
  public val navn: Sokernavn,
  public val kontonummer: Kontonummer,
  public val statsborgerskap: Statsborgerskap? = null,
  public val nordiskBorger: NordiskBorger? = null,
  public val telefonnummer: Telefonnummer? = null,
  public val folkeregistrertAdresse: Adresse? = null,
  public val oppholdsadresse: Adresse? = null,
  public val postadresse: Adresse? = null,
  public val fodselsdato: String? = null,
)
