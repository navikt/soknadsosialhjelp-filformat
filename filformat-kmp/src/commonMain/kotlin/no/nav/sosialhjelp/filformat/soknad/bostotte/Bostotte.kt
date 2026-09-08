package no.nav.sosialhjelp.filformat.soknad.bostotte

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Bostøtte informasjon om bruker.
 */
@Serializable
public data class Bostotte(
  public val saker: List<BostotteSak>? = null,
)
