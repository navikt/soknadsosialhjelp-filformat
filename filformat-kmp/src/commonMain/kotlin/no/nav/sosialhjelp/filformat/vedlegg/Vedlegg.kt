package no.nav.sosialhjelp.filformat.vedlegg

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Vedlegg
 */
@Serializable
public data class Vedlegg(
  /**
   * Angir hvilken type vedlegg det gjelder. Det kan komme nye typer og dette må håndteres dynamisk av konsumenter. Her er noen av de mulige typene:
   * * annet
   *  * barnebidrag
   *  * bostotte
   *  * dokumentasjon
   *  * faktura
   *  * husleiekontrakt
   *  * kjopekontrakt
   *  * kontooversikt
   *  * lonnslipp
   *  * nedbetalingsplan
   *  * oppholdstillatel
   *  * salgsoppgjor
   *  * samvarsavtale
   *  * skattemelding
   *  * student
   */
  public val type: String? = null,
  /**
   * Angir subtype til vedlegg det gjelder. Det vil jevnlig komme nye subtyper og dette må håndteres dynamisk av konsumenter. Her er noen av de mulige typene:
   * * aksjer
   *  * annet
   *  * annetbarnutgift
   *  * annetboutgift
   *  * annetinntekter
   *  * annetverdi
   *  * arbeid
   *  * avdraglaan
   *  * barn
   *  * barnehage
   *  * betaler
   *  * brukskonto
   *  * bsu
   *  * campingvogn
   *  * eiendom
   *  * forsikringsutbetaling
   *  * fritidsaktivitet
   *  * fritidseiendom
   *  * husleie
   *  * husleiekontrakt
   *  * kjopekontrakt
   *  * kjoretoy
   *  * kommunaleavgifter
   *  * livsforsikring
   *  * mottar
   *  * oppholdstillatel
   *  * oppvarming
   *  * sfo
   *  * skattemelding
   *  * sparekonto
   *  * strom
   *  * tannbehandling
   *  * utbytte
   *  * vedtak
   */
  public val tilleggsinfo: String? = null,
  /**
   * Id til klagen som vedlegget hører til, hvis det er tilfelle. Optional.
   */
  public val klageId: String? = null,
  /**
   * Status for vedlegget. Kan være "LastetOpp", "VedleggKreves" eller "VedleggAlleredeSendt". Hvis "status" har blitt satt til "VedleggAlleredeSendt" eller "VedleggKreves" vil "filer" være en tom array. Ukjente statusverdier skal ikke hindre visning/arkivering av eventuelle filer.
   */
  public val status: String? = null,
  /**
   * Array
   *
   * Liste med filer som hører til det samme vedlegget.
   */
  public val filer: List<Filer>? = null,
  /**
   * Angir typen til hendelsen som fikk brukeren til å laste opp dette vedlegget. Mulige typer:
   * * dokumentasjonEtterspurt - om vedlegget ble lastet opp på grunn av hendelsen dokumentasjonEtterspurt 
   * * dokumentasjonkrav - om vedlegget ble lastet opp på grunn av hendelsen dokumentasjonkrav 
   * * soknad - om vedlegget ble lastet opp på grunn av vedleggskrav generert av søknaden 
   * * bruker - når bruker selv velger å laste opp annen dokumentasjon
   */
  public val hendelseType: HendelseType? = null,
  /**
   * Peker på referansen til hendelsen som fikk brukeren til å laste opp dette vedlegget.
   */
  public val hendelseReferanse: String? = null,
) {
  @Serializable(with = HendelseTypeSerializer::class)
  public enum class HendelseType(
    public val jsonValue: String,
  ) {
    DOKUMENTASJON_ETTERSPURT("dokumentasjonEtterspurt"),
    DOKUMENTASJONKRAV("dokumentasjonkrav"),
    SOKNAD("soknad"),
    BRUKER("bruker"),
    UKJENT("UKJENT"),
    ;
  }

  public object HendelseTypeSerializer : UkjentTolerantEnumSerializer<HendelseType>("Vedlegg.HendelseType", HendelseType.entries.toTypedArray(), HendelseType.UKJENT, HendelseType::jsonValue)
}
