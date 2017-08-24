package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class WindData implements Parcelable {
    public static final Creator<WindData> CREATOR = new Creator<WindData>() {
        @Override
        public WindData createFromParcel(Parcel in) {
            return new WindData(in);
        }

        @Override
        public WindData[] newArray(int size) {
            return new WindData[size];
        }
    };


    public final float speed;
    public final float deg;

    public WindData(float speed, float deg) {
        this.speed = speed;
        this.deg = deg;
    }

    protected WindData(Parcel in) {
        speed = in.readFloat();
        deg = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(speed);
        dest.writeFloat(deg);
    }
}
