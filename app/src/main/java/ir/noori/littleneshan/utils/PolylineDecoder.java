package ir.noori.littleneshan.utils;

import org.neshan.common.model.LatLng;

import java.util.ArrayList;

public class PolylineDecoder {

    public ArrayList<LatLng> decodePoly(String encoded) {

//        Log.i("Location", "String received: " + encoded);
        ArrayList<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

//            LatLng p = new LatLng((int) (((double) lat / 1E5) * 1E6), (int) (((double) lng / 1E5 * 1E6)));
            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));

            poly.add(p);
        }

        for (int i = 0; i < poly.size(); i++) {
//            Log.i("Location", "Point sent: Latitude: " + poly.get(i).latitude + " Longitude: " + poly.get(i).longitude);
        }
        return poly;
    }
}
