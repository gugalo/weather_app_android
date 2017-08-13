package let.it.be.weatherapp.models.weather;

public class WeatherDataExtended extends WeatherData {
    public final float sea_level;
    public final float grnd_level;
    public final float temp_kf;

    public WeatherDataExtended(float temp, float tempMin, float tempMax, float pressure, float humidity, float sea_level, float grnd_level, float temp_kf) {
        super(temp, tempMin, tempMax, pressure, humidity);
        this.sea_level = sea_level;
        this.grnd_level = grnd_level;
        this.temp_kf = temp_kf;
    }
}
