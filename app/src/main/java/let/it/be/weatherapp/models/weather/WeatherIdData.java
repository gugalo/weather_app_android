package let.it.be.weatherapp.models.weather;

public class WeatherIdData {
    public final int id;
    public final String main;
    public final String description;
    public final String icon;

    public WeatherIdData(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }
}
