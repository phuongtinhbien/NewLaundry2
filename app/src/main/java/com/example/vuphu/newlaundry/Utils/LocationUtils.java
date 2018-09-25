package com.example.vuphu.newlaundry.Utils;

import android.location.Location;

public class LocationUtils {

    public static String getDistanceFromLocation(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {

        float[] distance = new float[2];

        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distance);

        return String.format("%.2f %S", distance[0] * 0.0006, "mi");
    }
}
