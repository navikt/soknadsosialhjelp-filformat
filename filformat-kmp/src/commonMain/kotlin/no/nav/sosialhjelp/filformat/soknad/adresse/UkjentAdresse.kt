package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable(with = UkjentAdresseSerializer::class)
public data class UkjentAdresse(
  override val kilde: Kilde,
  override val type: String,
  override val adresseValg: AdresseValg?,
  public val raw: JsonObject,
) : Adresse
