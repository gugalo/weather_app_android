package let.it.be.weatherapp.network;

import android.annotation.SuppressLint;
import android.app.Activity;

import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import retrofit2.Call;

@SuppressLint("ValidFragment")
public final class WeatherLoadingFragment extends AbstractNetworkingFragment<CurrentWeatherData> {

    public static final String TAG = ForecastLoadingFragment.class.getSimpleName();
    private static final String ESTONIA_MAP_POSITION = "22.34,57.71,28.25,59.70,10";

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
    public WeatherLoadingFragment(Activity activity) {
        super(activity, TAG);
    }

    public static WeatherLoadingFragment findFragment(Activity activity) {
        return (WeatherLoadingFragment) findFragment(activity, TAG);
    }

    @Override
    protected Call<CurrentWeatherData> getNetworkCall() {
        return WeatherProvider.requestCurrentWeather(ESTONIA_MAP_POSITION);
    }
}
