package let.it.be.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    public final float temp;
    @SerializedName("temp_min") public final float tempMin;
    @SerializedName("temp_max") public final float tempMax;
    public final float pressure;
    public final float humidity;

    public WeatherData(float temp, float tempMin, float tempMax, float pressure, float humidity) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getTempFormated() {
        return  String.format("%2.1f°", temp);
    }
}
