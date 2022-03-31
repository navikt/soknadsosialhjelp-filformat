package no.nav.sbl.soknadsosialhjelp.json;

import com.github.fge.jsonschema.core.report.ProcessingReport;

public class JsonSosialhjelpValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ProcessingReport report;

    public JsonSosialhjelpValidationException(ProcessingReport report) {
        super("JSON-validering mot skjema feilet: " + report);
        this.report = report;
    }

    public ProcessingReport getReport() {
        return report;
    }
}
