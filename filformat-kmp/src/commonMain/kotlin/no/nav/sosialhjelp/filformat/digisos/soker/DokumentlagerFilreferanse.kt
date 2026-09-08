package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Referanse til en fil som ligger lagret i Dokumentlager.
 */
@Serializable
public data class DokumentlagerFilreferanse(
  /**
   * En UUID som identifiserer dokumentet i Dokumentlager.
   */
  public val id: String,
  override val type: String = "dokumentlager",
) : Filreferanse
