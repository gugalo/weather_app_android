package let.it.be.weatherapp.network;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.exceptions.ResponseException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class WeatherLoadingFragment extends Fragment {

    private static final String ESTONIA_MAP_POSITION = "22.34,57.71,28.25,59.70,10";

    private CurrentWeatherData currentWeatherData;
    private ResultListener<CurrentWeatherData> listener;
    private NetworkException error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public CurrentWeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }

    public void setListener(ResultListener<CurrentWeatherData> listener) {
        this.listener = listener;
    }

    public void loadDataAsync() {
        currentWeatherData = null;
        error = null;
        Call<CurrentWeatherData> call = WeatherProvider.requestCurrentWeather(ESTONIA_MAP_POSITION);
        call.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeatherData> call, @NonNull Response<CurrentWeatherData> response) {
                if (!response.isSuccessful()) {
                    error = new ResponseException("Response code: " + response.code());
                    if (listener != null) listener.onFailed(error);
                    return;
                }

                currentWeatherData = response.body();
                if (listener != null) listener.onSuccess(currentWeatherData);
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeatherData> call, @NonNull Throwable t) {
                error = new NetworkException(t);
                if (listener != null) listener.onFailed(error);
            }
        });
    }
}
