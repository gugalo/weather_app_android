package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherDataExtended extends WeatherData implements Parcelable {
    public final float sea_level;
    public final float grnd_level;
    public final float temp_kf;

    public WeatherDataExtended(float temp, float tempMin, float tempMax, float pressure, float humidity, float sea_level, float grnd_level, float temp_kf) {
        super(temp, tempMin, tempMax, pressure, humidity);
        this.sea_level = sea_level;
        this.grnd_level = grnd_level;
        this.temp_kf = temp_kf;
    }

    protected WeatherDataExtended(Parcel in) {
        super(in);
        sea_level = in.readFloat();
        grnd_level = in.readFloat();
        temp_kf = in.readFloat();
    }

    public static final Creator<WeatherDataExtended> CREATOR = new Creator<WeatherDataExtended>() {
        @Override
        public WeatherDataExtended createFromParcel(Parcel in) {
            return new WeatherDataExtended(in);
        }

        @Override
        public WeatherDataExtended[] newArray(int size) {
            return new WeatherDataExtended[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(sea_level);
        dest.writeFloat(grnd_level);
        dest.writeFloat(temp_kf);
    }
}
