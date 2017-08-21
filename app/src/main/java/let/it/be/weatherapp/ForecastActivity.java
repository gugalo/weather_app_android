package let.it.be.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import let.it.be.weatherapp.models.CityData;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.models.weather.WeatherForecastData;
import let.it.be.weatherapp.network.ForecastLoadingFragment;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

public class ForecastActivity extends AppCompatActivity {

    private static final String TAG = ForecastActivity.class.getSimpleName();

    private ForecastLoadingFragment workerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecat);


        workerFragment = getForecastLoadingFragment();
        if (savedInstanceState == null) {
            workerFragment.startDataLoading();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        // TODO: restoreState
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private ForecastLoadingFragment getForecastLoadingFragment() {
        ForecastLoadingFragment forecastFragment = ForecastLoadingFragment.findFragment(this);
        if (forecastFragment == null) {
            forecastFragment = new ForecastLoadingFragment(this, getIntent().getLongExtra(CityData.TAG, 0));
        }
        forecastFragment.setResultListener(forecastDataListener);
        return forecastFragment;
    }

    private ResultListener<WeatherForecastData> forecastDataListener = new ResultListener<WeatherForecastData>() {
        @Override
        public void onSuccess(WeatherForecastData result) {
            Log.d(TAG, "Forecast info loaded successfully");
            ((TextView) findViewById(R.id.cityId)).setText(result.city.name);
            // TODO: setup layout
        }

        @Override
        public void onFailed(NetworkException error) {
            Log.e(TAG, "Error loading forecast data", error);
            ((TextView) findViewById(R.id.cityId)).setText("Failed");
            // TODO: show error
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            // this means that this activity is not subject to orientation change but is destroyed
            // so we need to remove our worker thread/fragment here
            if (workerFragment != null) {
                workerFragment.destroy();
            }

            // other stuff to clean
            // TODO
        }
    }
}
