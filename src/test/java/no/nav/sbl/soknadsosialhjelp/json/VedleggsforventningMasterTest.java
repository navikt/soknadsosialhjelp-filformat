package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.soknad.personalia.*;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;
import org.junit.Test;

import java.util.List;

import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForPersonalia;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VedleggsforventningMasterTest {

    @Test
    public void finnPaakrevdeVedleggForPersonaliaKreverVedleggForIkkeNordiskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(false))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("CHN"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("oppholdstillatel"));
        assertThat(vedlegg.getTilleggsinfo(), is("oppholdstillatel"));
    }

    @Test
    public void finnPaakrevdeVedleggForPersonaliaKreverIkkeVedleggForNorskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(true))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("NOR"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        assertThat(paakrevdeVedlegg.size(), is(0));
    }

}