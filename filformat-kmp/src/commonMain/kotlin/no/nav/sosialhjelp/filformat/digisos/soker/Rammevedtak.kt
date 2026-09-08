package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Rammevedtak
 *
 * Rammevedtak
 */
@Serializable
public data class Rammevedtak(
  /**
   * Unik referanse per rammevedtak.
   */
  public val rammevedtaksreferanse: String,
  override val hendelsestidspunkt: String,
  /**
   * Referanse rammevedtaket skal tilknyttes til
   */
  public val saksreferanse: String? = null,
  /**
   * Hva er rammevedtaket for, eks strøm, legeregning
   */
  public val beskrivelse: String? = null,
  /**
   * Hvor mye, i kr
   */
  public val belop: Double? = null,
  /**
   * Utbetalingsperiode (Fra)
   */
  public val fom: String? = null,
  /**
   * Utbetalingsperiode (Til)
   */
  public val tom: String? = null,
  override val type: String = "rammevedtak",
) : Hendelse
