package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable

/** `json/digisos/soker/parts/filreferanse/dokumentlager.json` */
@Serializable
public data class DokumentlagerFilreferanse(
    /** UUID identifying the document in Dokumentlager. */
    public val id: String,
    override val type: String = "dokumentlager",
) : Filreferanse

/** `json/digisos/soker/parts/filreferanse/svarut.json` */
@Serializable
public data class SvarUtFilreferanse(
    /** UUID identifying the SvarUt forsendelse. */
    public val id: String,
    /** File number within the SvarUt forsendelse. */
    public val nr: Int,
    override val type: String = "svarut",
) : Filreferanse
