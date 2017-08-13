package let.it.be.weatherapp.models.weather;

import java.util.Map;

public class CityWeatherData {
    public final int id;
    public final double dt;
    public final String name;
    public final Map coord;
    public final WeatherData main;
    public final WindData wind;
    public final FalloutData rain;
    public final FalloutData snow;
    public final Map clouds;
    public final WeatherIdData[] weather;

    public CityWeatherData(int id, double dt, String name, Map coord, WeatherData main, WindData wind, FalloutData rain, FalloutData snow, Map clouds, WeatherIdData[] weather) {
        this.id = id;
        this.dt = dt;
        this.name = name;
        this.coord = coord;
        this.main = main;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.clouds = clouds;
        this.weather = weather;
    }
}
