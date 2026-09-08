package no.nav.sosialhjelp.filformat.soknad.situasjonendring

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Endring i situasjon
 */
@Serializable
public data class Situasjonendring(
  public val kilde: KildeBruker,
  /**
   * Brukers svar på om noe har endret seg siden forrige søknad (gjelder kort søknad).
   */
  public val harNoeEndretSeg: Boolean,
  /**
   * Brukerskrevet forklaring på hva som har endret seg siden forrige søknad (gjelder kort søknad).
   */
  public val hvaHarEndretSeg: String? = null,
)
