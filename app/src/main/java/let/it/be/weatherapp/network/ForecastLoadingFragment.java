package let.it.be.weatherapp.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.exceptions.ResponseException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.models.weather.WeatherForecastData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public final class ForecastLoadingFragment extends AbstractNetworkingFragment<WeatherForecastData> {

    public static final String TAG = ForecastLoadingFragment.class.getSimpleName();
    private final long cityId;

    /**
     * You might say that this is mistake and that fragment must be instantiated using empty
     * constructor. But that is not true for all cases. Main idea for empty fragment constructor
     * is for system to gracefully handle fragment re-creation. This is from docs : "fragment must
     * have an empty constructor, so it can be instantiated when restoring its activity's state".
     * But this fragment main idea is to maintain instance during configuration changes. But if
     * activity is recreated because of some other cases as low on memory, then this should be
     * handle by activity it self, since all information in this case should be loaded again
     * from internet.
     */
    public ForecastLoadingFragment(Activity activity, long cityId) {
        super(activity, TAG);
        this.cityId = cityId;
    }

    @Override
    protected Call<WeatherForecastData> getNetworkCall() {
        return WeatherProvider.requestCityForecast(String.valueOf(cityId));
    }

    public static ForecastLoadingFragment findFragment(Activity activity) {
        return (ForecastLoadingFragment) findFragment(activity, TAG);
    }
}
