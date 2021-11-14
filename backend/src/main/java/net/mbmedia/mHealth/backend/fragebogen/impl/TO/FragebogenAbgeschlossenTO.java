package net.mbmedia.mHealth.backend.fragebogen.impl.TO;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;

import java.time.LocalDate;

public class FragebogenAbgeschlossenTO extends FragebogenTO
{
    Integer wert;
    LocalDate erstellt;

    public FragebogenAbgeschlossenTO(Long id, String titel, String beschreibung, String json, Integer wert, LocalDate erstellt)
    {
        super(id, titel, beschreibung, json);
        this.wert = wert;
        this.erstellt = erstellt;
    }

    public static FragebogenAbgeschlossenTO mappe(FragebogenAbgeschlossenEntity entity)
    {
        return new FragebogenAbgeschlossenTO(entity.getId(), entity.getTitel(), entity.getBeschreibung(), entity.getErgebnis(), entity.getWert(), entity.getErstellt());
    }
}
