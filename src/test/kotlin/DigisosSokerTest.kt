import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class DigisosSokerTest {
    @ParameterizedTest
    @MethodSource("finnAlleTestdatafiler")
    fun jsonValiderer(testName: String, testData: TestData) = testData.valider()

    companion object {
        @JvmStatic
        fun finnAlleTestdatafiler() = TestDataFiles.list(TestDataFiles.Config("json/digisos/soker", "digisos-soker.json", "src/test/resources/json/digisos/soker/"))
    }
}
