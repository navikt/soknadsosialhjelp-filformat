package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable(with = AdresseValgSerializer::class)
public enum class AdresseValg(
  public val jsonValue: String,
) {
  FOLKEREGISTRERT("folkeregistrert"),
  MIDLERTIDIG("midlertidig"),
  SOKNAD("soknad"),
  UKJENT("UKJENT"),
  ;
}

public object AdresseValgSerializer : UkjentTolerantEnumSerializer<AdresseValg>("AdresseValg", AdresseValg.entries.toTypedArray(), AdresseValg.UKJENT, AdresseValg::jsonValue)
