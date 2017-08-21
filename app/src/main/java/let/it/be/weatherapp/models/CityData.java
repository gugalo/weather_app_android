package let.it.be.weatherapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.internal.LinkedTreeMap;

public class CityData implements Parcelable {

    public static final String TAG = CityData.class.getSimpleName();

    public final int id;
    public final String name;
    public final LinkedTreeMap coord;
    public final String country;

    public CityData(int id, String name, LinkedTreeMap coord, String country) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
    }

    protected CityData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        coord = (LinkedTreeMap) in.readSerializable();
        country = in.readString();
    }

    public static final Creator<CityData> CREATOR = new Creator<CityData>() {
        @Override
        public CityData createFromParcel(Parcel in) {
            return new CityData(in);
        }

        @Override
        public CityData[] newArray(int size) {
            return new CityData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeSerializable(coord);
        dest.writeString(country);
    }
}
