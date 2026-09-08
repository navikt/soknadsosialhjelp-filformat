package no.nav.sbl.soknadsosialhjelp.soknad.utdanning

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Utdanning
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "erStudent", "studentgrad")
public open class JsonUtdanning : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("erStudent")
  @set:JsonProperty("erStudent")
  @get:JsonPropertyDescription("Hvis \"erStudent\" mangler betyr dette at søker ikke har svart på spørsmålet.")
  public var erStudent: Boolean? = null

  @get:JsonProperty("studentgrad")
  @set:JsonProperty("studentgrad")
  @get:JsonPropertyDescription("Hvis \"studentgrad\" mangler betyr dette at søker ikke har svart på spørsmålet.")
  public var studentgrad: Studentgrad? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonUtdanning {
    this.kilde = kilde
    return this
  }

  public open fun withErStudent(erStudent: Boolean?): JsonUtdanning {
    this.erStudent = erStudent
    return this
  }

  public open fun withStudentgrad(studentgrad: Studentgrad?): JsonUtdanning {
    this.studentgrad = studentgrad
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonUtdanning {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonUtdanning) return false
    return kilde == other.kilde &&
        erStudent == other.erStudent &&
        studentgrad == other.studentgrad &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (erStudent?.hashCode() ?: 0)
    result = result * 31 + (studentgrad?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonUtdanning(kilde=$kilde, erStudent=$erStudent, studentgrad=$studentgrad, additionalProperties=$additionalProperties)"

  public enum class Studentgrad(
    @JsonValue
    public val `value`: String,
  ) {
    HELTID("heltid"),
    DELTID("deltid"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Studentgrad = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
