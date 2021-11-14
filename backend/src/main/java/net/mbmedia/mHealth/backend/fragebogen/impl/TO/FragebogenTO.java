package net.mbmedia.mHealth.backend.fragebogen.impl.TO;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;

public class FragebogenTO
{
    Long id;
    String titel;
    String beschreibung;
    String json;

    FragebogenTO(Long id, String titel, String beschreibung, String json)
    {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.json = json;
    }

    public static FragebogenTO mappe(FragebogenEntity entity)
    {
        return new FragebogenTO(entity.getId(), entity.getTitel(), entity.getBeschreibung(), entity.getJson());
    }


}
