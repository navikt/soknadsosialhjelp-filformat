package no.nav.sbl.soknadsosialhjelp.json;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Reflects over the generated model and produces a stable structural description:
 * class name, superclass, bean properties (name + type), and enum constants.
 *
 * Used to prove that a regenerated model has the same names and package hierarchy
 * as the model it replaces.
 */
public final class ModelSnapshot {

    private static final String GENERATED_SOURCES_DIR =
            "filformat-jackson/build/generated/sources/filformat/main/kotlin";

    private ModelSnapshot() {
    }

    public static Map<String, ClassSnapshot> capture() throws Exception {
        Map<String, ClassSnapshot> snapshot = new TreeMap<>();
        for (String className : discoverGeneratedClassNames()) {
            Class<?> clazz = Class.forName(className);
            snapshot.put(className, describe(clazz));
        }
        return snapshot;
    }

    private static List<String> discoverGeneratedClassNames() throws IOException {
        Path root = Path.of(GENERATED_SOURCES_DIR);
        try (Stream<Path> files = Files.walk(root)) {
            return files
                    .filter(p -> p.toString().endsWith(".kt"))
                    .map(p -> root.relativize(p).toString())
                    .map(p -> p.substring(0, p.length() - ".kt".length()))
                    .map(p -> p.replace(File.separatorChar, '.'))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private static ClassSnapshot describe(Class<?> clazz) {
        ClassSnapshot result = new ClassSnapshot();
        result.superclass = clazz.getSuperclass() == null ? null : clazz.getSuperclass().getName();
        result.interfaces = new ArrayList<>();
        for (Class<?> iface : clazz.getInterfaces()) {
            result.interfaces.add(iface.getName());
        }
        result.interfaces.sort(String::compareTo);

        if (clazz.isEnum()) {
            result.enumConstants = new ArrayList<>();
            for (Object constant : clazz.getEnumConstants()) {
                result.enumConstants.add(((Enum<?>) constant).name());
            }
            return result;
        }

        result.properties = new TreeMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3 && method.getParameterCount() == 0) {
                String property = decapitalize(name.substring(3));
                result.properties.put(property, method.getReturnType().getSimpleName());
            } else if (name.startsWith("is") && name.length() > 2 && method.getParameterCount() == 0
                    && (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                String property = decapitalize(name.substring(2));
                result.properties.put(property, method.getReturnType().getSimpleName());
            }
        }

        result.nestedTypes = new ArrayList<>();
        for (Class<?> nested : clazz.getDeclaredClasses()) {
            result.nestedTypes.add(nested.getSimpleName());
        }
        result.nestedTypes.sort(String::compareTo);

        return result;
    }

    private static String decapitalize(String s) {
        if (s.isEmpty()) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static ObjectMapper snapshotMapper() {
        return JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .build();
    }

    public static class ClassSnapshot {
        public String superclass;
        public List<String> interfaces;
        public Map<String, String> properties;
        public List<String> nestedTypes;
        public List<String> enumConstants;
    }
}
