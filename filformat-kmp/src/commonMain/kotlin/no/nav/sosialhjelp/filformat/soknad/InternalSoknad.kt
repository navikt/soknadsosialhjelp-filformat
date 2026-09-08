package no.nav.sosialhjelp.filformat.soknad

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.`internal`.Soknadsmottaker
import no.nav.sosialhjelp.filformat.soknad.adresse.Adresse
import no.nav.sosialhjelp.filformat.vedlegg.VedleggSpesifikasjon

@Serializable
public data class InternalSoknad(
  public val soknad: Soknad? = null,
  public val vedlegg: VedleggSpesifikasjon? = null,
  /**
   * Soknadsmottaker
   */
  public val mottaker: Soknadsmottaker? = null,
  public val midlertidigAdresse: Adresse? = null,
)
