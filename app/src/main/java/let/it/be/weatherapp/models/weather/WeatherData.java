package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WeatherData implements Parcelable {
    public final float temp;
    @SerializedName("temp_min") public final float tempMin;
    @SerializedName("temp_max") public final float tempMax;
    public final float pressure;
    public final float humidity;

    public WeatherData(float temp, float tempMin, float tempMax, float pressure, float humidity) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    protected WeatherData(Parcel in) {
        temp = in.readFloat();
        tempMin = in.readFloat();
        tempMax = in.readFloat();
        pressure = in.readFloat();
        humidity = in.readFloat();
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    public String getTempFormated() {
        return  String.format("%2.1f°", temp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(temp);
        dest.writeFloat(tempMin);
        dest.writeFloat(tempMax);
        dest.writeFloat(pressure);
        dest.writeFloat(humidity);
    }
}
