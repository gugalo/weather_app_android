package let.it.be.weatherapp.models;

import java.util.Map;

public class CityData {
    public final int id;
    public final String name;
    public final Map coord;
    public final String country;

    public CityData(int id, String name, Map coord, String country) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
    }
}
