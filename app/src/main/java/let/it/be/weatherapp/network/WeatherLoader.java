package let.it.be.weatherapp.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import let.it.be.weatherapp.models.exceptions.ConnectionException;
import let.it.be.weatherapp.models.exceptions.RequestException;
import let.it.be.weatherapp.models.exceptions.ResponseException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class WeatherLoader {

    private static final String ESTONIA_MAP_POSITION = "22.34,57.71,28.25,59.70,10";

    private WeatherLoader() {
    }

    public static void loadCurrentAsync(@NonNull final ResultListener callback) {
        Call<CurrentWeatherData> call = WeatherProvider.requestCurrentWeather(ESTONIA_MAP_POSITION);
        call.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeatherData> call, @NonNull Response<CurrentWeatherData> response) {
                if (callback == null) return;
                if (!response.isSuccessful()) {
                        callback.onFailed(
                                new ResponseException("Response code: " + response.code())
                        );
                    return;
                }

                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeatherData> call, @NonNull Throwable t) {
                if (callback == null) return;
                callback.onFailed(new NetworkException(t));
            }
        });
    }

    public interface ResultListener {
        void onSuccess(CurrentWeatherData result);
        void onFailed(NetworkException error);
    }
}
