import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Kjører skjemavalidering av alle json-filer i "src/test/resouces/json/soknad".
 * 
 * <ol>
 *     <li>Filer som starter på "feil-" skal feile validering mens alle andre filer skal passere.</li>
 *     <li>Filer i underkataloger blir kjørt mot delskjemaene med samme navn som underkatalogen testfilen ligger i.</li>
 * </ol>
 */
@RunWith(Parameterized.class)
public class SoknadTest {

    private final TestData testData;

    
    public SoknadTest(String testName, TestData testData) {
        this.testData = testData;
    }
    
    
    @Parameters(name="{0}")
    public static Iterable<Object[]> finnAlleTestdatafiler() {
        return TestDataFiles.list(new TestDataFiles.Config()
                    .withSchemaDirectory("json/soknad")
                    .withToplevelSchemaFilename("soknad.json")
                    .withTestDataDirectory("src/test/resources/json/soknad/")
                );
    }
    
    @Test
    public void jsonValiderer() {       
        testData.valider();
    }
}
