package net.mbmedia.mHealth.backend.jobs;

import net.mbmedia.mHealth.backend.fragebogen.IFragebogenService;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class setCronZuweisungenJOB
{
    private static final Logger log = LoggerFactory.getLogger(setCronZuweisungenJOB.class);

    @Autowired
    private IFragebogenService fragebogenService;

    @Scheduled(cron = "0 0 5 * * *") //TODO: Um wie viel Uhr sollen die Mails gesendet werden? Aktuell 5 Uhr morgens
    public void run()
    {
        List<FragebogenZuweisungEntity> allCronZuweisungen = fragebogenService.getAllCronZuweisungen();
        allCronZuweisungen.stream()
                .filter(this::isRelevant)
                .map(this::mappeZuNeuerZuweisung)
                .forEach(z -> fragebogenService.addZuweisung(z));
    }

    private FragebogenZuweisungEntity mappeZuNeuerZuweisung(FragebogenZuweisungEntity cronEntity)
    {
        return cronEntity.toBuilder()
                .withCron(null)
                .withErstellt(LocalDate.now())
                .withID(null)
                .build();
    }

    private boolean isRelevant(FragebogenZuweisungEntity entity)
    {
        String cron = entity.getCron();
        LocalDate erstellt = entity.getErstellt();
        LocalDate erstelltPlusCron = erstellt.plusWeeks(Long.parseLong(cron));
        LocalDate heute = LocalDate.now();

        return erstelltPlusCron.equals(heute);
    }

}
