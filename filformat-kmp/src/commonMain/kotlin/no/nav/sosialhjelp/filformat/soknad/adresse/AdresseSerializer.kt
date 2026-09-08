package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object AdresseSerializer : JsonContentPolymorphicSerializer<Adresse>(Adresse::class) {
  override fun selectDeserializer(element: JsonElement): KSerializer<out Adresse> = when (element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
    "gateadresse" -> GateAdresse.serializer()
    "matrikkeladresse" -> MatrikkelAdresse.serializer()
    "postboks" -> PostboksAdresse.serializer()
    "ustrukturert" -> UstrukturertAdresse.serializer()
    else -> UkjentAdresse.serializer()
  }
}
