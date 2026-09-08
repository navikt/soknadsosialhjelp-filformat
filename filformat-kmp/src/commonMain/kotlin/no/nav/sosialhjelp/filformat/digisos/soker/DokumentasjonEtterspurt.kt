package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.digisos.soker.hendelse.Dokumenter

/**
 * Veileder ber om mer dokumentasjon fra søker
 */
@Serializable
public data class DokumentasjonEtterspurt(
  /**
   * Liste over dokumentene etterspurt
   */
  public val dokumenter: List<Dokumenter>,
  override val hendelsestidspunkt: String,
  public val forvaltningsbrev: Forvaltningsbrev? = null,
  /**
   * Vedlegg til forvaltningsbrev
   *
   * En liste med vedlegg til forvaltningsbrevet som søker skal ha mulighet til å se. Det er ingen garanti for at filene blir vist til søker. Filformatet skal være PDF.
   */
  public val vedlegg: List<Vedlegg>? = null,
  override val type: String = "dokumentasjonEtterspurt",
) : Hendelse
