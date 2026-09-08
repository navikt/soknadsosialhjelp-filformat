package no.nav.sbl.soknadsosialhjelp.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the contract stated in the readme: generated model classes must keep the
 * same names and package hierarchy as before. Regenerate the snapshot file with
 * {@code -DwriteModelSnapshot=true} only when a change is an intentional, reviewed
 * rename.
 */
class ModelSnapshotTest {

    private static final File SNAPSHOT_FILE =
            new File("src/test/resources/model-snapshot.json");

    @Test
    void generatedModelMatchesSnapshot() throws Exception {
        ObjectMapper mapper = ModelSnapshot.snapshotMapper();
        Map<String, ModelSnapshot.ClassSnapshot> actual = ModelSnapshot.capture();

        if (Boolean.getBoolean("writeModelSnapshot") || !SNAPSHOT_FILE.exists()) {
            mapper.writeValue(SNAPSHOT_FILE, actual);
            return;
        }

        String actualJson = mapper.writeValueAsString(actual);
        String expectedJson = readSnapshotFile(mapper);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    private static String readSnapshotFile(ObjectMapper mapper) throws IOException {
        Map<String, ModelSnapshot.ClassSnapshot> expected =
                mapper.readValue(SNAPSHOT_FILE, mapper.getTypeFactory()
                        .constructMapType(Map.class, String.class, ModelSnapshot.ClassSnapshot.class));
        return mapper.writeValueAsString(expected);
    }
}
