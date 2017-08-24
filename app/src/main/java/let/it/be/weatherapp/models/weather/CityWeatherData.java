package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

public class CityWeatherData implements Parcelable {

    public static final String TAG = CityWeatherData.class.getSimpleName();
    public static final Creator<CityWeatherData> CREATOR = new Creator<CityWeatherData>() {
        @Override
        public CityWeatherData createFromParcel(Parcel in) {
            return new CityWeatherData(in);
        }

        @Override
        public CityWeatherData[] newArray(int size) {
            return new CityWeatherData[size];
        }
    };

    public final int id;
    public final long dt;
    public final String name;
    public final LinkedHashMap coord;
    public final WeatherData main;
    public final WindData wind;
    public final FalloutData rain;
    public final FalloutData snow;
    public final LinkedHashMap clouds;
    public final WeatherIdData[] weather;

    public CityWeatherData(int id, long dt, String name, LinkedHashMap coord, WeatherData main, WindData wind, FalloutData rain, FalloutData snow, LinkedHashMap clouds, WeatherIdData[] weather) {
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

    protected CityWeatherData(Parcel in) {
        id = in.readInt();
        dt = in.readLong();
        name = in.readString();
        coord = (LinkedHashMap) in.readSerializable();
        main = in.readParcelable(WeatherData.class.getClassLoader());
        wind = in.readParcelable(WindData.class.getClassLoader());
        rain = in.readParcelable(FalloutData.class.getClassLoader());
        snow = in.readParcelable(FalloutData.class.getClassLoader());
        clouds = (LinkedHashMap) in.readSerializable();
        weather = in.createTypedArray(WeatherIdData.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(dt);
        dest.writeString(name);
        dest.writeSerializable(coord);
        dest.writeParcelable(main, flags);
        dest.writeParcelable(wind, flags);
        dest.writeParcelable(rain, flags);
        dest.writeParcelable(snow, flags);
        dest.writeSerializable(clouds);
        dest.writeTypedArray(weather, flags);
    }
}
