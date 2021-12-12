package net.mbmedia.mHealth.backend.jobs;

import net.mbmedia.mHealth.backend.fragebogen.IFragebogenService;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenEntityTestdatenErzeuger.createStandardFragebogenEntity;
import static net.mbmedia.mHealth.backend.fragebogen.FragebogenZuweisungEntityTestdatenErzeuger.getStandardFragebogenZuweisungBuilder;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class setCronZuweisungenJOBTest
{
    @Autowired
    private IFragebogenService fragebogenService;

    @Autowired
    private IUserService userService;

    @Autowired
    private setCronZuweisungenJOB job;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(fragebogenService, userService);
    }

    @Test
    public void run()
    {
        LocalDate vorEinerWoche = LocalDate.now().minusWeeks(1);
        FragebogenZuweisungEntity fragebogenZuweisungEntity = createFragebogenZuweisungEntity("1", vorEinerWoche);
        persist(fragebogenZuweisungEntity);

        job.run();

        List<FragebogenZuweisungEntity> allZuweisungen = fragebogenService.getAllZuweisungen();
        assert(allZuweisungen.size()) == 2;
        allZuweisungen.stream()
                .filter(z -> z.getCron() == null)
                .forEach(z ->
                {
                    assert(z.getErstellt().equals(LocalDate.now()));
                });
    }

    private FragebogenZuweisungEntity createFragebogenZuweisungEntity(String cron, LocalDate date)
    {
        FragebogenEntity fragebogen = createStandardFragebogenEntity();
        UserEntity user = getStandardUserEntity();
        persist(fragebogen);
        persist(user);
        return getStandardFragebogenZuweisungBuilder()
                .withFragebogen(fragebogen)
                .withEmpfaenger(user)
                .withCron(cron)
                .withErstellt(date)
                .build();
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