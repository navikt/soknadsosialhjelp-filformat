import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class TypesTest {
    @ParameterizedTest
    @MethodSource("finnAlleTestdatafiler")
    fun jsonValiderer(testName: String, testData: TestData) = testData.valider()

    companion object {
        @JvmStatic
        fun finnAlleTestdatafiler() = TestDataFiles.list(TestDataFiles.Config("json/types", null, "src/test/resources/json/types/"))
    }
}
