package net.mbmedia.mHealth.backend.util;

import java.util.UUID;

public class UUIDHelper
{

    public static String generateUUID()
    {
        return String.valueOf(UUID.randomUUID());
    }

    public static boolean isUUID(String id)
    {
        try
        {
            UUID uuid = UUID.fromString(id);
        } catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
