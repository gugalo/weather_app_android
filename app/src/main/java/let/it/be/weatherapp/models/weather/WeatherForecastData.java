package let.it.be.weatherapp.models.weather;

import let.it.be.weatherapp.models.CityData;

public class WeatherForecastData {

    public final int cod;
    public final float message;
    public final int cnt;
    public final CityForecastData[] list;
    public final CityData city;

    public WeatherForecastData(int cod, float message, int cnt, CityForecastData[] list, CityData city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }
}
