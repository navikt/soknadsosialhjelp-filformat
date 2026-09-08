package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir ustrukturert adresse (liste med tekststrenger).
 */
@Serializable
public data class UstrukturertAdresse(
  override val kilde: Kilde,
  public val adresse: List<String>? = null,
  override val adresseValg: AdresseValg? = null,
  override val type: String = "ustrukturert",
) : Adresse
