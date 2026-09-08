package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Navn

/**
 * Søkers ektefelle
 */
@Serializable
public data class Ektefelle(
  public val navn: Navn,
  public val fodselsdato: String? = null,
  public val personIdentifikator: String? = null,
)
