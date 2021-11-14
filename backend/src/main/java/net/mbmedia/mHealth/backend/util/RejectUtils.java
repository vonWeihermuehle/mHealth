package net.mbmedia.mHealth.backend.util;

import java.util.Arrays;
import java.util.Optional;

public class RejectUtils
{
    public static void rejectIf(boolean value)
    {
        if(value)
        {
            throw new RuntimeException();
        }
    }

    public static void rejectIfNot(boolean value)
    {
        rejectIf(!value);
    }

    public static void rejectIfNotPresent(Optional<?>... optionals)
    {
        Arrays.stream(optionals).forEach(RejectUtils::rejectIfNotPresent);
    }

    public static void rejectIfNotPresent(Optional<?> optional)
    {
        if(!optional.isPresent())
        {
            throw new RuntimeException();
        }
    }
}
