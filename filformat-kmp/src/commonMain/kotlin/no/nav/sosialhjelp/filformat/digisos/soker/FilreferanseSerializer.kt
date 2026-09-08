package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object FilreferanseSerializer : JsonContentPolymorphicSerializer<Filreferanse>(Filreferanse::class) {
  override fun selectDeserializer(element: JsonElement): KSerializer<out Filreferanse> = when (element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
    "svarut" -> SvarUtFilreferanse.serializer()
    "dokumentlager" -> DokumentlagerFilreferanse.serializer()
    else -> UkjentFilreferanse.serializer()
  }
}
