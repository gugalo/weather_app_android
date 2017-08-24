package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentWeatherData implements Parcelable {

    public static final String TAG = CurrentWeatherData.class.getSimpleName();

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

    public CityWeatherData findCityById(int cityId) {
        for (CityWeatherData item : list) {
            if (item.id == cityId) return item;
        }
        return null;
    }

    protected CurrentWeatherData(Parcel in) {
        cod = in.readInt();
        calctime = in.readFloat();
        cnt = in.readInt();
        list = in.createTypedArray(CityWeatherData.CREATOR);
    }

    public static final Creator<CurrentWeatherData> CREATOR = new Creator<CurrentWeatherData>() {
        @Override
        public CurrentWeatherData createFromParcel(Parcel in) {
            return new CurrentWeatherData(in);
        }

        @Override
        public CurrentWeatherData[] newArray(int size) {
            return new CurrentWeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cod);
        dest.writeFloat(calctime);
        dest.writeInt(cnt);
        dest.writeTypedArray(list, flags);
    }
}
