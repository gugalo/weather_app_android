package let.it.be.weatherapp.models.weather;

import java.util.Map;

public class CityForecastData {
    public final long dt;
    public final WeatherDataExtended main;
    public final WeatherIdData[] weather;
    public final Map clouds;
    public final WindData wind;
    public final Map sys;
    public final String dt_txt;

    public CityForecastData(long dt, WeatherDataExtended main, WeatherIdData[] weather, Map clouds, WindData wind, Map sys, String dt_txt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.sys = sys;
        this.dt_txt = dt_txt;
    }
}
