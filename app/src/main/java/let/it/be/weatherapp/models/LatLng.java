package let.it.be.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class LatLng {
    @SerializedName("Lat") public final double lat;
    @SerializedName("Lon") public final double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
