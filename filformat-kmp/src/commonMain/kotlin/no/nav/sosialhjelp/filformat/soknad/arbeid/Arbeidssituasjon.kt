package no.nav.sosialhjelp.filformat.soknad.arbeid

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * En oppsummering av arbeidssituasjonen.
 *
 * Hvis situasjon mangler vil "forhold" være satt (og mottsatt).
 */
@Serializable
public data class Arbeidssituasjon(
  public val kilde: KildeBruker,
  /**
   * Hvis "erIJobb" mangler betyr dette at bruker ikke har besvart spørsmålet i skjemaet.
   */
  public val erIJobb: Boolean? = null,
  /**
   * Hvis "jobbGrad" mangler betyr dette at bruker ikke har besvart spørsmålet i skjemaet.
   */
  public val jobbGrad: JobbGrad? = null,
) {
  @Serializable(with = JobbGradSerializer::class)
  public enum class JobbGrad(
    public val jsonValue: String,
  ) {
    HELTID("heltid"),
    DELTID("deltid"),
    UKJENT("UKJENT"),
    ;
  }

  public object JobbGradSerializer : UkjentTolerantEnumSerializer<JobbGrad>("Arbeidssituasjon.JobbGrad", JobbGrad.entries.toTypedArray(), JobbGrad.UKJENT, JobbGrad::jsonValue)
}
