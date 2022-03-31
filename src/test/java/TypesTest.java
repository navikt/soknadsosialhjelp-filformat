import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Kjører skjemavalidering av alle json-filer i "src/test/resouces/json/soknad".
 *
 * <ol>
 *     <li>Filer som starter på "feil-" skal feile validering mens alle andre filer skal passere.</li>
 *     <li>Filer i underkataloger blir kjørt mot delskjemaene med samme navn som underkatalogen testfilen ligger i.</li>
 * </ol>
 */
class TypesTest {

    private static Iterable<Object[]> finnAlleTestdatafiler() {
        return TestDataFiles.list(new TestDataFiles.Config()
                .withSchemaDirectory("json/types")
                .withToplevelSchemaFilename(null)
                .withTestDataDirectory("src/test/resources/json/types/")
        );
    }

    @ParameterizedTest
    @MethodSource("finnAlleTestdatafiler")
    void jsonValiderer(String testName, TestData testData) {
        testData.valider();
    }
}
