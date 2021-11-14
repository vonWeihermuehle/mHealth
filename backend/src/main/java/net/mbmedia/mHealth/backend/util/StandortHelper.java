package net.mbmedia.mHealth.backend.util;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;

public class StandortHelper
{

    private static final float erdRadius = 6371; //Radius in KM
    private static final float radius = 30; //gesuchter Radius in KM


    public static UmkreisPunkte berechne(UserEntity user)
    {
        double lat = user.getLat();
        double lng = user.getLng();
        double vLat = Math.toDegrees(radius / erdRadius);
        double maxLat = lat + vLat;
        double minLat = lat - vLat;
        double vLng = Math.toDegrees(radius / erdRadius / Math.cos(Math.toRadians(lat)));
        double maxLng = lng + vLng;
        double minLng = lng - vLng;
        return new UmkreisPunkte(maxLat, minLat, maxLng, minLng);
    }

    public static class UmkreisPunkte
    {
        private final double maxLat;
        private final double minLat;
        private final double maxLng;
        private final double minlng;

        public UmkreisPunkte(double maxLat, double minLat, double maxLng, double minlng)
        {
            this.maxLat = maxLat;
            this.minLat = minLat;
            this.maxLng = maxLng;
            this.minlng = minlng;
        }

        public double getMaxLat()
        {
            return maxLat;
        }

        public double getMinLat()
        {
            return minLat;
        }

        public double getMaxLng()
        {
            return maxLng;
        }

        public double getMinlng()
        {
            return minlng;
        }
    }
}
