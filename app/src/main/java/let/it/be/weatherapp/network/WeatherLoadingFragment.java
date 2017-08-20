package let.it.be.weatherapp.network;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.exceptions.ResponseException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Headless fragment for loading weather data in background thread
 */
public final class WeatherLoadingFragment extends Fragment {

    private static final String ESTONIA_MAP_POSITION = "22.34,57.71,28.25,59.70,10";
    public static final String TAG = WeatherLoadingFragment.class.getSimpleName();

    private CurrentWeatherData currentWeatherData;
    private ResultListener<CurrentWeatherData> resultListener;
    private NetworkException error;
    private Call<CurrentWeatherData> currentNetworkCall;

    public static WeatherLoadingFragment newInstance(FragmentManager fm, ResultListener<CurrentWeatherData> listener) {
        WeatherLoadingFragment thisInstance = (WeatherLoadingFragment) fm.findFragmentByTag(TAG);
        if (thisInstance == null) {
            thisInstance = new WeatherLoadingFragment();
            fm.beginTransaction().add(thisInstance, TAG).commit();
        }
        thisInstance.setListener(listener);
        if (thisInstance.currentWeatherData != null) {
            listener.onSuccess(thisInstance.currentWeatherData);
        } else if (thisInstance.error != null) {
            listener.onFailed(thisInstance.error);
        } else if (thisInstance.currentNetworkCall == null) {
            thisInstance.loadDataAsync();
        } else {
            // this case means that thread is currently in progress already
            // do nothing
        }
        return thisInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public CurrentWeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }

    private void setListener(ResultListener<CurrentWeatherData> listener) {
        this.resultListener = listener;
    }

    private void loadDataAsync() {
        if (currentNetworkCall != null) {
            // do not start new loading process if it is already loading
            return;
        }
        currentWeatherData = null;
        error = null;
        currentNetworkCall = WeatherProvider.requestCurrentWeather(ESTONIA_MAP_POSITION);
        currentNetworkCall.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeatherData> call, @NonNull Response<CurrentWeatherData> response) {
                if (!response.isSuccessful()) {
                    error = new ResponseException("Response code: " + response.code());
                    if (resultListener != null) resultListener.onFailed(error);
                    return;
                }

                currentWeatherData = response.body();
                currentNetworkCall = null;
                if (resultListener != null) resultListener.onSuccess(currentWeatherData);
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeatherData> call, @NonNull Throwable t) {
                error = new NetworkException(t);
                currentNetworkCall = null;
                if (resultListener != null) resultListener.onFailed(error);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // remove reference to activity to prevent memory leak
        resultListener = null;
    }

    public void destroy() {
        stopBackgroundProcess();
        if (getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void stopBackgroundProcess() {
        if (currentNetworkCall != null) {
            currentNetworkCall.cancel();
            currentNetworkCall = null;
        }
    }

    @Override
    public void onDestroy() {
        stopBackgroundProcess();
        super.onDestroy();
    }

    public void retry() {
        loadDataAsync();
    }
}
