package net.mbmedia.mHealth.backend.unterstuetzung;

import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.unterstuetzung.UnterstuetzungEntityTestDatenErzeuger.standardUnterstuetzung;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnterstuetzungJPATest
{
    @Autowired
    private IUnterstuetzungService unterstuetzungService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables( unterstuetzungService);
    }

    @Test
    public void soll_hinzufuegen_und_alle_anzeigen_koennen()
    {
        UnterstuetzungEntity entity = standardUnterstuetzung();
        Optional<Long> id = unterstuetzungService.add(entity);

        List<UnterstuetzungEntity> all = unterstuetzungService.getAll();
        assert(all).size() == 1;
        assert(id).isPresent();
        assert (all.get(0)).getId().equals(id.get());
    }

    @Test
    public void soll_updaten(){
        UnterstuetzungEntity entity = standardUnterstuetzung();
        Optional<Long> id = unterstuetzungService.add(entity);

        String speziellerText = "SpeziellerText";
        String speziellerTitel = "SpeziellerTitel";
        unterstuetzungService.update(id.get(), speziellerTitel, speziellerText);

        List<UnterstuetzungEntity> all = unterstuetzungService.getAll();
        assert(all).size() == 1;
        assert(all.get(0).getText()).equals(speziellerText);
        assert(all.get(0).getTitel()).equals(speziellerTitel);
    }

    @Test
    public void deleteForPatient()
    {
        UnterstuetzungEntity entity = standardUnterstuetzung();
        unterstuetzungService.add(entity);

        unterstuetzungService.deleteForPatient(entity.getEmpfaenger());

        List<UnterstuetzungEntity> all = unterstuetzungService.getAll();
        assert(all.isEmpty());
    }
}
