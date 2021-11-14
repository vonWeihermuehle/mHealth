package net.mbmedia.mHealth.backend.ort.impl;

import net.mbmedia.mHealth.backend.ort.IOrteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.ort.impl.OrtEntity.BUILDER.toBuilder;
import static net.mbmedia.mHealth.backend.ort.impl.OrtEntityTestDatenErzeuger.standartOrt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrteJPATest
{

    @Autowired
    private IOrteService orteService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(orteService);
    }

    @Test
    public void add_soll_hinzufuegen()
    {
        OrtEntity ort = standartOrt();
        Optional<Long> ortId = orteService.add(ort);

        OrtEntity ortEntity = orteService.getAll().get(0);

        assert (ortEntity).getTitel().equals(ort.getTitel());
    }

    @Test
    public void getAllFuer_soll_nur_fuer_uuid_anzeigen()
    {
        OrtEntity ort = standartOrt();
        OrtEntity irrelevanterOrt = standartOrt();
        persistiere(ort, irrelevanterOrt);

        List<OrtEntity> allFuer = orteService.getAllFuer(ort.getPatientUUID());
        assert (!allFuer.isEmpty());
        allFuer.forEach(o ->
                {
                    assert (ort.getPatientUUID().equals(o.getPatientUUID()));
                });
    }

    @Test
    public void soll_Ort_loeschen()
    {
        OrtEntity ort = standartOrt();
        Optional<Long> ortID = orteService.add(ort);

        orteService.removeByID(ortID.get());

        List<OrtEntity> all = orteService.getAll();
        assert (all.isEmpty());
    }

    @Test
    public void soll_Ort_aendern()
    {
        OrtEntity ort = standartOrt();
        Optional<Long> ortID = orteService.add(ort);

        String beschreibung = "geänderte Beschreibung";
        OrtEntity ortUpgedated = toBuilder(ort)
                .withID(ortID.get())
                .withBeschreibung(beschreibung)
                .build();

        orteService.update(ortUpgedated);

        OrtEntity nachUpdate = orteService.getAll().get(0);
        assert (nachUpdate.getBeschreibung()).equals(beschreibung);
    }

    @Test
    public void OrteLoeschenFuer()
    {
        OrtEntity ort = standartOrt();
        persistiere(ort);

        orteService.deleteFor(ort.getPatientUUID());

        List<OrtEntity> all = orteService.getAll();
        assert(all.isEmpty());
    }

    private void persistiere(OrtEntity... orte)
    {
        Arrays.stream(orte).forEach(ort -> this.orteService.add(ort));
    }

}