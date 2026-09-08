package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object HendelseSerializer : JsonContentPolymorphicSerializer<Hendelse>(Hendelse::class) {
  override fun selectDeserializer(element: JsonElement): KSerializer<out Hendelse> = when (element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
    "tildeltNavKontor" -> TildeltNavKontor.serializer()
    "soknadsStatus" -> SoknadsStatus.serializer()
    "vedtakFattet" -> VedtakFattet.serializer()
    "dokumentasjonEtterspurt" -> DokumentasjonEtterspurt.serializer()
    "forelopigSvar" -> ForelopigSvar.serializer()
    "saksStatus" -> SaksStatus.serializer()
    "utbetaling" -> Utbetaling.serializer()
    "vilkar" -> Vilkar.serializer()
    "dokumentasjonkrav" -> Dokumentasjonkrav.serializer()
    "rammevedtak" -> Rammevedtak.serializer()
    else -> UkjentHendelse.serializer()
  }
}
