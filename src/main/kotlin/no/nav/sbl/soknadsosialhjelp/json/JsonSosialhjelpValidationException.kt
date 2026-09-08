package no.nav.sbl.soknadsosialhjelp.json

import com.github.fge.jsonschema.core.report.ProcessingReport

class JsonSosialhjelpValidationException(val report: ProcessingReport) :
    RuntimeException("JSON-validering mot skjema feilet: $report")
