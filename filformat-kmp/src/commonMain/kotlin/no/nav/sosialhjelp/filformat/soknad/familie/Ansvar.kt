package no.nav.sosialhjelp.filformat.soknad.familie

import kotlinx.serialization.Serializable

@Serializable
public data class Ansvar(
  public val barn: Barn? = null,
  public val borSammenMed: BorSammenMed? = null,
  public val erFolkeregistrertSammen: ErFolkeregistrertSammen? = null,
  public val harDeltBosted: HarDeltBosted? = null,
  public val samvarsgrad: Samvarsgrad? = null,
)
