package no.nav.sosialhjelp.filformat.digisos.soker.hendelse

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Dokument
 */
@Serializable
public data class Dokumenter(
  /**
   * Dokumenttype
   *
   * En predefinert eller fritekstbasert dokumenttype som veileder etterspør fra søker, eks: "strømfaktura" eller "kontoutskrift"
   */
  public val dokumenttype: String,
  public val innsendelsesfrist: String,
  /**
   * Tilleggsinformasjon
   *
   * Tilleggsinformasjon til dokumenttypen veileder etterspør
   */
  public val tilleggsinformasjon: String? = null,
  /**
   * Dokumentreferanse
   *
   * Referansen til dette dokumentet som etterspørres. Vil bli sendt tilbake i vedlegg.json
   */
  public val dokumentreferanse: String? = null,
)
