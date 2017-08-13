package let.it.be.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

public class FalloutData {
    @SerializedName("3h") public final float threeHour;

    public FalloutData(float threeHour) {
        this.threeHour = threeHour;
    }
}
