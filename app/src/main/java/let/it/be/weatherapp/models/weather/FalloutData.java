package let.it.be.weatherapp.models.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FalloutData implements Parcelable {

    @SerializedName("3h") public final float threeHour;

    public FalloutData(float threeHour) {
        this.threeHour = threeHour;
    }

    protected FalloutData(Parcel in) {
        threeHour = in.readFloat();
    }

    public static final Creator<FalloutData> CREATOR = new Creator<FalloutData>() {
        @Override
        public FalloutData createFromParcel(Parcel in) {
            return new FalloutData(in);
        }

        @Override
        public FalloutData[] newArray(int size) {
            return new FalloutData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(threeHour);
    }
}
