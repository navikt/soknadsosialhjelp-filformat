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
class SoknadTest {

    private static Iterable<Object[]> finnAlleTestdatafiler() {
        var config = new TestDataFiles.Config()
                .withSchemaDirectory("json/soknad")
                .withToplevelSchemaFilename("soknad.json")
                .withTestDataDirectory("src/test/resources/json/soknad/");
        return TestDataFiles.list(config);
    }

    @ParameterizedTest
    @MethodSource("finnAlleTestdatafiler")
    void jsonValiderer(String testName, TestData testData) {
        testData.valider();
    }
}
