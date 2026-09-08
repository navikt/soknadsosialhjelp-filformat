package no.nav.sosialhjelp.filformat.vedlegg

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Filer(
  public val filnavn: String? = null,
  public val sha512: String? = null,
)
