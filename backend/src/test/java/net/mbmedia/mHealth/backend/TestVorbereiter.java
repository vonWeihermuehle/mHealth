package net.mbmedia.mHealth.backend;

import net.mbmedia.mHealth.backend.db.BaseJPA;

import java.util.Arrays;

public class TestVorbereiter
{

    public static void truncateAllTables(Object... services)
    {
        Arrays.stream(services).map(s -> (BaseJPA) s).forEach(BaseJPA::removeAll);
    }
}
