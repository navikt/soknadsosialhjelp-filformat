package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Referanse til en fil som har blitt lagret gjennom SvarUt.
 */
@Serializable
public data class SvarUtFilreferanse(
  /**
   * En UUID som identifiserer SvarUt-forsendelsen.
   */
  public val id: String,
  /**
   * Filnummer i SvarUt-forsendelsen.
   */
  public val nr: Int,
  override val type: String = "svarut",
) : Filreferanse
