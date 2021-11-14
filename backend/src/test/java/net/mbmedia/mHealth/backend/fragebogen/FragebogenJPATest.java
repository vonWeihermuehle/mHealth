package net.mbmedia.mHealth.backend.fragebogen;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenAbgeschlossenEntityTestdatenErzeuger.*;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenEntityTestdatenErzeuger.createStandardFragebogenEntity;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenEntityTestdatenErzeuger.getStandardFragebogenEntityBuilder;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenZuweisungEntityTestdatenErzeuger.createStandardFragebogenZuweisung;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenZuweisungEntityTestdatenErzeuger.getStandardFragebogenZuweisungBuilder;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FragebogenJPATest
{

    @Autowired
    private IFragebogenService fragebogenService;

    @Autowired
    private IUserService userService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(fragebogenService, userService);
    }

    @Test
    public void addFragebogen()
    {
        FragebogenEntity entity = createStandardFragebogenEntity();

        Optional<Long> id = fragebogenService.addFragebogen(entity);

        List<FragebogenEntity> all = fragebogenService.getAll();
        assert (id.isPresent());
        assert (all.size()) == 1;
    }

    @Test
    public void delFragebogen()
    {
        FragebogenEntity entity = createStandardFragebogenEntity();
        Optional<Long> id = fragebogenService.addFragebogen(entity);
        assert (id.isPresent());

        fragebogenService.delFragebogen(id.get());

        List<FragebogenEntity> all = fragebogenService.getAll();
        assert (all.size()) == 0;
    }

    @Test
    public void updateFragebogen()
    {
        FragebogenEntity entity = createStandardFragebogenEntity();
        Optional<Long> id = fragebogenService.addFragebogen(entity);

        String anderesJson = "ganz anderes Json";
        FragebogenEntity updated = entity.toBuilder()
                .withID(id.get())
                .withJson(anderesJson)
                .build();

        fragebogenService.updateFragebogen(updated);

        Optional<FragebogenEntity> byId = fragebogenService.getById(id.get());
        assert (byId.isPresent());
        assert (byId.get().getJson().equals(anderesJson));
    }

    @Test
    public void getAll()
    {
        persist(createStandardFragebogenEntity(), createStandardFragebogenEntity());

        List<FragebogenEntity> all = fragebogenService.getAll();

        assert (all.size()) == 2;
    }

    @Test
    public void getById()
    {
        Optional<Long> aLong = fragebogenService.addFragebogen(createStandardFragebogenEntity());

        Optional<FragebogenEntity> byId = fragebogenService.getById(aLong.get());

        assert (byId.isPresent());
    }

    @Test
    public void getForAuthor()
    {
        String authorID = "asdf";
        FragebogenEntity relevant = getStandardFragebogenEntityBuilder()
                .withAuthor(authorID)
                .build();
        FragebogenEntity irrelevant = createStandardFragebogenEntity();
        persist(relevant, irrelevant);

        List<FragebogenEntity> all = fragebogenService.getForAuthor(authorID);

        all.forEach(f ->
        {
            assert (f.getAuthor().equals(authorID));
        });
    }

    @Test
    public void getForEmpfaenger()
    {
        String empfaengerID = "asdf";
        String besonderesJson = "besonderes Json";
        FragebogenEntity relevant = getStandardFragebogenEntityBuilder().withJson(besonderesJson).build();
        FragebogenEntity irrelevant = createStandardFragebogenEntity();
        persist(relevant, irrelevant);
        FragebogenZuweisungEntity zuweisung = getStandardFragebogenZuweisungBuilder()
                .withEmpfaenger(getStandardUserEntityBuilder().withUUID(empfaengerID).build())
                .build();
        persist(zuweisung);

        List<FragebogenEntity> all = fragebogenService.getForEmpfaenger(empfaengerID);

        all.forEach(f ->
        {
            assert (f.getJson().equals(besonderesJson));
        });
    }

    @Test
    public void addZuweisung()
    {
        FragebogenZuweisungEntity entity = createFragebogenZuweisungEntity();

        fragebogenService.addZuweisung(entity);

        assert (fragebogenService.getAllZuweisungen().size()) == 1;
    }

    @Test
    public void delZuweisung()
    {
        FragebogenZuweisungEntity entity = createFragebogenZuweisungEntity();
        fragebogenService.addZuweisung(entity);
        List<FragebogenZuweisungEntity> allZuweisungen = fragebogenService.getAllZuweisungen();

        allZuweisungen.forEach(z -> fragebogenService.delZuweisung(z.getId()));

        assert (fragebogenService.getAllZuweisungen().size()) == 0;
    }

    @Test
    public void updateZuweisung()
    {
        FragebogenZuweisungEntity entity = createFragebogenZuweisungEntity();
        Optional<Long> id = fragebogenService.addZuweisung(entity);

        String besondererCron = "besondererCron";
        FragebogenZuweisungEntity updated = entity.toBuilder()
                .withID(id.get())
                .withCron(besondererCron)
                .build();

        fragebogenService.updateZuweisung(updated);

        FragebogenZuweisungEntity entity1 = fragebogenService.getAllZuweisungen().get(0);
        assert (entity1.getCron().equals(besondererCron));
    }

    @Test
    public void removeZuweisungenForEmpfaenger()
    {
        UserEntity user = UserEntityTestDatenErzeuger.getStandardUserEntity();
        FragebogenZuweisungEntity relevant = getStandardFragebogenZuweisungBuilder().withEmpfaenger(user).build();
        FragebogenZuweisungEntity irrelevant = createStandardFragebogenZuweisung();
        persist(relevant, irrelevant);

        fragebogenService.removeForEmpfaenger(user.getUuid());

        List<FragebogenZuweisungEntity> allZuweisungen = fragebogenService.getAllZuweisungen();
        allZuweisungen.forEach(z ->
        {
            assert (!z.getEmpfaenger().getUuid().equals(user.getUuid()));
        });


    }

    @Test
    public void getZuweisungenForDate()
    {
        LocalDate date = LocalDate.of(1995, 1, 1);
        FragebogenZuweisungEntity relevant = createFragebogenZuweisungEntity(date);
        FragebogenZuweisungEntity irrelevant = createFragebogenZuweisungEntity();
        persist(relevant, irrelevant);

        List<FragebogenZuweisungEntity> forDate = fragebogenService.getForDate(date);

        assert (forDate.size()) == 1;
    }

    @Test
    public void getAllCronZuweisungen()
    {
        String cron = "1";
        FragebogenZuweisungEntity relevant = createFragebogenZuweisungEntityWithCron(cron);
        FragebogenZuweisungEntity irrelevant = createFragebogenZuweisungEntityWithCron(null);
        persist(relevant, irrelevant);

        List<FragebogenZuweisungEntity> allCronZuweisungen = fragebogenService.getAllCronZuweisungen();

        assert (allCronZuweisungen.size()) == 1;
    }

    @Test
    public void addAbgeschlossen()
    {
        FragebogenAbgeschlossenEntity entity = createFragebogenAbgeschlossenEntity();

        fragebogenService.addAbgeschlossen(entity);

        assert (fragebogenService.getAllAbgeschlossen().size()) == 1;
    }

    @Test
    public void getAllAbgeschlossenFuerPatient()
    {
        String patientUUID = UUIDHelper.generateUUID();
        FragebogenAbgeschlossenEntity relevant = createFragebogenAbgeschlossenEntity(patientUUID);
        FragebogenAbgeschlossenEntity irrelevant = createFragebogenAbgeschlossenEntity();
        persist(relevant, irrelevant);

        List<FragebogenAbgeschlossenEntity> allAbgeschlossenFuerPatient = fragebogenService.getAllAbgeschlossenFuerPatient(patientUUID);

        assert(allAbgeschlossenFuerPatient.size()) == 1;
    }

    private FragebogenAbgeschlossenEntity createFragebogenAbgeschlossenEntity()
    {
        return this.createFragebogenAbgeschlossenEntity(UUIDHelper.generateUUID());
    }

    private FragebogenAbgeschlossenEntity createFragebogenAbgeschlossenEntity(String uuid)
    {
        UserEntity user = getStandardUserEntityBuilder()
                .withUUID(uuid)
                .build();
        persist(user);
        return getStandardFragebogenAbgeschlossenBuilder()
                .withPatient(user)
                .build();
    }

    private FragebogenZuweisungEntity createFragebogenZuweisungEntity()
    {
        return createFragebogenZuweisungEntity(LocalDate.now());
    }

    private FragebogenZuweisungEntity createFragebogenZuweisungEntityWithCron(String cron)
    {
        FragebogenEntity fragebogen = createStandardFragebogenEntity();
        UserEntity user = getStandardUserEntity();
        persist(fragebogen);
        persist(user);
        return getStandardFragebogenZuweisungBuilder().withFragebogen(fragebogen).withEmpfaenger(user).withCron(cron).build();
    }

    private FragebogenZuweisungEntity createFragebogenZuweisungEntity(LocalDate date)
    {
        FragebogenEntity fragebogen = createStandardFragebogenEntity();
        UserEntity user = getStandardUserEntity();
        persist(fragebogen);
        persist(user);
        return getStandardFragebogenZuweisungBuilder().withFragebogen(fragebogen).withEmpfaenger(user).withErstellt(date).build();
    }

    private void persist(FragebogenAbgeschlossenEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> fragebogenService.addAbgeschlossen(e));
    }

    private void persist(FragebogenEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> fragebogenService.addFragebogen(e));
    }

    private void persist(FragebogenZuweisungEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> fragebogenService.addZuweisung(e));
    }

    private void persist(UserEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> userService.register(e));
    }
}
