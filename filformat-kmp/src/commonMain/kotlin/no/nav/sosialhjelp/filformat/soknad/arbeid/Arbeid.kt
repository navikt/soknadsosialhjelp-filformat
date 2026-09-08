package no.nav.sosialhjelp.filformat.soknad.arbeid

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Arbeid
 */
@Serializable
public data class Arbeid(
  /**
   * Liste over arbeidsforhold de siste 3 månedene.
   *
   * Hvis "forhold" mangler betyr dette at man ikke har fått data fra AA-registeret. Feltet "situasjon" vil da istedenfor benyttes.
   */
  public val forhold: List<Arbeidsforhold>? = null,
  public val situasjon: Arbeidssituasjon? = null,
  public val kommentarTilArbeidsforhold: KommentarTilArbeidsforhold? = null,
)
