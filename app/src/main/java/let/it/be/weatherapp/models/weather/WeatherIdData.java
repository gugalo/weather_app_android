package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherIdData implements Parcelable {
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

    protected WeatherIdData(Parcel in) {
        id = in.readInt();
        main = in.readString();
        description = in.readString();
        icon = in.readString();
    }

    public static final Creator<WeatherIdData> CREATOR = new Creator<WeatherIdData>() {
        @Override
        public WeatherIdData createFromParcel(Parcel in) {
            return new WeatherIdData(in);
        }

        @Override
        public WeatherIdData[] newArray(int size) {
            return new WeatherIdData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(main);
        dest.writeString(description);
        dest.writeString(icon);
    }
}
