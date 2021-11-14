package net.mbmedia.mHealth.backend.kontakt.impl;

import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.kontakt.impl.KontakteEntityTestDatenErzeuger.getStandardKontakt;
import static net.mbmedia.mHealth.backend.kontakt.impl.KontakteEntityTestDatenErzeuger.getStandardKontakteBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KontakteJPATest
{

    @Autowired
    private IKontakteService kontakteService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(kontakteService);
    }

    @Test
    public void soll_Kontakt_hinzufuegen()
    {
        KontaktEntity kontakt = getStandardKontakt();
        Optional<Long> id = kontakteService.addKontakt(kontakt);

        List<KontaktEntity> all = kontakteService.getAll();
        assert (id.isPresent());
        assert (all).size() == 1;
    }

    @Test
    public void soll_Kontakt_entfernen()
    {
        KontaktEntity kontakt = getStandardKontakt();
        Optional<Long> id = kontakteService.addKontakt(kontakt);

        kontakteService.delKontakt(id.get());
        List<Long> Ids = kontakteService.getAll().stream().map(KontaktEntity::getId).collect(Collectors.toList());

        assert (!Ids.contains(id.get()));
    }

    @Test
    public void soll_Kontakte_fuer_Patient_anzeigen()
    {
        String uuid = UUIDHelper.generateUUID();
        KontaktEntity kontakt = getStandardKontakteBuilder()
                .withUserID(uuid)
                .build();
        kontakteService.addKontakt(kontakt);

        List<KontaktEntity> allFuer = kontakteService.getAllFuer(uuid);
        String collect = allFuer.stream().map(KontaktEntity::getUserID).collect(Collectors.joining(","));
        assert (collect).contains(uuid);
    }

    @Test
    public void updateKontakt()
    {
        KontaktEntity kontakt = getStandardKontakt();
        Optional<Long> id = kontakteService.addKontakt(kontakt);

        String speziellerName = "speziellerName";
        kontakt.setName(speziellerName);
        kontakteService.updateKontakt(kontakt);

        Optional<KontaktEntity> nachUpdate = kontakteService.getByID(id.get());
        assert (nachUpdate.isPresent());
        assert (nachUpdate.get().getName()).equals(speziellerName);
    }

    @Test
    public void deleteKontakt()
    {
        KontaktEntity kontakt = getStandardKontakt();
        kontakteService.addKontakt(kontakt);

        kontakteService.deleteKontakteFor(kontakt.getUserID());

        List<KontaktEntity> all = kontakteService.getAll();
        assert(all.isEmpty());
    }
}