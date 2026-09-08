package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Forsørgerplikt
 */
@Serializable
public data class Forsorgerplikt(
  public val harForsorgerplikt: HarForsorgerplikt? = null,
  public val barnebidrag: Barnebidrag? = null,
  public val ansvar: List<Ansvar>? = null,
)
