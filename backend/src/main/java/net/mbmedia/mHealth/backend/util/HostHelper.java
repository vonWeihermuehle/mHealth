package net.mbmedia.mHealth.backend.util;

public class HostHelper {

    public static String getHostFromString(String host){
        String[] split = host.split(":");
        return split[0];
    }
}
