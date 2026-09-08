package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Hendelse
 *
 * Feltet "type" angir hvilken type hendelse det er. Se egen definisjon per hendelse. Det som er dokumentert direkte under er kun det som er felles for alle hendelser.
 */
@Serializable(with = HendelseSerializer::class)
public sealed interface Hendelse {
  public val type: String

  public val hendelsestidspunkt: String
}
