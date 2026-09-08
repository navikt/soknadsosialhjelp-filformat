package no.nav.sosialhjelp.filformat.digisos.soker.hendelse

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.digisos.soker.Filreferanse

/**
 * Vedtaksfil
 *
 * Vedtaksfilen som søker skal ha mulighet til å se. Det er ingen garanti for at filen blir vist til søker. Filformatet skal være PDF.
 */
@Serializable
public data class Vedtaksfil(
  public val referanse: Filreferanse,
)
