package let.it.be.weatherapp.models.weather;

public class CurrentWeatherData {

    public final int cod;
    public final float calctime;
    public final int cnt;
    public final CityWeatherData[] list;

    public CurrentWeatherData(int cod, float calctime, int cnt, CityWeatherData[] list) {
        this.cod = cod;
        this.calctime = calctime;
        this.cnt = cnt;
        this.list = list;
    }
}
