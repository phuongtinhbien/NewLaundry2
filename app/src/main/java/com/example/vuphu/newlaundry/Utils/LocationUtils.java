package com.example.vuphu.newlaundry.Utils;

import android.location.Location;

public class LocationUtils {

    public static float getDistanceFromLocation(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {

        float[] distance = new float[2];

        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distance);

        return distance[0];
    }
}

