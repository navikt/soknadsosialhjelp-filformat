package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable

@Serializable
public data class DokumentlagerFilreferanse(
    public val id: String,
    override val type: String = "dokumentlager",
) : Filreferanse

@Serializable
public data class SvarUtFilreferanse(
    public val id: String,
    public val nr: Int,
    override val type: String = "svarut",
) : Filreferanse
