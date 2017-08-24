package let.it.be.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import let.it.be.weatherapp.models.CityData;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CityWeatherData;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

/**
 * This activity has setup in styles 'passive' splash screen that wait for app
 * initialization by system (recommended by android patterns). After 'passive' splash screen it
 * displays 'active' splash screen that waits for in app settings to load.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private WeatherLoadingFragment workerFragment;
    private ResultListener<CurrentWeatherData> onSettingsLoaded = new ResultListener<CurrentWeatherData>() {
        @Override
        public void onSuccess(CurrentWeatherData result) {
            gotToNextActivity(result, null);
        }

        @Override
        public void onFailed(NetworkException error) {
            gotToNextActivity(null, error);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        workerFragment = WeatherLoadingFragment.findFragment(this);
        if (workerFragment == null) {
            workerFragment = new WeatherLoadingFragment(this);
            workerFragment.startDataLoading();
        }
        workerFragment.setResultListener(onSettingsLoaded);
    }

    private void gotToNextActivity(CurrentWeatherData result, NetworkException error) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (result != null) intent.putExtra(CurrentWeatherData.TAG, result);
        if (error != null) intent.putExtra(NetworkException.TAG, error);
        startActivity(intent);

        int favoriteCityId = getSharedPreferences(WeatherApp.TAG, Context.MODE_PRIVATE)
                .getInt(CityData.TAG, -1);
        if (favoriteCityId >= 0) {
            CityWeatherData favoriteCity = result.findCityById(favoriteCityId);
            if (favoriteCity != null) {
                Intent secondIntent = new Intent(this, ForecastActivity.class);
                secondIntent.putExtra(CityWeatherData.TAG, favoriteCity);
                startActivity(secondIntent);
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (workerFragment != null) {
            // this will stop loading process and kill fragment
            workerFragment.destroy();
        }
        super.onBackPressed();
    }
}
